package smaant;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import org.apache.commons.io.IOUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Before;
import org.junit.Test;

public class NewsExtractorTest {

  private NewsExtractor newsExtractor;

  public NewsExtractorTest() {
  }

  @Before
  public void setup() {
    newsExtractor = new NewsExtractor(null);
  }

  @Test
  public void dayWithOneItemShouldBeExtracted() throws IOException {
    final String testData = openResource("oneItem.html");
    assertThat(newsExtractor.extractNewsTitles(testData), containsInAnyOrder(
        createItem("10.12.2014 17:42", "News title", 7431979)
    ));
  }

  @Test
  public void dayWithTwoItemsShouldBeExtracted() throws IOException {
    final String testData = openResource("twoItems.html");
    assertThat(newsExtractor.extractNewsTitles(testData), containsInAnyOrder(
        createItem("10.12.2014 17:42", "News1 title", 7431979),
        createItem("10.12.2014 18:42", "News2 title", 7431980)
    ));
  }

  private String openResource(String resourceName) throws IOException {
    final String fullPath = "/pages/" + resourceName;
    final InputStream resource = getClass().getResourceAsStream(fullPath);
    if (resource == null) {
      throw new IOException("There's no resource at " + fullPath);
    }
    return IOUtils.toString(resource);
  }

  private NewsItem createItem(String date, String title, int id) {
    final String url = String.format("/news/lenta/?id=%d", id);
    return new NewsItem(LocalDateTime.parse(date, NewsExtractor.NEWS_DATE_TIME), title, url, null);
  }
}
