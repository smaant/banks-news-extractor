package smaant.dao;

import java.util.Collection;
import smaant.model.NewsItem;

public interface UserRepositoryCustom {

  void pushNews(String username, Collection<NewsItem> news);

}
