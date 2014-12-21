package smaant.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super(String.format("User '%s' not found", username));
  }
}
