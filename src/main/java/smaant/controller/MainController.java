package smaant.controller;

import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smaant.dao.NewsRepository;
import smaant.service.NewsExtractor;

@Controller
public class MainController {

  @Inject
  private NewsRepository repository;

  @Inject
  private NewsExtractor extractor;

  @RequestMapping(value = "/collectNews", method = RequestMethod.POST)
  @ResponseBody
  public void collectNews() {
  }

}
