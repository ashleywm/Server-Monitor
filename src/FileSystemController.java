import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class FileSystemController {

	public boolean checkDir(String defaultLocation ){
		File theDir = new File(defaultLocation);
		if (theDir.exists()){
			return true;
		}else{
			return false;

		}
	}

	public void makeFile(String defaultLocation){
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(defaultLocation), "utf-8"));
			writer.write("Something");
		} catch (IOException ex) {
		} finally {
			try {writer.close();} catch (Exception ex) {//ignore}
			}
		}
	}

}


