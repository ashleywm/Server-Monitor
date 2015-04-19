import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigWriter {

	private String loc;
	
	public ConfigWriter(String loc){
		this.loc = loc;
	}
	
	public void write(String name){
	
	//	String propFileName = "D:\\Users\\Ashley Morris\\Documents\\GitHub\\Server-Monitor\\src\\resources\\config.properties";

		Properties prop = new Properties();
		 
        try {
            //set the properties value
            prop.setProperty("name", name);
            //save properties to project root folder
            FileOutputStream fo = new FileOutputStream(loc);
            prop.store(fo, null);
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }

	}
}


