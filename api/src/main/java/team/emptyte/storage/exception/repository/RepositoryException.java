package team.emptyte.storage.exception.repository;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class RepositoryException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public RepositoryException(final @NotNull String message) {
    super(message);
  }

  public RepositoryException(final @NotNull String message, final @NotNull Throwable cause) {
    super(message, cause);
  }
}
