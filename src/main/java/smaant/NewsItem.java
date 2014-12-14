package smaant;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.time.LocalDateTime;

public class NewsItem {

  private LocalDateTime date;
  private String title;
  private String url;
  private String text;

  public NewsItem(LocalDateTime date, String title, String url, String text) {
    this.date = date;
    this.title = title;
    this.url = url;
    this.text = text;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("date", date)
        .add("title", title)
        .add("url", url)
        .add("text", text)
        .omitNullValues()
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewsItem that = (NewsItem) o;

    return Objects.equal(this.date, that.date) &&
        Objects.equal(this.title, that.title) &&
        Objects.equal(this.url, that.url) &&
        Objects.equal(this.text, that.text);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, title, url, text);
  }
}
