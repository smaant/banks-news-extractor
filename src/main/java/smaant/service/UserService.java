package smaant.service;

import javax.inject.Inject;
import org.springframework.stereotype.Service;
import smaant.dao.UserRepository;
import smaant.model.User;

@Service
public class UserService {

  @Inject
  private UserRepository userRepository;

  public void addUser(User user) {
    userRepository.save(user);
  }

  public User getByName(String name) {
    return userRepository.findByName(name);
  }

}
