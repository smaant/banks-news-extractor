package smaant.service;

import javax.inject.Inject;
import org.springframework.stereotype.Service;
import smaant.dao.UserRepository;
import smaant.exceptions.UserNotFoundException;
import smaant.model.User;

@Service
public class UserService {

  @Inject
  private UserRepository userRepository;

  public void addUser(User user) {
    userRepository.save(user);
  }

  public User getByName(String username) {
    return userRepository.findByName(username);
  }

  public User getOrThrow(String username) {
    final User result = getByName(username);
    if (result == null) {
      throw new UserNotFoundException(username);
    }
    return result;
  }

  public void updateUser(User user) {
    User result = getOrThrow(user.getName());
    user.setId(result.getId());
    userRepository.save(user);
  }

  public void deleteUser(String username) {
    User user = userRepository.findByName(username);
    userRepository.delete(user);
  }
}
