import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.filechooser.FileSystemView;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class StaticSysInfo {

	private static Sigar sigar = new Sigar();
	private static Controller control = new Controller();
	private static ApiHandler apiH = new ApiHandler();
	private static PropertiesHandler propH = new PropertiesHandler();

	public void sendInfo() throws SigarException{
		apiH.apiCall("cpu/1/", cpuInfo());
		apiH.apiCall("ram/", ramInfo());
		apiH.apiCall("", sysInfo());
		try {
			apiH.apiCall("network/", networkInfo());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		diskInfo();
	}

	public static JSONObject cpuInfo() throws SigarException{

		JSONObject cpu = new JSONObject();

		CpuInfo[] infos = sigar.getCpuInfoList();
		CpuInfo info = infos[0];

		cpu.put("vendor", info.getVendor());
		cpu.put("model", info.getModel());
		cpu.put("clockSpeed", Integer.toString(info.getMhz()));
		cpu.put("totalCores", info.getTotalCores());

		return cpu;
	}

	public static void diskInfo() throws SigarException{

		JSONObject disks = new JSONObject();

		File[] paths;

		paths = File.listRoots();

		int id = 1;

		for(File path:paths){
			long diskSize = new File(path.toString()).getTotalSpace();
			diskSize = diskSize/1024/1024; //mb 
			if(diskSize > 0){
				JSONObject disk = new JSONObject();

				disk.put("disk_id", id);
				disk.put("total_space", diskSize);	
				disk.put("disk_name", FileSystemView.getFileSystemView().getSystemDisplayName (path));
				
				disks.put("total_space_"+id, diskSize);	
				disks.put("disk_name_"+id, FileSystemView.getFileSystemView().getSystemDisplayName (path));

				apiH.apiCall("disk/"+id+"/", disk);

				id++;
			}
		}
		

		try {
			propH.storeDisk(disks);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static JSONObject networkInfo() throws SigarException, IOException{

		JSONObject net = new JSONObject();

		URL getPublic = new URL("http://checkip.amazonaws.com");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(getPublic.openStream()));
		String publicIP = buffer.readLine(); //you get the IP as a String

		InetAddress localIP = InetAddress.getLocalHost();


		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)){
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {

				String local = localIP.getHostAddress().toString();
				String address = inetAddress.getHostAddress().toString();

				if(local.equalsIgnoreCase(address) ){

					String netName = netint.getName();
					System.out.println("NETWORK ADAPATER NAME: "+netName );

				}
			}
		}
		net.put("ip_address", localIP.getHostAddress());
		net.put("hostname", sigar.getNetInfo().getHostName());
		net.put("gateway",sigar.getNetInfo().getDefaultGateway());
		net.put("public_ip", publicIP);
		

		return net;

	}


	public static JSONObject ramInfo() throws SigarException{
		JSONObject mem = new JSONObject();

		String ramTotal = Long.toString(sigar.getMem().getTotal()/1024/1024);

		mem.put("total", ramTotal);

		System.out.println(mem);

		return mem;
	}


	public static JSONObject sysInfo() throws SigarException{
		JSONObject sys = new JSONObject();

		sys.put("operating_system", System.getProperty("os.name"));
		sys.put("name", control.getSysName());

		return sys;
	}


}
