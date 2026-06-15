package team.emptyte.storage.test.user;

import org.jetbrains.annotations.NotNull;
import team.emptyte.storage.Identity;

public class User extends Identity<String> {
  private final String name;

  public User(final @NotNull String id, final @NotNull String name) {
    super(id);
    this.name = name;
  }

  public @NotNull String name() {
    return this.name;
  }
}
