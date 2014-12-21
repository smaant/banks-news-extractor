package smaant.controller;

import java.net.URI;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smaant.exceptions.UsernameAlreadyTaken;
import smaant.model.User;
import smaant.service.UserService;

@Controller
@RequestMapping("/users")
public class UsersController {

  @Inject
  private UserService userService;

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  @ResponseBody
  public ResponseEntity<Void> addUser(@RequestBody @Valid User user) {
    if (userService.getByName(user.getName()) != null) {
      throw new UsernameAlreadyTaken();
    }

    userService.addUser(user);
    final URI location = URI.create("/users/" + user.getName());
    return ResponseEntity.created(location).build();
  }
}

