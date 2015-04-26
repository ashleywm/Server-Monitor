import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.filechooser.FileSystemView;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class Controller {

	// static ConfigReader properties = new ConfigReader();
	private static String sysName = "";
	private static FileSystemController fsc;
	private static String DEFAULT_LOCATION = System.getenv("SystemDrive") + "\\Monitoring\\";
	private static String DEFAULT_FILE = "config.properties";

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

	}

	public static void nameSystem() {
		Scanner nameIn = new Scanner(System.in);

		System.out.println("Please enter a friendly system name ");
		sysName = nameIn.nextLine();
		while (sysName.equalsIgnoreCase("")) {
			System.out.println("Please enter a friendly system name ");
			sysName = nameIn.nextLine();
		}
		System.out.println(sysName);

	}

	public static void updateConfig(){

		Scanner input = new Scanner(System.in);

		String in = ""; // initialisation  

		in = input.next();
		if(in.equalsIgnoreCase("y")){
			System.out.println("yyyy");
		}else{
			while(!(in.equalsIgnoreCase("n"))){
				System.out.println("Configuration is valid, do you want to force a system check? Y/N");
				in = input.next();
			}
			
		}
		input.close();
	}



	public static void main(String args[]) throws SigarException, IOException {





		FileSystemController fsc = new FileSystemController();
		ConfigWriter cw = new ConfigWriter();
		ConfigReader cr = new ConfigReader();


		if(fsc.checkDir(DEFAULT_LOCATION) && fsc.checkFile(DEFAULT_LOCATION, DEFAULT_FILE)){ //if the folder or file doesn't exist 

			System.out.println("The system needs to be configured to start monitoring");
			enterContinue();
			nameSystem();

			fsc.makeDir(DEFAULT_LOCATION);
			cw.writeInitial(sysName, DEFAULT_LOCATION+DEFAULT_FILE);

			System.out.println("Configuration has been Made in " +DEFAULT_LOCATION+DEFAULT_FILE);
		}else if(!(fsc.checkDir(DEFAULT_LOCATION)) && fsc.checkFile(DEFAULT_LOCATION, DEFAULT_FILE)){  //if the folder exists but the file does not 

			System.out.println("The system needs to be configured to start monitoring");
			enterContinue();
			nameSystem();

			cw.writeInitial(sysName, DEFAULT_LOCATION+DEFAULT_FILE);

			System.out.println("Configuration has been Made in " +DEFAULT_LOCATION+DEFAULT_FILE);

		}else if(!(fsc.checkDir(DEFAULT_LOCATION) && fsc.checkFile(DEFAULT_LOCATION, DEFAULT_FILE))){ //if both exist 
			if(!(cr.checkConfig(DEFAULT_LOCATION, DEFAULT_FILE))){
				System.out.println("The system needs to be configured to start monitoring");
				enterContinue();
				nameSystem();

				cw.writeInitial(sysName, DEFAULT_LOCATION+DEFAULT_FILE);

				System.out.println("Configuration has been Made in " +DEFAULT_LOCATION+DEFAULT_FILE);
			}else{
				System.out.println("Configuration is valid, do you want to force a system check? Y/N \n(Recommended if you have changed system hardware)");
				updateConfig();
			}

		}
		
		repeater();
		
	}

}
