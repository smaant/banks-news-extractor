package smaant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsExtractor {

  // ToDo: move to external properties
  private static final String BASE_URL = "http://www.banki.ru/banks/bank/";
  private static final String NEWS_PATH = "/news/";

  private static final String DATE_SELECTOR = "div.widget__date";
  private static final String NEWS_SELECTOR = DATE_SELECTOR + "+ul";
  private static final String NEWS_ITEM_SELECTOR = "li";
  private static final String NEWS_TIME_SELECTOR = "span.text-list-date";
  private static final String NEWS_TITLE_SELECTOR = "a.text-list-link";
  private static final String NEWS_LINK_SELECTOR = NEWS_TITLE_SELECTOR;
  private static final String NEWS_DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
  //--------------

  public static final DateTimeFormatter NEWS_DATE_TIME = DateTimeFormatter.ofPattern(NEWS_DATE_TIME_PATTERN);

  private final String bankName;

  public NewsExtractor(String bankName) {
    this.bankName = bankName;
  }

  String getNewsPage() {
    return null;
  }
  
  private NewsItem extractNewsItem(String newsDate, Element newsElement) {
    final String time = newsElement.select(NEWS_TIME_SELECTOR).text();
    final String title = newsElement.select(NEWS_TITLE_SELECTOR).text();
    final String url = newsElement.select(NEWS_LINK_SELECTOR).attr("href");
    return new NewsItem(LocalDateTime.parse(newsDate + " " + time, NEWS_DATE_TIME), title, url, null);
  }

  List<NewsItem> extractNewsTitles(String pageSrc) {
    final Document doc = Jsoup.parse(pageSrc);
    final Elements dates = doc.select(DATE_SELECTOR);
    final Elements news = doc.select(NEWS_SELECTOR);

    final List<NewsItem> result = new ArrayList<>();
    for (int i = 0; i < dates.size(); i++) {
      final String newsDate = dates.get(i).text();
      result.addAll(
          news.get(i).getElementsByTag(NEWS_ITEM_SELECTOR).stream()
              .map(x -> extractNewsItem(newsDate, x)).collect(Collectors.toList())
      );
    }
    return result;
  }

  public List<NewsItem> getAllNews() {
    return null;
  }
}
