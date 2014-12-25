package smaant.controller;

import com.google.common.base.Joiner;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import smaant.exceptions.UsernameAlreadyTakenException;
import smaant.model.Bank;
import smaant.model.NewsItem;
import smaant.model.User;
import smaant.service.EmailService;
import smaant.service.NewsService;
import smaant.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Inject
  private UserService userService;

  @Inject
  private NewsService newsService;

  @Inject
  private EmailService emailService;

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public ResponseEntity<Void> addUser(@RequestBody @Valid User user) {
    if (userService.getByName(user.getName()) != null) {
      throw new UsernameAlreadyTakenException();
    }

    userService.addUser(user);
    final URI location = URI.create("/users/" + user.getName());
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(value = "/{username}/collectNews", method = RequestMethod.POST)
  public ResponseEntity<Void> collectNews(@PathVariable("username") String username) {
    final User user = userService.getOrThrow(username);

    final List<NewsItem> news = new ArrayList<>();
    for (Bank bank : user.getBanks()) {
      news.addAll(newsService.getNewNews(username, bank.getId()));
    }

    final List<String> titles = news.stream().map(NewsItem::getTitle).collect(Collectors.toList());
    final String html = "<html><body>" + Joiner.on("<br>").join(titles) + "</body></html>";

    emailService.sendEmail(user.getEmail(), "Update", html);

    Collections.reverse(news);
    newsService.pushNews(username, news);
    return ResponseEntity.ok().build();
  }

  @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = {"application/json"})
  public User getUser(@PathVariable("username") String username) {
    return userService.getOrThrow(username);
  }

  @RequestMapping(value = "/{username}", method = RequestMethod.PUT, consumes = {"application/json"})
  public ResponseEntity<Void> updateUser(@PathVariable("username") String username, @RequestBody @Valid User user) {
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

