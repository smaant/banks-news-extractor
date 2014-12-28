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
import smaant.model.Bank;
import smaant.model.NewsItem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class NewsExtractorTest {

  private static final Bank BANK = new Bank("bankName", "bankId");

  @Inject
  private NewsExtractor newsExtractor;

  @Inject
  private BankiRuService bankiRuService;

  @Test
  public void dayWithOneItemShouldBeExtracted() throws IOException {
    final String testData = readResource("oneItem.html");
    assertThat(newsExtractor.getNewsPreviews(BANK, testData), containsInAnyOrder(
        createItem(BANK, "10.12.2014 17:42", "News title", 7431979)
    ));
  }

  @Test
  public void dayWithTwoItemsShouldBeExtracted() throws IOException {
    final String testData = readResource("twoItems.html");
    assertThat(newsExtractor.getNewsPreviews(BANK, testData), containsInAnyOrder(
        createItem(BANK, "10.12.2014 17:42", "News1 title", 7431979),
        createItem(BANK, "10.12.2014 18:42", "News2 title", 7431980)
    ));
  }

  @Test
  public void wholeNewsTextShouldBeExtracted() throws IOException {
    final String testData = readResource("text.html");
    assertThat(newsExtractor.getNewsText(testData),
        equalTo("<p>First paragraph</p>\n<p>Second paragraph</p>"));
  }

  @Test
  public void uselessInformationShouldBeDeletedFromText() throws IOException {
    final String testData = readResource("textWithNote.html");
    assertThat(newsExtractor.getNewsText(testData),
        equalTo("<p>First paragraph</p>\n<p>Second paragraph</p>"));
  }

  @Test
  public void newsWithTextOutOfTagShouldBeExtracted() throws IOException {
    final String testData = readResource("textOutOfTag.html");
    assertThat(newsExtractor.getNewsText(testData),
        equalTo("<p>First paragraph</p>\n<p>Second paragraph</p>"));
  }

  @Test
  public void newsWithInternalTagsShouldBeExtracted() throws IOException {
    final String testData = readResource("textWithInternalTags.html");
    assertThat(newsExtractor.getNewsText(testData),
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

  private NewsItem createItem(Bank bank, String date, String title, int id) {
    final String url = bankiRuService.getFullUrl(String.format("/news/lenta/?id=%d", id));
    return new NewsItem(bank, DateTime.parse(date, newsExtractor.getDateTimeFormatter()), title, url);
  }
}
