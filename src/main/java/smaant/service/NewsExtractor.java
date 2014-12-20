package smaant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

  @Value("${selector.news_date}")
  private String NEWS_DATE_SELECTOR;

  @Value("${selector.news_group}")
  private String NEWS_GROUP_SELECTOR;

  @Value("${selector.news_item}")
  private String NEWS_ITEM_SELECTOR;

  @Value("${selector.news_time}")
  private String NEWS_TIME_SELECTOR;

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

  public DateTimeFormatter getDateTimeFormatter() {
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormat.forPattern(NEWS_DATE_TIME_PATTERN);
    }
    return dateTimeFormatter;
  }

  public List<NewsItem> getNewsPreviews(String bankName, String pageSrc) {
    final Document doc = Jsoup.parse(pageSrc);
    final Elements dates = doc.select(NEWS_DATE_SELECTOR);
    final Elements news = doc.select(NEWS_GROUP_SELECTOR);

    final List<NewsItem> result = new ArrayList<>();
    for (int i = 0; i < dates.size(); i++) {
      final String newsDate = dates.get(i).text();
      result.addAll(
          news.get(i).getElementsByTag(NEWS_ITEM_SELECTOR).stream()
              .map(x -> extractNewsItem(bankName, newsDate, x)).collect(Collectors.toList())
      );
    }
    return result;
  }

  public String getNewsText(String pageSrc) {
    final Document doc = Jsoup.parse(pageSrc);
    Arrays.stream(TAGS_TO_REMOVE).forEach(tag -> doc.select(NEWS_ARTICLE_SELECTOR + " " + tag).remove());
    return doc.select(NEWS_ARTICLE_CONTENT).toString();
  }

  private NewsItem extractNewsItem(String bankName, String newsDate, Element newsElement) {
    final String time = newsElement.select(NEWS_TIME_SELECTOR).text();
    final String title = newsElement.select(NEWS_TITLE_SELECTOR).text();
    final String url = newsElement.select(NEWS_LINK_SELECTOR).attr("href");
    return new NewsItem(bankName, DateTime.parse(newsDate + " " + time, getDateTimeFormatter()), title, url, null);
  }
}
