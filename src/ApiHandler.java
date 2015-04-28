import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


public class ApiHandler {

	String token;

	public String getToken(String key) {

		JSONObject input = new JSONObject();
		input.put("key", key);

		try {

			URL url = new URL("http://student20265.201415.uk/pmt/api/auth/server/");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output = br.readLine();

			JSONObject obj = new JSONObject(output);
			System.out.println(token = obj.get("token").toString());

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return token;

	}


	public void apiCall(String apiUrl, JSONObject input ) {
		
		PropertiesHandler proph = new PropertiesHandler();
		
		input.put("token", proph.getToken());
		
		System.out.println(input);
		
		try {

			URL url = new URL("http://student20265.201415.uk/pmt/api/update/"+apiUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			conn.disconnect();			

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
