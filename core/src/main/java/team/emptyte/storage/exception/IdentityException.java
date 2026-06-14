package team.emptyte.storage.exception;

import org.jetbrains.annotations.NotNull;

public class IdentityException extends RuntimeException {
  public IdentityException(final @NotNull String message) {
    super(message);
  }

  public IdentityException(final @NotNull String message, final @NotNull Throwable cause) {
    super(message, cause);
  }
}
