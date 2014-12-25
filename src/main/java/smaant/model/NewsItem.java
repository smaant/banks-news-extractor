package smaant.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.joda.time.DateTime;

public class NewsItem {

  private int hash;
  private String bank;
  private DateTime date;
  private String title;
  private String url;

  public NewsItem() { }

  public NewsItem(String bank, DateTime date, String title, String url) {
    this.bank = bank;
    this.date = date;
    this.title = title;
    this.url = url;
    this.hash = Objects.hashCode(bank, date, title, url);
  }

  public String getBank() {
    return bank;
  }

  public DateTime getDate() {
    return date;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public int getHash() {
    return hash;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("bank", bank)
        .add("date", date)
        .add("title", title)
        .add("url", url)
        .omitNullValues()
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewsItem that = (NewsItem) o;

    return Objects.equal(this.bank, that.bank) &&
        Objects.equal(this.date, that.date) &&
        Objects.equal(this.title, that.title) &&
        Objects.equal(this.url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bank, date, title, url);
  }
}
