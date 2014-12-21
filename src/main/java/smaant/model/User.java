package smaant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.List;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public class User {

  @Id
  @JsonIgnore
  private String id;

  @NotBlank
  private String name;

  @NotBlank @Email
  private String email;

  private List<Bank> banks = Collections.emptyList();

  public User() { }

  public String getName() {
    return name;
  }

  public User(String email, List<Bank> banks) {
    this.email = email;
    this.banks = banks;
  }

  public String getEmail() {
    return email;
  }

  public List<Bank> getBanks() {
    return banks;
  }
}
