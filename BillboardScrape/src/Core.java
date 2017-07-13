import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Core {

	public static void main(String[] args) throws InterruptedException {

		boolean run = true;
		String date = "/charts/hot-100/2000-01-01";
		String url = "http://www.billboard.com";

		Map<String, String> songs = new HashMap<String, String>();

		while (run) {
			System.out.println(date);
			try {
				// Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.16.174.11",
				// 8080));
				String page = WebRequestUtil.doGetRequest(url + date);
				Document doc = Jsoup.parse(page);
				for (int i = 1; i < 6; i++) {
					Element num = doc.getElementsByClass("chart-row--" + i).get(0);
					String songName = num.getElementsByClass("chart-row__song").html();
					String singer = num.getElementsByClass("chart-row__artist").html();

					songs.put(songName, singer);
				}

				String next = doc.getElementsByAttributeValue("title", "Next Week").attr("href");
				if (next == null || next.equals(""))
					run = false;
				else
					date = next;
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}

		StringBuilder builder = new StringBuilder();
		for (String song : songs.keySet())
			builder.append(song + "By: " + songs.get(song));

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter("Top5Songs.txt");
			bw = new BufferedWriter(fw);
			bw.write(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
