package team.emptyte.storage;

import org.jetbrains.annotations.NotNull;

public abstract class Identity<ID> {
  public ID id;

  public Identity(final @NotNull ID id) {
    this.id = id;
  }

  public ID id() {
    return this.id;
  }
}
