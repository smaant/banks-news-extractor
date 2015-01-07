package smaant.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.trimou.engine.MustacheEngineBuilder;
import smaant.model.NewsItem;
import smaant.utils.JodaDateTimeFormatHelper;

@Service
public class TemplateService {

  @Value("${email.template.path}")
  private String templatePath;

  public InputStream getTemplateStream() {
    if (StringUtils.isNotBlank(templatePath)) {
      try {
        return new FileInputStream(templatePath);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    } else {
      return this.getClass().getResourceAsStream("/mail.html");
    }
  }

  public String generateHtml(List<NewsItem> news) {
    final Map<String, Object> data = new HashMap<>();
    data.put("banks",
        news.stream().collect(Collectors.groupingBy(NewsItem::getBank))
            .entrySet().stream().map(x -> new DataItem(x.getKey().getName(), x.getValue())).collect(Collectors.toList()));

    final String template;
    try {
       template = IOUtils.toString(getTemplateStream(), "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return MustacheEngineBuilder
        .newBuilder()
        .registerHelper("jodaDateTimeFormatter", new JodaDateTimeFormatHelper())
        .build()
        .compileMustache("email", template)
        .render(data);
  }

  public static class DataItem {
    public String bankName;
    public List<NewsItem> news;

    public DataItem(String bankName, List<NewsItem> news) {
      this.bankName = bankName;
      this.news = news;
    }

    public String getBankName() {
      return bankName;
    }

    public List<NewsItem> getNews() {
      return news;
    }
  }
}
