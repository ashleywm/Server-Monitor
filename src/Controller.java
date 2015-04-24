import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.hyperic.sigar.SigarException;

public class Controller {

	// static ConfigReader properties = new ConfigReader();
	private static String sysName;
	private static FileSystemController fsc;
	private static String DEFAULT_LOCATION = System.getenv("SystemDrive") + "\\Monitoring\\config.properties";
	
	public static void repeater() {
		int initialDelay = 10000; // start after 30 seconds
		int period = 5000; // repeat every 5 seconds
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {

			public void run() {
				task(); // do task

			}
		};
		timer.scheduleAtFixedRate(task, initialDelay, period);

	}

	public static void task() {
		try {
			SystemInfo.getSysInfo();
		} catch (SigarException e) {
			e.printStackTrace();
		}
	}

	public static void enterContinue() {
		System.out.println("Press enter to continue...");
		Scanner keyIn = new Scanner(System.in);
		keyIn.nextLine();
		keyIn.close();
	}

	public static void nameSystem() {
		Scanner keyIn = new Scanner(System.in);

		System.out.println("Please enter a friendly system name ");
		sysName = keyIn.nextLine();
		while (sysName.equalsIgnoreCase("")) {
			System.out.println("Please enter a friendly system name ");
			sysName = keyIn.nextLine();
		}
		System.out.println(sysName);
		keyIn.close();
	}

	public static void main(String args[]) {
		/*String loc = System.getenv("SystemDrive") + "\\config.properties";
		
		try {
			fsc = new FileSystemController(new ConfigWriter(loc), new ConfigReader(loc));
			fsc.getWriter().
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(properties.isConfigure()){ //if configure is true then configuration is required
			System.out.println("The system needs to be configured to start monitoring");
			enterContinue();
			nameSystem();
			
			//ConfigWriter cw = new ConfigWriter();
			//cw.write(sysName, properties.getLocation());
			
		}
	}*/
		FileSystemController fsc = new FileSystemController();
		
		if(!(fsc.checkDir(DEFAULT_LOCATION))){
			fsc.makeFile(DEFAULT_LOCATION);
		}else{
		System.out.println("outtttt");	
		}
		
		repeater();
	}

}
