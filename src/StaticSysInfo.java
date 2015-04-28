import java.io.File;
	
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class StaticSysInfo {

	private static Sigar sigar = new Sigar();
	private static Controller control = new Controller();
	private static ApiHandler apiH = new ApiHandler();

	public void sendInfo() throws SigarException{
		apiH.apiCall("cpu/1/", cpuInfo());
		apiH.apiCall("ram/", ramInfo());
		apiH.apiCall("", sysInfo());
		diskInfo();

	}

	public static JSONObject cpuInfo() throws SigarException{

		JSONObject cpu = new JSONObject();

		CpuInfo[] infos = sigar.getCpuInfoList();
		CpuInfo info = infos[0];

		cpu.put("vendor", info.getVendor());
		cpu.put("model", info.getModel());
		cpu.put("clockSpeed", info.getMhz());
		cpu.put("totalCores", info.getTotalCores());

		return cpu;
	}

	public static void diskInfo() throws SigarException{

		JSONObject disks = new JSONObject();
		
		File[] paths;

		paths = File.listRoots();

		for(File path:paths)
		{
			long diskSize = new File(path.toString()).getTotalSpace();
			diskSize = diskSize/1024/1024; //mb 
			if(diskSize > 0){
			
				System.out.println(diskSize);
				disks.put("total_space", diskSize);
			}
		}

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
