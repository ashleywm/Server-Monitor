import java.io.IOException;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;
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

	public void sendInfo() throws SigarException{
		apiH.apiCall("cpu/1/", cpuInfoD());
		apiH.apiCall("ram/", ramInfoD());
		apiH.apiCall("network/1/", networkInfoD());
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

	public static JSONObject networkInfoD() throws SigarException{
		JSONObject net = new JSONObject();

		String ramUsed = Long.toString(sigar.getMem().getUsed()/1024/1024);

		String netName ;
		
		if(propH.getNetName() == null){
			try {
				propH.getEth();
			} catch (IOException e) {
				e.printStackTrace();
			}
			netName = propH.getNetName();
		}else{
			netName = propH.getNetName();
		}
		
		NetInterfaceStat netStat = sigar.getNetInterfaceStat(netName);
		NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(netName);

        String hwaddr = null;
        if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
            hwaddr = ifConfig.getHwaddr();
        }
        if (hwaddr != null) {
		String down = Long.toString(netStat.getRxBytes()/1024);

		String up = Long.toString(netStat.getTxBytes()/1024);
		
		net.put("upload_total ", up);
		net.put("download_total ", down);
        }
		
	

		return net;
	}


}
