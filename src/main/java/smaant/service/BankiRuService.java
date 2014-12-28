package smaant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smaant.utils.HttpUtils;

@Service
public class BankiRuService {

  @Value("${url.base}")
  private String BASE_URL;

  @Value("${url.bank}")
  private String BANK_URL;

  @Value("${url.news_path}")
  private String NEWS_PATH;

  public String getBankPage(String bankId) {
    return HttpUtils.readUrl(BANK_URL + bankId + NEWS_PATH);
  }

  public String getNewsPage(String newsUrl) {
    return HttpUtils.readUrl(BASE_URL + newsUrl);
  }

  public String getFullUrl(String path) {
    return BASE_URL + path;
  }
}
