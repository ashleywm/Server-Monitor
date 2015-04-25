import java.io.File;
import java.io.IOException;

public class FileSystemController {

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
}




