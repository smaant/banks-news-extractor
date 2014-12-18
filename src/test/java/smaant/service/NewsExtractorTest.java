package smaant.service;

import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import smaant.Application;
import smaant.model.NewsItem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class NewsExtractorTest {

  @Inject
  private NewsExtractor newsExtractor;

  @Test
  public void newsItemShouldBeExtracted() throws IOException {
    final String testData = readResource("oneItem.html");
    assertThat(newsExtractor.extractNewsTitles(testData), containsInAnyOrder(
        createItem("10.12.2014", "News title", 12345)
    ));
  }

  @Test
  public void wholeNewsTextShouldBeExtracted() throws IOException {
    final String testData = readResource("text.html");
    assertThat(newsExtractor.extractNewsText(testData), equalTo("<p>First paragraph</p>\n<p>Second paragraph</p>"));
  }

  @Test
  public void uselessInformationShouldBeDeletedFromText() throws IOException {
    final String testData = readResource("textWithNote.html");
    assertThat(newsExtractor.extractNewsText(testData), equalTo("<p>First paragraph</p>\n<p>Second paragraph</p>"));
  }

  @Test
  public void newsWithTextOutOfTagShouldBeExtracted() throws IOException {
    final String testData = readResource("textOutOfTag.html");
    assertThat(newsExtractor.extractNewsText(testData), equalTo("<p>First paragraph</p>\n<p>Second paragraph</p>"));
  }

  @Test
  public void newsWithInternalTagsShouldBeExtracted() throws IOException {
    final String testData = readResource("textWithInternalTags.html");
    assertThat(newsExtractor.extractNewsText(testData),
        equalTo("<p>First paragraph <a>link1</a></p>\n<p>Second paragraph <a>link2</a></p>"));
  }

  private String readResource(String resourceName) throws IOException {
    final String fullPath = "/pages/" + resourceName;
    final InputStream resource = getClass().getResourceAsStream(fullPath);
    if (resource == null) {
      throw new IOException("There's no resource at " + fullPath);
    }
    return IOUtils.toString(resource);
  }

  private NewsItem createItem(String date, String title, int id) {
    final String url = String.format("/news/lenta/?id=%d", id);
    return new NewsItem(DateTime.parse(date, newsExtractor.getDateTimeFormatter()), title, url, null);
  }
}
