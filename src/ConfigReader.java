import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import sun.misc.IOUtils;


public class ConfigReader {

	private String name, loc;
	private boolean configure; 

	public ConfigReader(String loc){
		this.loc = loc;
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

		configure = Boolean.parseBoolean(prop.getProperty("configure"));
		name = prop.getProperty("name");
		
		inputStream.close();

	}


	public String getName() {
		return name;
	}

	public boolean isConfigure() {
		return configure;
	}

}
