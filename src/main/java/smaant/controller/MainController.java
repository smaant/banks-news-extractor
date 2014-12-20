package smaant.controller;

import com.google.common.base.Joiner;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smaant.model.NewsItem;
import smaant.service.EmailService;
import smaant.service.NewsService;

@Controller
public class MainController {

  @Inject
  private NewsService newsService;

  @Inject
  private EmailService emailService;

  @RequestMapping(value = "/collectNews", method = RequestMethod.POST)
  @ResponseBody
  public void collectNews() {
    List<NewsItem> newNews = newsService.getNewNews("somebank");

    final List<String> titles = newNews.stream().map(NewsItem::getTitle).collect(Collectors.toList());
    final String html = "<html><body>" + Joiner.on("<br>").join(titles) + "</body></html>";

    emailService.sendEmail("email", "Update", html);

    newsService.saveNews(newNews);
  }

}
