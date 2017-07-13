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
		Map<String, String> logos = new HashMap<String, String>();
		for (int letter = 'a'; letter <= 'z'; letter++) {
			System.out.println("Getting all " + ((char) letter) + "'s...");
			String page = WebRequestUtil.doGetRequest("http://www.ncaa.com/schools/" + ((char) letter));
			Document doc = Jsoup.parse(page);
			Element schoolsList = doc.getElementById("school-list");
			for (Element school : schoolsList.getElementsByTag("a")) {
				String link = "http://www.ncaa.com" + school.attr("href");
				page = WebRequestUtil.doGetRequest(link);
				Document schoolDoc = Jsoup.parse(page);

				Element logo = schoolDoc.getElementsByClass("school-logo").get(0);

				logos.put(school.getElementsByTag("span").html(), logo.getElementsByTag("img").attr("src"));
			}
		}

		StringBuilder builder = new StringBuilder();
		for (String logo : logos.keySet())
			builder.append(logo + ": " + logos.get(logo) + "\n");

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter("School_Logo.txt");
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
