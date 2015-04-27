import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class InitialSysInfo {

	private static Sigar sigar = new Sigar();
	private static Controller control = new Controller();
	private static ApiHandler apiH = new ApiHandler();

	public void sendInfo() throws SigarException{
		apiH.apiCall("/cpu/1/", cpuInfo());
		apiH.apiCall("/ram/", ramInfo());
		apiH.apiCall("", sysInfo());

	}

	public static JSONObject cpuInfo() throws SigarException{

		//TODO: WE NEED TO ACCOUNT FOR MULTIE CPU SYSTEMS

		JSONObject cpu = new JSONObject();

		CpuInfo[] infos = sigar.getCpuInfoList();
		CpuInfo info = infos[0];

		cpu.put("vendor", info.getVendor());
		cpu.put("model", info.getModel());
		cpu.put("clockSpeed", info.getMhz());
		cpu.put("totalCores", info.getTotalCores());

		return cpu;
	}

	public static JSONObject ramInfo() throws SigarException{
		JSONObject mem = new JSONObject();

		mem.put("total", sigar.getMem().getTotal()/1024/1024);

		return mem;
	}


	public static JSONObject sysInfo() throws SigarException{
		JSONObject sys = new JSONObject();

		sys.put("operating_system", System.getProperty("os.name"));
		sys.put("name", control.getSysName());
	
		return sys;
	}


}
