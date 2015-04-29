import java.io.IOException;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class DynamicSysInfo {

	private static Sigar sigar = new Sigar();
	private static Controller control = new Controller();
	private static ApiHandler apiH = new ApiHandler();
	private static PropertiesHandler propH = new PropertiesHandler();

	public void sendInfo() throws SigarException{
		apiH.apiCall("cpu/1/", cpuInfoD());
		apiH.apiCall("ram/", ramInfoD());
		apiH.apiCall("", sysInfoD());
		/*try {
			apiH.apiCall("network/1/", networkInfo());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		diskInfo();*/
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


}
