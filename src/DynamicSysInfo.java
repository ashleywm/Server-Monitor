
import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;








import javax.swing.filechooser.FileSystemView;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class DynamicSysInfo {

	private static Sigar sigar = new Sigar();
	private static Controller control = new Controller();
	private static ApiHandler apiH = new ApiHandler();
	private static PropertiesHandler propH = new PropertiesHandler();


	private static long oldRead, oldWrite;

	static Map<String, Long> rxCurrentMap = new HashMap<String, Long>();
	static Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
	static Map<String, Long> txCurrentMap = new HashMap<String, Long>();
	static Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();

	public void sendInfo() throws SigarException{
		apiH.apiCall("cpu/1/", cpuInfoD());
		apiH.apiCall("ram/", ramInfoD());
		apiH.apiCall("network/", networkInfoD());
		//apiH.apiCall("disks/", 
		diskInfoD();
		apiH.apiCall("", sysInfoD());

	}

	private void diskInfoD() throws SigarException {




		String readSpeed = null;
		String writeSpeed = null; 

		File[] paths;

		int id = 0;

		paths = File.listRoots();

		int arrayLength = paths.length * 2;
		for(File path:paths){
			long diskSize = new File(path.toString()).getTotalSpace();

			if(diskSize < 0){
				arrayLength = arrayLength - 1;
			}
		}

		long[] diskArray = new long[arrayLength];

		for(File path:paths){

			long diskSize = new File(path.toString()).getTotalSpace();

			if(diskSize > 0){
				String disk = (path).toString();
				System.out.println(disk);
				FileSystemUsage usage = sigar.getFileSystemUsage(disk+"\\");

				JSONObject diskSend = new JSONObject();

				long read = usage.getDiskReadBytes()/1024/1024; //current
				long write = usage.getDiskWriteBytes()/1024/1024; //current

				long readDiff = read - diskArray[id];
				long writeDiff = write - diskArray[id + 1];

				readSpeed = Long.toString(readDiff/5);
				writeSpeed = Long.toString(writeDiff/5);

				diskArray[id] = read;
				diskArray[id + 1] = write;

				System.out.println("Read :"+readSpeed+" Write :"+writeSpeed);

				long space = new File(disk+"\\").getFreeSpace();

				diskSend.put("read_speed ", readSpeed);
				diskSend.put("write_speed", writeSpeed);
				diskSend.put("remaining_space", Long.toString(space/1024/1024));


				apiH.apiCall("disk/"+id+"/", diskSend);

				id+=2;
			}
		}
	}


	public static JSONObject sysInfoD() throws SigarException{
		JSONObject sys = new JSONObject();

		String uptime = Double.toString(sigar.getUptime().getUptime());

		sys.put("uptime", uptime);

		return sys;
	}

	public static JSONObject cpuInfoD() throws SigarException{
		JSONObject cpu = new JSONObject();

		CpuPerc cpuperc = sigar.getCpuPerc();

		Integer percent = Integer.valueOf((int) Math.round(cpuperc.getCombined()*100));

		cpu.put("cpu_usage_percentage", percent);

		return cpu;
	}


	public static JSONObject ramInfoD() throws SigarException{
		JSONObject mem = new JSONObject();

		String ramUsed = Long.toString(sigar.getMem().getUsed()/1024/1024);

		mem.put("in_use", ramUsed);

		return mem;
	}

	public static JSONObject networkInfoD() throws SigarException{

		JSONObject net = new JSONObject();
		Long[] m = getMetric();
		long totalrx = m[0];
		long totaltx = m[1];

		String down = Long.toString(totalrx/1024);
		String up = Long.toString(totaltx/1024);
		net.put("upload_total", up);
		net.put("download_total", down);

		return net;
	}

	//http://stackoverflow.com/questions/11034753/sigar-network-speed
	//COPY PASTED

	public static Long[] getMetric() throws SigarException {

		for (String ni : sigar.getNetInterfaceList()) {

			NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
			NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
			String hwaddr = null;
			if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
				hwaddr = ifConfig.getHwaddr();
			}
			if (hwaddr != null) {
				long rxCurrenttmp = netStat.getRxBytes();
				saveChange(rxCurrentMap, rxChangeMap, hwaddr, rxCurrenttmp, ni);
				long txCurrenttmp = netStat.getTxBytes();
				saveChange(txCurrentMap, txChangeMap, hwaddr, txCurrenttmp, ni);
			}
		}
		long totalrx = getMetricData(rxChangeMap);
		long totaltx = getMetricData(txChangeMap);
		for (List<Long> l : rxChangeMap.values())
			l.clear();
		for (List<Long> l : txChangeMap.values())
			l.clear();
		return new Long[] { totalrx, totaltx };
	}


	private static void saveChange(Map<String, Long> currentMap,
			Map<String, List<Long>> changeMap, String hwaddr, long current,
			String ni) {
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

	private static long getMetricData(Map<String, List<Long>> rxChangeMap) {
		long total = 0;
		for (Entry<String, List<Long>> entry : rxChangeMap.entrySet()) {
			int average = 0;
			for (Long l : entry.getValue()) {
				average += l;
			}
			total += average / entry.getValue().size();
		}
		return total;
	}


}
