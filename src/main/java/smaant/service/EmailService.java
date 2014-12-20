package smaant.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailService {

  private final HtmlEmail email;

  public EmailService(String host, String port, String user, String password, String from) {
    email = new HtmlEmail();
    email.setHostName(host);
    email.setAuthentication(user, password);
    email.setSslSmtpPort(port);
    email.setSSLOnConnect(true);

    try {
      email.setFrom(from);
    } catch (EmailException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendEmail(String to, String subject, String body) {
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
