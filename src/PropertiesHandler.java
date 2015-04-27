import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertiesHandler {

	private String loc; 
	private static String token = "";
	
	
	
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
	
	public void storeToken(String location, String file) throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(location+file));

		//if(prop.getProperty("Token") == null){
			//throw new RuntimeException("ERROR: Token not found");
		//}else{
			token = prop.getProperty("Token");
			System.out.println("this " + token);
		//}
	}

	public String getToken() {
		return token;
	}

}




