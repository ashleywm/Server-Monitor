import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.hyperic.sigar.SigarException;


public class Controller {

	private static String sysName = "", token;
	private static PropertiesHandler propH = new PropertiesHandler();
	private static StaticSysInfo isi = new StaticSysInfo();
	private static ApiHandler apiH = new ApiHandler();
	public static final String DEFAULT_LOCATION = System.getProperty("user.home") + "/Monitoring/";
	public static final String DEFAULT_FILE = "config.properties";


	public static void repeater() {
		int initialDelay = 10000; // start after 30 seconds
		int period = 10000; // repeat every 5 seconds
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {

			public void run() {
				task(); // do task

			}
		};
		timer.scheduleAtFixedRate(task, initialDelay, period);

	}

	public static void task() {
			DynamicSysInfo dsi = new DynamicSysInfo();
			try {
				dsi.sendInfo();
			} catch (SigarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void enterContinue(String to) {
		
		if (to == ""){
		System.out.println("Press enter to continue...");
		}else{
			System.out.println("Press enter to "+to);
		}
		
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
	}

	public static void userkey() {
		Scanner keyIn = new Scanner(System.in);

		String key;
		
		System.out.println("Please enter the system key ");
		key = keyIn.next();
		while (key.equalsIgnoreCase("")) {
			System.out.println("Please enter the system key ");
			key = keyIn.next();
		}
		token = apiH.getToken(key);

	}
	
	public static void updateConfig(){

		Scanner input = new Scanner(System.in);

		String in = ""; // initialisation  

		in = input.next();
		if(in.equalsIgnoreCase("y")){
			try {
				System.out.println("Updating...");
				isi.sendInfo();
			} catch (SigarException e) {
				throw new RuntimeException("Failed : Unable to update the system properties");
			}
		}else{
			while(!(in.equalsIgnoreCase("n"))){
				System.out.println("Configuration is valid, do you want to force a system check? Y/N");
				in = input.next();
			}
			
		}
		input.close();
	}

	public static void starter() throws IOException{
		enterContinue("");
		nameSystem();
		userkey();
	}

	public static void main(String args[]) throws SigarException, IOException {
		
		System.out.println("Welcome to PULSr, the monitoring service for your multi-platform server\n");
		
		if(propH.checkDir() && propH.checkFile()){ //if the folder or file doesn't exist 
			System.out.println("The system needs to be configured to start monitoring");
			starter();

			propH.makeDir();
			propH.writeInitial(sysName, token, DEFAULT_LOCATION+DEFAULT_FILE);

			System.out.println("Configuration has been Made in " +DEFAULT_LOCATION+DEFAULT_FILE + "\nThe new installation needs to send initial data to the service");
			enterContinue("");
			propH.storeToken();
			isi.sendInfo();
		}else if(!(propH.checkDir()) && propH.checkFile()){  //if the folder exists but the file does not 

			System.out.println("The system needs to be configured to start monitoring");
			starter();
			
			propH.writeInitial(sysName, token, DEFAULT_LOCATION+DEFAULT_FILE);

			System.out.println("A new server configuration has been Made in " +DEFAULT_LOCATION+DEFAULT_FILE + "\nThe new installation needs to send initial data to the service");
			enterContinue("");
			propH.storeToken();
			isi.sendInfo();

		}else if(!(propH.checkDir() && propH.checkFile())){ //if both exist 
			if(!(propH.checkConfig())){
				System.out.println("The system needs to be configured to start monitoring");
				starter();

				propH.writeInitial(sysName, token, DEFAULT_LOCATION+DEFAULT_FILE);
				System.out.println("A new server configuration has been Made in " +DEFAULT_LOCATION+DEFAULT_FILE);
			}else if(propH.checkConfig()){
				System.out.println("Current configuration is valid, do you want to force a system check?\n(Recommended if you have changed system hardware or haven't used PULSr in a while)\ny/n ");
				propH.storeToken();
				
				updateConfig();
				System.out.println("...Done\n");
				//enterContinue("start monitoring your system\n");
				
			}

		}
		
	
		System.out.println("PULSr is now monitoring your system go to http://student20265.201415.uk/pmt/");
		
		repeater();

	}

	public String getSysName() {
		return sysName;
	}

}
