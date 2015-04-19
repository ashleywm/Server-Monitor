
public class FileSystemController {
	
	private ConfigWriter writer;
	private ConfigReader reader;
	private String defaultLocation = System.getenv("SystemDrive") + "\\Monitoring\\";
	
	public FileSystemController( ConfigWriter writer, ConfigReader reader){
		this.writer = writer;
		this.reader = reader;
	}
	
	
	public ConfigWriter getWriter() {
		return writer;
	}

	public ConfigReader getReader() {
		return reader;
	}

	public String getDefaultLocation() {
		return defaultLocation;
	}
	

}
