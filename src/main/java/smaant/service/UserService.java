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

  public void updateUser(User user) {
    User result = userRepository.findByName(user.getName());
    user.setId(result.getId());
    userRepository.save(user);
  }

  public void deleteUser(String username) {
    User user = userRepository.findByName(username);
    userRepository.delete(user);
  }
}
