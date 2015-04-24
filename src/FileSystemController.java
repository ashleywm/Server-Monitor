import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class FileSystemController {

	public boolean checkDir(String defaultLocation ){
		File theDir = new File(defaultLocation);
		if (!(theDir.exists())){
			return true;
		}else{
			return false;
		}
	}

	public void makeDir(String defaultLocation){
		try{

			boolean success = (new File(defaultLocation)).mkdir();
			if (success) {
				System.out.println("Directory: " 
						+ defaultLocation + " created");
			}else{
				System.out.println("Directory could not be created in it's default set location");
			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void makeFile(String defaultLocation, String filename) {

		try {
		      File file = new File(defaultLocation + filename );
	 
		      if (file.createNewFile()){
		        System.out.println("File is created!");
		      }else{
		        System.out.println("File already exists.");
		      }
	 
	    	} catch (IOException e) {
		      e.printStackTrace();
		}
		
	}

}


