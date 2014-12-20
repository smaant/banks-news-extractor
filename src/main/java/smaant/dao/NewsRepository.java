package smaant.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import smaant.model.NewsItem;

public interface NewsRepository extends MongoRepository<NewsItem, String> {

  @Query(value = "{'hash': {'$in': ?0}}", fields = "{'bank':1, 'date':1, 'title':1, 'url':1}")
  List<NewsItem> findPreviewsByHashIn(Collection<Integer> hashes);

}
