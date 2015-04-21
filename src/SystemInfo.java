import java.io.File;

import java.net.NetworkInterface;
import java.net.SocketException;

import javax.swing.filechooser.FileSystemView;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SystemInfo {
	
	private static final String OS_LINUX = "Linux";
	private static final String OS_WINDOWS = "Windows";
	
	public static boolean isLinux(){
		if(System.getProperty("os.name").equals(OS_LINUX)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isWindows(){
		if(System.getProperty("os.name").equals(OS_WINDOWS)){
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Linux Disk Size
		long diskSize = new File("/").getTotalSpace() / 1024 / 1024 / 1024;
		System.out.println(diskSize);
	}
	

	public static void getSysInfo() throws SigarException{
		Sigar sigar = new Sigar();
		org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
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
		}

	}
}

