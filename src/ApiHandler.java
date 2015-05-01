import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


public class ApiHandler {

	String token;

	public String getToken(String key) {
		
		//this method takes the user inputted key and returns the token, this is a separate api cal to the cookie cutter apiCall method
		//below this is for security and because the auth is a different url location

		JSONObject input = new JSONObject();
		input.put("key", key);

		try {

			URL url = new URL("http://student20265.201415.uk/pmt/api/auth/server/"); //where to query 

			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST"); //type of request 
			httpConn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = httpConn.getOutputStream();  //get the output 
			os.write(input.toString().getBytes());
			os.flush();

			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) { //check response for errors
				throw new RuntimeException("Failed : HTTP error code : "
						+ httpConn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpConn.getInputStream())));

			String output = br.readLine();

			JSONObject obj = new JSONObject(output); //returned line is the token 
			token = obj.get("token").toString(); //store it 

			httpConn.disconnect();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return token; //return it 

	}


	public void apiCall(String updateUrl, JSONObject input ) {
		
		//this is a cookieCutter class which allows for all API calls to go through this dependant of info
		//the constructor has a spcial url as to where it posts to and the JSONOBject which contains the data which it is sending to the api
		
		PropertiesHandler proph = new PropertiesHandler();
		
		input.put("token", proph.getToken());
		//System.out.println(input); uncomment to see whats going out
		
		try {

			URL url = new URL("http://student20265.201415.uk/pmt/api/update/" + updateUrl);

			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = httpConn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();

			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ httpConn.getResponseCode());
			}

			httpConn.disconnect();			

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
