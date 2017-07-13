import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleCore {

	// private static JsonParser parser = new JsonParser();

	public static void main(String[] args) throws InterruptedException {
		DataBaseManager database = new DataBaseManager();
		database.init();
		List<String> colleges = database.getColleges();
		for (String college : colleges) {
			String page = WebRequestUtil.doGetRequest("https://www.google.com/search?q="
					+ college.replaceAll(" ", "+").replaceAll("&", "%26") + "+colors");
			Document doc = Jsoup.parse(page);
			Elements cards = doc.getElementsByClass("_Mjf");

			List<String> colors = new ArrayList<String>();
			for (Element e : cards) {
				colors.add(e.getElementsByClass("title").html());
			}

			if (colors.size() == 0) {
				System.out.print(".");
				continue;
			}
			System.out.println(".");

			String primarycolor = colors.get(0);
			String secondarycolor = colors.get(0);

			if (colors.size() >= 2)
				secondarycolor = colors.get(1);

			database.attachColorToCollege(college, primarycolor, secondarycolor);

			System.out.print("Set " + college + "'s colors to " + primarycolor + " and " + secondarycolor);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
