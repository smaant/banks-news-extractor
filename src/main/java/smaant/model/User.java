package smaant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.List;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
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

  public User(String email, List<Bank> banks) {
    this.email = email;
    this.banks = banks;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public List<Bank> getBanks() {
    return banks;
  }
}
