import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;

import javax.swing.filechooser.FileSystemView;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SystemInfo {
	
	private static final String OS_LINUX = "Linux";
	private static final String OS_WINDOWS = "Windows";
	//TODO: BSD, Solaris etc.
	
	public static boolean isLinux(){
		if(System.getProperty("os.name").equals(OS_LINUX)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isWindows(){
		if(System.getProperty("os.name").startsWith(OS_WINDOWS)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void windowsDiskInfo(Sigar sigar){
		//Windows Directory Stats
		try {
			System.out.println(sigar.getDirStat("C:/").toMap());
		} catch (SigarException e) {
			e.printStackTrace();
		}
		
		//Windows Disk Size
		long diskSize = new File("C:/").getTotalSpace()/1024/1024/1024; //bit
		System.out.println(diskSize);
	}
	
	public static void linuxDiskInfo(Sigar sigar){
		//Linux Directory Stats
		try {
			System.out.println(sigar.getDirStat("/").toMap());
		} catch (SigarException e) {
			e.printStackTrace();
		}
		
		//Linux Disk Size
		long diskSize = new File("/").getTotalSpace() / 1024 / 1024 / 1024;
		System.out.println(diskSize);
	}
	
	public static void jsonizer() throws JSONException, SigarException{
		JSONObject root = new JSONObject();
		
		root.append("user", userJson());
		root.append("cpu", cpuJson());
		root.append("memory", memJson());
		System.out.println(root);
	}
	
	public static JSONObject userJson() throws SigarException{
		Sigar sigar = new Sigar();
		JSONObject user = new JSONObject();
		user.append("client", sigar.getFQDN());
		user.append("hostname", sigar.getNetInfo().getHostName());
		return user;
	}
	
	public static JSONObject cpuJson() throws SigarException{
		Sigar sigar = new Sigar();
		JSONObject cpu = new JSONObject();
		

		CpuInfo[] infos = sigar.getCpuInfoList();
		CpuInfo info = infos[0];
				
		cpu.append("vendor", info.getVendor());
		cpu.append("model", info.getModel());
		cpu.append("clockSpeed", info.getMhz());
		cpu.append("totalCores", info.getTotalCores());
		cpu.append("cacheSize", info.getCacheSize());
		
		/*if ((info.getTotalCores() != info.getTotalSockets()) ||
				(info.getCoresPerSocket() < info.getTotalCores()))
		{
			System.out.println("Number of sockets.." + info.getTotalSockets());
			System.out.println("Cores per CPU.." + info.getCoresPerSocket());
		}*/
		
		return cpu;
	}
	
	public static JSONObject memJson() throws SigarException{
		Sigar sigar = new Sigar();
		JSONObject mem = new JSONObject();
		mem.append("totalRam", sigar.getMem().getTotal()/1024/1024);
		mem.append("usedRam", sigar.getMem().getUsedPercent());
		return mem;
	}
	

	public static void getSysInfo() throws SigarException{
		jsonizer();
		/*
		Sigar sigar = new Sigar();
		org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
		System.out.println("Client:" + sigar.getFQDN());
		System.out.println("Hostname:" + sigar.getNetInfo().getHostName());
		System.out.println(System.getProperty("os.name"));

		org.hyperic.sigar.CpuInfo info = infos[0];
		System.out.println("Vendor........." + info.getVendor());
		System.out.println("Model.........." + info.getModel());
		System.out.println("Mhz............" + info.getMhz());
		System.out.println("Total Cores...." + info.getTotalCores());
		System.out.println("Cache Size....." + info.getCacheSize());
		if ((info.getTotalCores() != info.getTotalSockets()) ||
				(info.getCoresPerSocket() < info.getTotalCores()))
		{
			System.out.println("Number of sockets.." + info.getTotalSockets());
			System.out.println("Cores per CPU.." + info.getCoresPerSocket());
		}
		System.out.println(sigar.getMem().getTotal()/1024/1024);

		System.out.println(sigar.getMem().getUsedPercent());
		
		


		try {
			System.out.println(NetworkInterface.getNetworkInterfaces());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if(isWindows()){
			windowsDiskInfo(sigar);
		}else if(isLinux()){
			linuxDiskInfo(sigar);
		}else{
			//why are you using bsd...
		}


		File[] paths;
		FileSystemView fsv = FileSystemView.getFileSystemView();

		// returns pathnames for files and directory
		paths = File.listRoots();

		// for each pathname in pathname array
		for(File path:paths)
		{
			// prints file and directory paths
			if(!("CD Drive".equalsIgnoreCase(fsv.getSystemTypeDescription(path)))){
				System.out.println("Drive Name: "+path);
				System.out.println("Description: "+fsv.getSystemTypeDescription(path));
			}
		}*/

	}
}

