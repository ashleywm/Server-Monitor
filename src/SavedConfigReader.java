import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SavedConfigReader {

	private String name;


	public void getSavedConfig() throws IOException {

		
		Properties prop = new Properties();
		String propFileName = "resources/config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
		name = prop.getProperty("name");
	
	}
	
	public String getName() {
		return name;
	}

}
