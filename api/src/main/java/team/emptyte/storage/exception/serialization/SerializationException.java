package team.emptyte.storage.exception.serialization;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class SerializationException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public SerializationException(final @NotNull String message) {
    super(message);
  }

  public SerializationException(final @NotNull String message, final @NotNull Throwable cause) {
    super(message, cause);
  }
}
