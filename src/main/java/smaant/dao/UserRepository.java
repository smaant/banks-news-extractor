package smaant.dao;

import java.util.Collection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import smaant.model.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

  @Query(value = "{ $and: [{name: ?0}, {news: {$elemMatch: {hash: {$in: ?1}}}}] }", fields = "{news: {$slice: -55}}")
  User findPreviewsByNameAndHashIn(String username, Collection<Integer> hashes);

  User findByName(String name);

}
