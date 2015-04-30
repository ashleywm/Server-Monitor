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

		JSONObject input = new JSONObject();
		input.put("key", key);

		try {

			URL url = new URL("http://student20265.201415.uk/pmt/api/auth/server/");

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

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(httpConn.getInputStream())));

			String output = br.readLine();

			JSONObject obj = new JSONObject(output);
			token = obj.get("token").toString();

			httpConn.disconnect();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return token;

	}


	public void apiCall(String updateUrl, JSONObject input ) {
		
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
