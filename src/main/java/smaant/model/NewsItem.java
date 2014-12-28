package smaant.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.Collections;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "news")
public class NewsItem {

  @Id
  private String id;

  @Indexed
  private String hash;

  @Indexed
  @Field("seen_by")
  private List<String> seenBy;

  private Bank bank;
  private DateTime date;
  private String title;
  private String url;

  public NewsItem() {
    seenBy = Collections.emptyList();
  }

  public NewsItem(Bank bank, DateTime date, String title, String url) {
    this();
    this.bank = bank;
    this.date = date;
    this.title = title;
    this.url = url;
    this.hash = DigestUtils.md5Hex(bank.getId() + date.toString() + title + url);
  }

  public NewsItem(NewsItem src, List<String> seenBy) {
    this(src.getBank(), src.getDate(), src.getTitle(), src.getUrl());
    this.id = src.id;
    this.seenBy = seenBy;
  }

  public Bank getBank() {
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

  public String getHash() {
    return hash;
  }

  public List<String> getSeenBy() {
    return seenBy;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("bank", bank)
        .add("date", date)
        .add("title", title)
        .add("url", url)
        .add("hash", hash)
        .add("seen_by", seenBy)
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
