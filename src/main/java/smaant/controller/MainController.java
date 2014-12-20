package smaant.controller;

import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smaant.model.NewsItem;
import smaant.service.NewsService;

@Controller
public class MainController {

  @Inject
  private NewsService newsService;

  @RequestMapping(value = "/collectNews", method = RequestMethod.POST)
  @ResponseBody
  public void collectNews() {
    List<NewsItem> newNews = newsService.getNewNews("somebank");
    newNews.forEach(x -> System.out.println(x.getTitle()));
    newsService.saveNews(newNews);
  }

}
