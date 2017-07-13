
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequestUtil {
	public static String doGetRequest(String link) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(link).openConnection();
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
			con.setUseCaches(false);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_BAD_GATEWAY)
				doGetRequest(link);
			else if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
				System.err.println(
						"Update request returned response code: " + responseCode + " " + con.getResponseMessage());
			else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM)
				throw new Exception();

			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				buffer.append(line);

			String page = buffer.toString();

			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
