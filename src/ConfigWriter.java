import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class ConfigWriter {

	
	public void writeInitial(String name, String loc) throws SigarException{

		Sigar sigar = new Sigar();
	

		CpuInfo[] infos = sigar.getCpuInfoList();
		CpuInfo info = infos[0];
	
		Properties prop = new Properties();
		String clock = Integer.toString(info.getMhz());
		String cores = Integer.toString(info.getTotalCores());
		String cache = Long.toString(info.getCacheSize());
		
		 
        try {

            prop.setProperty("System_Name", name);
            prop.setProperty("CPU_Vendor", info.getVendor());
            prop.setProperty("CPU_Model", info.getModel());
            prop.setProperty("CPU_Clock_Speed", clock );
            prop.setProperty("CPU_Total_Cores", cores);
            prop.setProperty("CPU_Cache_Size", cache);

            FileOutputStream fo = new FileOutputStream(loc);
            prop.store(fo, null);
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }

	}
}


