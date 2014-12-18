package smaant.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import smaant.model.NewsItem;

public interface NewsRepository extends MongoRepository<NewsItem, String> {

}
