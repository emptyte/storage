package team.emptyte.storage.serialization;

import org.jetbrains.annotations.NotNull;

public interface TypeSerializer<T, O> {
  void serialize(final @NotNull T object, final @NotNull Writer<O> writer);

  @NotNull T deserialize(final @NotNull Reader<O> reader);
}
