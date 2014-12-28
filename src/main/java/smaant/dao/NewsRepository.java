package smaant.dao;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import smaant.model.NewsItem;

public interface NewsRepository extends MongoRepository<NewsItem, String> {

  List<NewsItem> findBySeenByAndHashIn(String username, List<String> hashes);

  List<NewsItem> findByHashIn(List<String> hashes);

  List<NewsItem> findBySeenBy(String username);

}
