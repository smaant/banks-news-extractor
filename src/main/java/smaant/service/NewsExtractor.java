package smaant.service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smaant.model.NewsItem;

@Service
public class NewsExtractor {

  @Value("${url.base}")
  private String BASE_URL;

  @Value("${url.bank}")
  private String BANK_URL;

  @Value("${url.news_path}")
  private String NEWS_PATH;

  @Value("${selector.news_item}")
  private String NEWS_ITEM_SELECTOR;

  @Value("${selector.news_date}")
  private String NEWS_DATE_SELECTOR;

  @Value("${selector.news_title}")
  private String NEWS_TITLE_SELECTOR;

  @Value("${selector.news_link}")
  private String NEWS_LINK_SELECTOR;

  @Value("${news_date_time_pattern}")
  private String NEWS_DATE_TIME_PATTERN;

  @Value("${selector.news_article}")
  private String NEWS_ARTICLE_SELECTOR;

  @Value("${selector.article_content}")
  private String NEWS_ARTICLE_CONTENT;

  @Value("${tags_to_remove}")
  private String[] TAGS_TO_REMOVE;

  private DateTimeFormatter dateTimeFormatter;

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
    return new NewsItem(DateTime.parse(date, getDateTimeFormatter()), title, url, null);
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

  public DateTimeFormatter getDateTimeFormatter() {
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");
    }
    return dateTimeFormatter;
  }

  public List<NewsItem> getAllNews(String bankName) {
    final String html = readUrl(BANK_URL + bankName + NEWS_PATH);
    final List<NewsItem> news = extractNewsTitles(html);
    return news.stream().map(x -> {
      String text = extractNewsText(readUrl(BASE_URL + x.getUrl()));
      return new NewsItem(x, text);
    }).collect(Collectors.toList());
  }
}
