import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;


public class ConfigReader {

	private String name, loc;
	
	public  void getSavedConfig() throws IOException {


		Properties prop = new Properties();
		String propFileName = loc+"config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
				

		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		name = prop.getProperty("name");
		
		inputStream.close();

	}

	
	public boolean checkConfig(String location, String file) throws IOException {

		Properties prop = new Properties();

		prop.load(new FileInputStream(location+file));

		if(prop.getProperty("System_Name") != null){
			return true; //name is configured 
		}else{
			return false;
		}
	}


	public String getName() {
		return name;
	}
}
