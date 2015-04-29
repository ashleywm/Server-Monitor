import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;


public class PropertiesHandler {

	private String loc; 
	private static String token = "", eth;
	private static String DEFAULT_LOCATION = System.getProperty("user.home") + "/Monitoring/";
	private static String DEFAULT_FILE = "config.properties";


	public boolean checkDir(){
		File theDir = new File(DEFAULT_LOCATION);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}

	public boolean checkFile(){
		File theDir = new File(DEFAULT_LOCATION+DEFAULT_FILE);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}

	public void makeDir(){
		try{
			boolean success = (new File(DEFAULT_LOCATION)).mkdir();
			if (!(success)) {
				System.out.println("Directory could not be created in it's default set location");
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void makeFile() {

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

			prop.setProperty("System_Name", name);
			prop.setProperty("Token", token);

			FileOutputStream fo = new FileOutputStream(loc);
			prop.store(fo, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}


	public boolean checkConfig() throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(DEFAULT_LOCATION+DEFAULT_FILE));

		String name = prop.getProperty("System_Name");
		name = name.replaceAll("[^a-zA-Z\\s]", ""); //buggy 

		if(prop.getProperty("System_Name") != null && name != null ){
			return true; //name is configured 
		}else{
			return false;
		}
	}

	public void storeToken() throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(DEFAULT_LOCATION+DEFAULT_FILE));

		if(prop.getProperty("Token") == null){
			throw new RuntimeException("ERROR: Token not found");
		}else{
			token = prop.getProperty("Token");
			System.out.println("this " + token);
		}
	}

	public void storeDisk(JSONObject disk ) throws IOException {

		Properties prop = new Properties();
		System.out.println(token);

		try {
			prop.setProperty("Disk", disk.toString());

			FileOutputStream fo = new FileOutputStream(DEFAULT_LOCATION+DEFAULT_FILE, true);
			prop.store(fo, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	
	public void getEth() throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(DEFAULT_LOCATION+DEFAULT_FILE));

		if(prop.getProperty("Network") == null){
			throw new RuntimeException("ERROR: Network not found");
		}else{
			eth = prop.getProperty("Network");
		}
	}

	public void storeEth(String eth ) throws IOException {

		Properties prop = new Properties();
		System.out.println(token);

		try {
			prop.setProperty("Network", eth);

			FileOutputStream fo = new FileOutputStream(DEFAULT_LOCATION+DEFAULT_FILE, true);
			prop.store(fo, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	public String getToken() {
		return token;
	}

	public String getNetName() {
		return eth;
	}
}




