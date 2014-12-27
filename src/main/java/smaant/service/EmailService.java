package smaant.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailService {

  private final String host;
  private final String port;
  private final String username;
  private final String password;
  private final String from;

  public EmailService(String host, String port, String user, String password, String from) {
    this.host = host;
    this.port = port;
    this.username = user;
    this.password = password;
    this.from = from;
  }

  private HtmlEmail getNewEmailClient() {
    final HtmlEmail email = new HtmlEmail();

    email.setHostName(host);
    email.setAuthentication(username, password);
    email.setSslSmtpPort(port);
    email.setSSLOnConnect(true);

    try {
      email.setFrom(from);
    } catch (EmailException e) {
      throw new RuntimeException(e);
    }

    return email;
  }

  public void sendEmail(String to, String subject, String body) {
    final HtmlEmail email = getNewEmailClient();
    try {
      email.addTo(to);
      email.setSubject(subject);
      email.setHtmlMsg(body);
      email.send();
    } catch (EmailException e) {
      e.printStackTrace();
    }
  }

}
