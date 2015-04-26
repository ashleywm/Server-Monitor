import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jdk.nashorn.internal.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PropertiesHandler {

	private String name, loc;
	
	public boolean checkDir(String defaultLocation ){
		File theDir = new File(defaultLocation);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}

	public boolean checkFile(String defaultLocation, String filename){
		File theDir = new File(defaultLocation+filename);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}
	
	public void makeDir(String defaultLocation){
		try{
			boolean success = (new File(defaultLocation)).mkdir();
			if (!(success)) {
				System.out.println("Directory could not be created in it's default set location");
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void makeFile(String defaultLocation, String filename) {

		try {
			File file = new File(defaultLocation + filename );

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
	
	public  void getSavedConfig() throws IOException {


		Properties prop = new Properties();
		String propFileName = loc+"config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
				

		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		name = prop.getProperty("System_Name");
		
		inputStream.close();

	}

	
	public boolean checkConfig(String location, String file) throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(location+file));

		String name = prop.getProperty("System_Name");
		name = name.replaceAll("[^a-zA-Z\\s]", ""); //buggy 
		
		if(prop.getProperty("System_Name") != null && name != null ){
			return true; //name is configured 
		}else{
			return false;
		}
	}

}




