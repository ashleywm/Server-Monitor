import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.hyperic.sigar.SigarException;

public class Controller{

	 static SavedConfigReader properties = new SavedConfigReader();
	
	public static void Repeater(){ //neds constructor 
		int initialDelay = 10000; // start after 30 seconds
		int period = 5000;        // repeat every 5 seconds
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {

			public void run() {
				task(); //do task 

			}
		};
		timer.scheduleAtFixedRate(task, initialDelay, period);

	}

	public static void task(){
		try {
			SystemInfo.getSysInfo();
		} catch (SigarException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		
		try {
			properties.getSavedConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(properties.getName()); //fetch server name from config
		//Repeater();
		
	}

	
}
