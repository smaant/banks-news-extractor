package smaant.controller;

import java.net.URI;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import smaant.exceptions.UserNotFoundException;
import smaant.exceptions.UsernameAlreadyTakenException;
import smaant.model.User;
import smaant.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Inject
  private UserService userService;

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public ResponseEntity<Void> addUser(@RequestBody @Valid User user) {
    if (userService.getByName(user.getName()) != null) {
      throw new UsernameAlreadyTakenException();
    }

    userService.addUser(user);
    final URI location = URI.create("/users/" + user.getName());
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = {"application/json"})
  public User getUser(@PathVariable("username") String username) {
    final User user = userService.getByName(username);
    if (user == null){
      throw new UserNotFoundException(username);
    }
    return user;
  }

  @RequestMapping(value = "/{username}", method = RequestMethod.PUT, consumes = {"application/json"})
  public ResponseEntity<Void> updateUser(@PathVariable("username") String username, @RequestBody @Valid User user) {
    if (userService.getByName(username) == null) {
      throw new UserNotFoundException(username);
    }

    if (!username.equals(user.getName())) {
      throw new SecurityException();
    }

    userService.updateUser(user);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
    userService.deleteUser(username);
    return ResponseEntity.ok().build();
  }
}

