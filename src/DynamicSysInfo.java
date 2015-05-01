import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class DynamicSysInfo {

	private static Sigar sigar = new Sigar();
	private static ApiHandler apiH = new ApiHandler();

	static Map<String, Long> newDown = new HashMap<String, Long>();
	static Map<String, List<Long>> oldDown = new HashMap<String, List<Long>>();
	static Map<String, Long> newUp = new HashMap<String, Long>();
	static Map<String, List<Long>> oldUp = new HashMap<String, List<Long>>();

	public void sendInfo() throws SigarException{
		apiH.apiCall("cpu/1/", cpuInfoD()); //these are all the infos that are being sent to the api 
		apiH.apiCall("ram/", ramInfoD());
		apiH.apiCall("network/", networkInfoD()); 
		apiH.apiCall("", sysInfoD());
		diskInfoD();
	}

	private void diskInfoD() throws SigarException {
		String readSpeed = null;
		String writeSpeed = null; 

		File[] paths;

		int id = 0;

		paths = File.listRoots(); //get a list of all the folder roots

		int arrayLength = paths.length * 2;
		for(File path:paths){ //for every disk/parition
			long diskSize = new File(path.toString()).getTotalSpace(); //get the total space

			if(diskSize < 0){ //make sure it's not a dvd drive 
				arrayLength = arrayLength - 1;
			}
		}

		long[] diskArray = new long[arrayLength];

		for(File path:paths){ //for every valid paths 

			long diskSize = new File(path.toString()).getTotalSpace();

			if(diskSize > 0){
				String disk = (path).toString(); //get the paths
	
				FileSystemUsage usage = sigar.getFileSystemUsage(disk+"\\"); //get the usage of the path

				JSONObject diskSend = new JSONObject(); //create a diskjson 

				long read = usage.getDiskReadBytes()/1024/1024; //current
				long write = usage.getDiskWriteBytes()/1024/1024; //current

				long readDiff = read - diskArray[id]; //get the old disk read and minus it by the current, the difference is then read speed
				long writeDiff = write - diskArray[id + 1];  //get the old disk write and minus it by the current, the difference is then write speed

				readSpeed = Long.toString(readDiff/5);
				writeSpeed = Long.toString(writeDiff/5);

				diskArray[id] = read;
				diskArray[id + 1] = write;

				long space = new File(disk+"\\").getFreeSpace(); //get the amount of space left on the disk 

				diskSend.put("read_speed ", readSpeed);
				diskSend.put("write_speed", writeSpeed);
				diskSend.put("remaining_space", Long.toString(space/1024/1024));


				apiH.apiCall("disk/"+id+"/", diskSend);

				id+=2;
			}
		}
	}


	public static JSONObject sysInfoD() throws SigarException{
		
		//get and send the uptime of the system 
		
		JSONObject sys = new JSONObject();

		String uptime = Double.toString(sigar.getUptime().getUptime());

		sys.put("uptime", uptime);

		return sys;
	}

	public static JSONObject cpuInfoD() throws SigarException{
		JSONObject cpu = new JSONObject();

		CpuPerc cpuperc = sigar.getCpuPerc();
		
		//get and round the overall cpu usage 

		Integer percent = Integer.valueOf((int) Math.round(cpuperc.getCombined()*100));

		cpu.put("cpu_usage_percentage", percent);

		return cpu;
	}


	public static JSONObject ramInfoD() throws SigarException{
		JSONObject mem = new JSONObject();

		//get the total ram usage in mb
		
		String ramUsed = Long.toString(sigar.getMem().getUsed()/1024/1024);

		mem.put("in_use", ramUsed);

		return mem;
	}

	public static JSONObject networkInfoD() throws SigarException{

		JSONObject net = new JSONObject();
		Long[] l = getMetric();
		long downLong = l[0];
		long upLong = l[1];

		String down = Long.toString(downLong/1024);
		String up = Long.toString(upLong/1024);
		net.put("upload_total", up);
		net.put("download_total", down);

		return net;
	}


	public static Long[] getMetric() throws SigarException {

		String nicList[] = sigar.getNetInterfaceList();
		long totalDown = 0;
		long totalUp = 0;
		int average = 0;
		
		// for very network interface 
		//due to multiplicity issues the up and down is the total network up and down not for each inividual nic 
		for (String nic : nicList) {
			NetInterfaceStat netInterface = sigar.getNetInterfaceStat(nic);
			NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(nic);
			String hwaddr = null;
			if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
				hwaddr = ifConfig.getHwaddr();
			}
			if (hwaddr != null) {
				long newDownTmp = netInterface.getRxBytes(); //get the current amount of bytes that have been downloaded
				saveChange(newDown, oldDown, hwaddr, newDownTmp, nic); //compare old values to the new and the difference is the speed
				long newUpTmp = netInterface.getTxBytes();  //get the current amount of bytes that have been uploaded
				saveChange(newUp, oldUp, hwaddr, newUpTmp, nic);  //compare old values to the new and the difference is the speed
			}
		}
		
		for (Entry<String, List<Long>> entry : oldDown.entrySet()) {
			for (Long l : entry.getValue()) {
				average += l;
			}
			totalDown += average / entry.getValue().size();
		}
		
		//do value conversions
		
		
		for (Entry<String, List<Long>> entry : oldUp.entrySet()) {
			for (Long l : entry.getValue()) {
				average += l;
			}
			totalUp += average / entry.getValue().size();
		}
		
		for (List<Long> l : oldDown.values()){
			l.clear();
		}
		for (List<Long> l : oldUp.values()){
			l.clear();
		}
		
		//clean up 
	
		return new Long[] { totalDown, totalUp };
	}

	//please see documentation about this section of code, thanks
	private static void saveChange(Map<String, Long> currentMap, Map<String, List<Long>> changeMap, String hwaddr, long current,String ni) {
		Long oldCurrent = currentMap.get(ni);
		if (oldCurrent != null) {
			List<Long> list = changeMap.get(hwaddr);
			if (list == null) {
				list = new LinkedList<Long>();
				changeMap.put(hwaddr, list);
			}
			list.add((current - oldCurrent));
		}
		currentMap.put(ni, current);
	}
}
