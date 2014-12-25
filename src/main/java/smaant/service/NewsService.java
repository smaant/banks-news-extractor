package smaant.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import smaant.dao.UserRepository;
import smaant.model.NewsItem;
import smaant.model.User;

@Service
public class NewsService {

  @Inject
  private NewsExtractor newsExtractor;

  @Inject
  private UserRepository userRepository;

  @Inject
  private BankiRuService bankiRuService;

  public List<NewsItem> getNewsPreviews(String bankName) {
    final String bankPage = bankiRuService.getBankPage(bankName);
    return newsExtractor.getNewsPreviews(bankName, bankPage);
  }

  public String getNewsText(NewsItem news) {
    final String newsPage = bankiRuService.getNewsPage(news.getUrl());
    return newsExtractor.getNewsText(newsPage);
  }

  public List<NewsItem> getNewNews(String username, String bankId) {
    final List<NewsItem> allPreviews = getNewsPreviews(bankId);
    final User user = userRepository.findPreviewsByNameAndHashIn(
        username,
        allPreviews.stream().map(NewsItem::getHash).collect(Collectors.toList())
    );
    if (user == null) {
      return allPreviews;
    } else {
      return allPreviews.stream().filter(x -> !user.getNews().contains(x)).collect(Collectors.toList());
    }
  }

  public void pushNews(String username, Collection<NewsItem> news) {
    userRepository.pushNews(username, news);
  }

}
