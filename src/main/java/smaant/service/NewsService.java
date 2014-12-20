package smaant.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import smaant.dao.NewsRepository;
import smaant.model.NewsItem;

@Service
public class NewsService {

  @Inject
  private NewsRepository repository;

  @Inject
  private NewsExtractor newsExtractor;

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

  public List<NewsItem> getNewNews(String bankName) {
    final List<NewsItem> allPreviews = getNewsPreviews(bankName);
    final List<NewsItem> knownPreviews = repository.findPreviewsByHashIn(
        allPreviews.stream().map(NewsItem::getHash).collect(Collectors.toList())
    );
    return filterKnown(allPreviews, knownPreviews).stream()
        .map(x -> new NewsItem(x, getNewsText(x))).collect(Collectors.toList());
  }

  private List<NewsItem> filterKnown(List<NewsItem> all, List<NewsItem> known) {
    return all.stream().filter(x -> !known.contains(x)).collect(Collectors.toList());
  }

  public void saveNews(Collection<NewsItem> news) {
    repository.save(news);
  }

}
