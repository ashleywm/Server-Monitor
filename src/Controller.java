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
	private static String DEFAULT_LOCATION = PropertiesHandler.getDefaultLocation(); //return the location 
	private static String DEFAULT_FILE = PropertiesHandler.getDefaultFile(); //return file 


	public static void repeater() {
		int initialDelay = 10000; // start after 10 seconds
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
		
		//the task is what the repeater repeats every 5 seconds
			DynamicSysInfo dsi = new DynamicSysInfo();
			try {
				dsi.sendInfo(); //update the info 
			} catch (SigarException e) {
				e.printStackTrace();
			}
	}

	public static void enterContinue(String to) {
		
		//adds a wait to ui so the user has to press enter
		
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
		while (sysName.equalsIgnoreCase("")) { //can't be empty
			System.out.println("Please enter a friendly system name ");
			sysName = nameIn.nextLine();
		}
	}

	public static void userkey() {
		
		//user must enter a key into the system
		Scanner keyIn = new Scanner(System.in);

		String key;
		
		System.out.println("Please enter the system key ");
		key = keyIn.next();
		while (key.equalsIgnoreCase("")) { //forces user to enter at least 1 char could do with ore validation here but keys change 
			System.out.println("Please enter the system key ");
			key = keyIn.next();
		}
		token = apiH.getToken(key); //sends the key to the api handler which then quieries the api and returns the associated authenticated token

	}
	
	public static void updateConfig(){
		
		//this prompts the users if they would like to update the config the config is the static information 

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
			while(!(in.equalsIgnoreCase("n"))){ //while loop forces the user to enter either y or n 
				System.out.println("Configuration is valid, do you want to force a system check? Y/N");
				in = input.next();
			}
			
		}
		input.close();
	}

	public static void starter() throws IOException{
		
		//this is to prevents code repitition  
		enterContinue("");
		nameSystem();
		userkey();
	}

	public static void main(String args[]) throws SigarException, IOException {
		
		//the main hold the main UI to start the monitoring app, there is no serious logic here 
		
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
