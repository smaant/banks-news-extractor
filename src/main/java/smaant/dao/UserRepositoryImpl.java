package smaant.dao;

import java.util.Collection;
import javax.inject.Inject;
import org.apache.tomcat.jni.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.data.mongodb.core.query.Update;
import smaant.model.NewsItem;

public class UserRepositoryImpl implements UserRepositoryCustom {

  @Inject
  private MongoTemplate mongoTemplate;

  @Override
  public void pushNews(String username, Collection<NewsItem> news) {
    final Query query = query(where("name").is(username));
    final Update update = new Update().push("news").each(news.toArray());
    mongoTemplate.findAndModify(query, update, User.class);
  }
}
