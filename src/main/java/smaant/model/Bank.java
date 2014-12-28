package smaant.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Bank {

  private String id;
  private String name;

  public Bank() {
  }

  public Bank(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Bank that = (Bank) o;

    return Objects.equal(this.name, that.name) &&
        Objects.equal(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, id);
  }


  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .toString();
  }
}
