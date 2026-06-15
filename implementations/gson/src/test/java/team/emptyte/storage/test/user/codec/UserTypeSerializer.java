package team.emptyte.storage.test.user.codec;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import team.emptyte.storage.serialization.Reader;
import team.emptyte.storage.serialization.Writer;
import team.emptyte.storage.test.user.User;

public enum UserTypeSerializer implements team.emptyte.storage.serialization.TypeSerializer<User, JsonObject> {
  INSTANCE;

  @Override
  public @NotNull JsonObject serialize(final @NotNull User object, final @NotNull Writer<JsonObject> writer) {
    return writer
      .writeString("id", object.id())
      .writeString("name", object.name())
      .end();
  }

  @Override
  public @NonNull User deserialize(final @NotNull Reader<JsonObject> reader) {
    final String id = reader.readString("id");
    if (id == null) {
      throw new IllegalArgumentException("id is null");
    }
    final String name = reader.readString("name");
    if (name == null) {
      throw new IllegalArgumentException("name is null");
    }
    return new User(id, name);
  }
}
