package smaant;

import javax.inject.Inject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import smaant.service.EmailService;

@SpringBootApplication
public class Application {

  @Inject
  Environment env;

  @Bean
  public EmailService emailService() {
    final String host = env.getRequiredProperty("email.host");
    final String port = env.getRequiredProperty("email.ssl_port");
    final String user = env.getRequiredProperty("email.user");
    final String password = env.getRequiredProperty("email.password");
    final String from = env.getRequiredProperty("email.from");
    return new EmailService(host, port, user, password, from);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
