package smaant;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsExtractor {

  // ToDo: move to external properties
  private static final String BASE_URL = "http://www.banki.ru";
  private static final String BANK_URL = BASE_URL + "/banks/bank/";
  private static final String NEWS_PATH = "/news/";

  private static final String NEWS_ITEM_SELECTOR = "li.b-nw-list__item";
  private static final String NEWS_DATE_SELECTOR = "span.date";
  private static final String NEWS_TITLE_SELECTOR = "a";
  private static final String NEWS_LINK_SELECTOR = NEWS_TITLE_SELECTOR;

  private static final String NEWS_DATE_TIME_PATTERN = "dd.MM.yyyy";

  private static final String NEWS_ARTICLE_SELECTOR = "article.lenta";
  private static final String NEWS_ARTICLE_CONTENT = NEWS_ARTICLE_SELECTOR + " > *";
  private static final String[] TAGS_TO_REMOVE = {"div"};
  //--------------

  public static final DateTimeFormatter NEWS_DATE_TIME = DateTimeFormat.forPattern(NEWS_DATE_TIME_PATTERN);

  private final String bankName;

  public NewsExtractor(String bankName) {
    this.bankName = bankName;
  }

  private String readUrl(String url) {
    final HttpClient httpClient = HttpClientBuilder.create().build();
    final HttpGet httpGet = new HttpGet(url);
    final String html;
    try {
      final HttpResponse response = httpClient.execute(httpGet);
      html = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return html;
  }
  
  private NewsItem extractNewsItem(Element newsElement) {
    final String date = newsElement.select(NEWS_DATE_SELECTOR).text();
    final String title = newsElement.select(NEWS_TITLE_SELECTOR).text();
    final String url = newsElement.select(NEWS_LINK_SELECTOR).attr("href");
    return new NewsItem(DateTime.parse(date, NEWS_DATE_TIME), title, url, null);
  }

  List<NewsItem> extractNewsTitles(String pageSrc) {
    final Document doc = Jsoup.parse(pageSrc);
    final Elements news = doc.select(NEWS_ITEM_SELECTOR);

    return news.stream()
        .map(this::extractNewsItem).collect(Collectors.toList());
  }

  String extractNewsText(String pageSrc) {
    final Document doc = Jsoup.parse(pageSrc);
    Arrays.stream(TAGS_TO_REMOVE).forEach(tag -> doc.select(NEWS_ARTICLE_SELECTOR + " " + tag).remove());
    return doc.select(NEWS_ARTICLE_CONTENT).toString();
  }

  public List<NewsItem> getAllNews() {
    final String html = readUrl(BANK_URL + bankName + NEWS_PATH);
    final List<NewsItem> news = extractNewsTitles(html);
    return news.stream().map(x -> {
      String text = extractNewsText(readUrl(BASE_URL + x.getUrl()));
      return new NewsItem(x, text);
    }).collect(Collectors.toList());
  }
}
