package smaant.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import smaant.dao.NewsRepository;
import smaant.model.Bank;
import smaant.model.NewsItem;

@Service
public class NewsService {

  @Inject
  private NewsExtractor newsExtractor;

  @Inject
  private NewsRepository newsRepository;

  @Inject
  private BankiRuService bankiRuService;

  public List<NewsItem> getNewsPreviews(Bank bank) {
    final String bankPage = bankiRuService.getBankPage(bank.getId());
    return newsExtractor.getNewsPreviews(bank, bankPage);
  }

  public String getNewsText(NewsItem news) {
    final String newsPage = bankiRuService.getNewsPage(news.getUrl());
    return newsExtractor.getNewsText(newsPage);
  }

  public List<NewsItem> getNewNews(String username, Bank bank) {
    final List<NewsItem> allPreviews = getNewsPreviews(bank);
    final List<NewsItem> seenByUser = newsRepository.findBySeenByAndHashIn(
        username,
        allPreviews.stream().map(NewsItem::getHash).collect(Collectors.toList())
    );
    return allPreviews.stream().filter(x -> !seenByUser.contains(x)).collect(Collectors.toList());
  }

  public void saveAsSeen(String username, Collection<NewsItem> newsToSave) {
    final List<NewsItem> knownNews = newsRepository.findByHashIn(
        newsToSave.stream().map(NewsItem::getHash).collect(Collectors.toList())
    );

    newsRepository.save(newsToSave.stream().map(item -> {
      final List<String> seenBy = new ArrayList<>();
      final int knownIndex = knownNews.indexOf(item);
      seenBy.add(username);
      if (knownIndex != -1) {
        seenBy.addAll(knownNews.get(knownIndex).getSeenBy());
        return new NewsItem(knownNews.get(knownIndex), seenBy);
      } else {
        return new NewsItem(item, seenBy);
      }
    }).collect(Collectors.toList()));
  }

}
