package smaant.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import smaant.model.User;

public interface UserRepository extends MongoRepository<User, String> {

  User findByName(String name);

}
