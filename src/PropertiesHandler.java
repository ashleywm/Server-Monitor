import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.json.JSONObject;


public class PropertiesHandler {

	private static String token = "";
	private static final String DEFAULT_LOCATION = System.getProperty("user.home") + "/Monitoring/"; //where the config should be stored
	private static final String DEFAULT_FILE = "config.properties"; //what it should be named


	public boolean checkDir(){ //make sure the dir exists
		File theDir = new File(DEFAULT_LOCATION);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}

	public boolean checkFile(){ //this checks to see if the config file already exists inside of the dir
		File theDir = new File(DEFAULT_LOCATION + DEFAULT_FILE);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}

	public void makeDir(){ //if dir does not exist or brand new config make a new dir 
		try{
			boolean success = (new File(DEFAULT_LOCATION)).mkdir();
			if (!(success)) {
				System.out.println("Directory could not be created in it's default set location");
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void makeFile() { //after making a new dir make a new file 

		try {
			File file = new File(DEFAULT_LOCATION + DEFAULT_FILE );

			if (!(file.createNewFile())){
				System.out.println("File already exists.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeInitial(String name, String token, String loc){

		Properties prop = new Properties();
		System.out.println(token);

		try {

			prop.setProperty("System_Name", name); //create a property clled system name and save the value name
			prop.setProperty("Token", token); //do the same with token
			
			//token is stored in the the properties file, see docs about this

			FileOutputStream fo = new FileOutputStream(loc); //write
			prop.store(fo, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean checkConfig() throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(DEFAULT_LOCATION+DEFAULT_FILE));

		String token = prop.getProperty("Token"); //check to see if the config file has these values
		String name = prop.getProperty("System_Name");
		name = name.replaceAll("[^a-zA-Z\\s]", ""); 

		if(name != null && token != null){
			return true; //name is configured 
		}else{
			return false;
		}
	}

	public void storeToken() throws IOException { //get the token from the properties file 

		Properties prop = new Properties();

		prop.load(new FileInputStream(DEFAULT_LOCATION + DEFAULT_FILE));

		if(prop.getProperty("Token") == null){
			throw new RuntimeException("ERROR: Token not found");
		}else{
			token = prop.getProperty("Token");
		}
	}

	public void storeDisk(JSONObject disk ) throws IOException {

		FileInputStream in = new FileInputStream(DEFAULT_LOCATION + DEFAULT_FILE); //store all the disks
		Properties props = new Properties();
		props.load(in);
		in.close();

		//just update the file 
		FileOutputStream out = new FileOutputStream(DEFAULT_LOCATION + DEFAULT_FILE);
		props.setProperty("Disk", disk.toString());
		props.store(out, null);
		out.close();
	}

	//returns 
	
	public String getToken(){
		return token;
	}

	public static String getDefaultLocation() {
		return DEFAULT_LOCATION;
	}

	public static String getDefaultFile() {
		return DEFAULT_FILE;
	}

}




