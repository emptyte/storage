package team.emptyte.storage.exception;

import org.jetbrains.annotations.NotNull;

public class RepositoryException extends RuntimeException {
  public RepositoryException(final @NotNull String message) {
    super(message);
  }

  public RepositoryException(final @NotNull String message, final @NotNull Throwable cause) {
    super(message, cause);
  }
}
