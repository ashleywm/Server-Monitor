import java.io.File;

import java.net.NetworkInterface;
import java.net.SocketException;

import javax.swing.filechooser.FileSystemView;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SystemInfo {

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
		System.out.println(sigar.getDirStat("C:/").toMap());


		try {
			System.out.println(NetworkInterface.getNetworkInterfaces());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long diskSize = new File("C:/").getTotalSpace()/1024/1024/1024; //bit
		System.out.println(diskSize);

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

