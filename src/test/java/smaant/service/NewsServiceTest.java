package smaant.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import smaant.Application;
import smaant.FongoConfiguration;
import smaant.dao.NewsRepository;
import smaant.model.Bank;
import smaant.model.NewsItem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, FongoConfiguration.class})
public class NewsServiceTest {

  @Inject @Spy
  private NewsService newsService;

  @Inject
  private NewsRepository newsRepository;

  @Inject
  private MongoTemplate mongoTemplate;

  private static final Bank BANK1 = new Bank("bank1", "b1");

  private static final NewsItem NEWS1_1 = new NewsItem(BANK1, DateTime.now(), "Title 1", "url1");
  private static final NewsItem NEWS1_2 = new NewsItem(BANK1, DateTime.now(), "Title 2", "url2");

  private static final String USER1 = "user1";
  private static final String USER2 = "user1";

  @Before
  public void beforeEachTest() {
    MockitoAnnotations.initMocks(this);
    mongoTemplate.dropCollection("news");
  }

  @Test
  public void getNewNewsShouldReturnUnseenNewsOnly() {
    when(newsService.getNewsPreviews(BANK1)).thenReturn(Arrays.asList(NEWS1_1, NEWS1_2));
    newsRepository.save(new NewsItem(NEWS1_1, Arrays.asList(USER1)));

    List<NewsItem> newNews = newsService.getNewNews(USER1, BANK1);
    assertThat(newNews, contains(NEWS1_2));
  }

  @Test
  public void getNewNewsShouldReturnAllNewsIfNoSeen() {
    when(newsService.getNewsPreviews(BANK1)).thenReturn(Arrays.asList(NEWS1_1, NEWS1_2));

    List<NewsItem> newNews = newsService.getNewNews(USER1, BANK1);
    assertThat(newNews, containsInAnyOrder(NEWS1_1, NEWS1_2));
  }

  @Test
  public void getNewNewsShouldReturnNothingIfAllSeen() {
    when(newsService.getNewsPreviews(BANK1)).thenReturn(Arrays.asList(NEWS1_1, NEWS1_2));
    newsRepository.save(new NewsItem(NEWS1_1, Arrays.asList(USER1)));
    newsRepository.save(new NewsItem(NEWS1_2, Arrays.asList(USER1)));

    List<NewsItem> newNews = newsService.getNewNews(USER1, BANK1);
    assertThat(newNews, empty());
  }

  @Test
  public void getNewNewsShouldReturnNothingIfNoNewsExtracted() {
    when(newsService.getNewsPreviews(BANK1)).thenReturn(Collections.emptyList());
    newsRepository.save(new NewsItem(NEWS1_1, Arrays.asList(USER1)));

    List<NewsItem> newNews = newsService.getNewNews(USER1, BANK1);
    assertThat(newNews, empty());
  }

  @Test
  public void saveAsSeenShouldSaveNewNews() {
    newsService.saveAsSeen(USER1, Arrays.asList(NEWS1_1, NEWS1_2));

    final List<NewsItem> seen = newsRepository.findBySeenBy(USER1);
    assertThat(seen, containsInAnyOrder(NEWS1_1, NEWS1_2));
  }

  @Test
  public void saveAsSeenShouldNotDuplicateNewsForUsers() {
    newsService.saveAsSeen(USER1, Arrays.asList(NEWS1_1, NEWS1_2));
    newsService.saveAsSeen(USER2, Arrays.asList(NEWS1_1, NEWS1_2));

    final List<NewsItem> all = newsRepository.findAll();
    assertThat(all, hasSize(2));

    final List<NewsItem> seenByUser1 = newsRepository.findBySeenBy(USER1);
    final List<NewsItem> seenByUser2 = newsRepository.findBySeenBy(USER2);
    assertThat(seenByUser1, equalTo(seenByUser2));
  }
}
