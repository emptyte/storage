package team.emptyte.storage.json.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.emptyte.storage.serialization.TypeSerializer;
import team.emptyte.storage.serialization.Writer;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class JsonWriter implements Writer<JsonObject> {
  private final static JsonObject EMPTY_JSON_OBJECT = new JsonObject();

  private final JsonObject jsonObject;

  public JsonWriter(final @NotNull JsonObject jsonObject) {
    this.jsonObject = jsonObject;
  }

  public JsonWriter() {
    this(EMPTY_JSON_OBJECT);
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeDetailedUuid(
    final @NotNull String key,
    final @Nullable UUID uuid
  ) {
    final var serializedUuid = this.writeDetailedUuid(uuid);
    if (serializedUuid == null) {
      return this;
    }
    this.jsonObject.add(key, serializedUuid);
    return this;
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeDetailedUuids(
    final @NotNull String key,
    final @Nullable Collection<@NotNull UUID> uuids
  ) {
    if (uuids == null) {
      return this;
    }
    final var array = new JsonArray(uuids.size());
    for (final var uuid : uuids) {
      final var serializedUuid = this.writeDetailedUuid(uuid);
      if (serializedUuid == null) {
        continue;
      }
      array.add(serializedUuid);
    }
    this.jsonObject.add(key, array);
    return this;
  }

  public @Nullable JsonObject writeDetailedUuid(final @Nullable UUID uuid) {
    if (uuid == null) {
      return null;
    }
    final var serializedUuid = new JsonObject();
    serializedUuid.addProperty("least", uuid.getLeastSignificantBits());
    serializedUuid.addProperty("most", uuid.getMostSignificantBits());
    return serializedUuid;
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeThis(
    final @NotNull String key,
    final @Nullable JsonObject value
  ) {
    this.jsonObject.add(key, value);
    return this;
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeUuid(final @NotNull String field, final @Nullable UUID uuid) {
    if (uuid == null) {
      return this;
    }
    this.jsonObject.addProperty(field, uuid.toString());
    return this;
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeString(
    final @NotNull String field,
    final @Nullable String value
  ) {
    if (value == null) {
      return this;
    }
    this.jsonObject.addProperty(field, value);
    return this;
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeNumber(
    final @NotNull String field,
    final @Nullable Number value
  ) {
    if (value == null) {
      return this;
    }
    this.jsonObject.addProperty(field, value);
    return this;
  }

  @Override
  @Contract("_, _ -> this")
  public @NotNull JsonWriter writeBoolean(
    final @NotNull String field,
    final @Nullable Boolean value
  ) {
    if (value == null) {
      return this;
    }
    this.jsonObject.addProperty(field, value);
    return this;
  }

  @Override
  @Contract("_, _, _ -> this")
  public <T> @NotNull JsonWriter writeObject(
    final @NotNull String field,
    final @Nullable T child,
    final @NotNull TypeSerializer<T, JsonObject> typeSerializer
  ) {
    if (child == null) {
      return this;
    }
    this.jsonObject.add(field, typeSerializer.serialize(child, new JsonWriter()));
    return this;
  }

  @Override
  @Contract("_, _ -> this")
  public <T> @NotNull JsonWriter writeRawCollection(
    final @NotNull String field,
    final @Nullable Collection<T> children
  ) {
    if (children == null) {
      return this;
    }
    final var array = new JsonArray(children.size());
    for (final var child : children) {
      if (child == null) {
        continue;
      }
      array.add(child.toString());
    }
    this.jsonObject.add(field, array);
    return this;
  }

  @Override
  @Contract("_, _, _ -> this")
  public <T> @NotNull JsonWriter writeCollection(
    final @NotNull String field,
    final @Nullable Collection<T> children,
    final @NotNull TypeSerializer<T, JsonObject> typeSerializer
  ) {
    if (children == null) {
      return this;
    }
    final var array = new JsonArray(children.size());
    for (final var child : children) {
      array.add(typeSerializer.serialize(child, new JsonWriter()));
    }
    this.jsonObject.add(field, array);
    return this;
  }

  @Contract("_, _, _ -> this")
  public <T> @NotNull JsonWriter writePrimitiveCollection(
    final @NotNull String field,
    final @Nullable Collection<T> children,
    final @NotNull Function<T, JsonElement> writer
  ) {
    if (children == null) {
      return this;
    }
    final var array = new JsonArray(children.size());
    for (final var child : children) {
      array.add(writer.apply(child));
    }
    this.jsonObject.add(field, array);
    return this;
  }

  public <T> @NotNull JsonWriter writePrimitiveArray(
    final @NotNull String field,
    final @Nullable T[] children,
    final @NotNull Function<T, JsonElement> writer
  ) {
    if (children == null) {
      return this;
    }
    final var array = new JsonArray(children.length);
    for (final var child : children) {
      array.add(writer.apply(child));
    }
    this.jsonObject.add(field, array);
    return this;
  }

  public <K, V> @NotNull JsonWriter writePrimitiveMap(
    final @NotNull String field,
    final @Nullable Map<K, V> map,
    final @NotNull Function<K, String> keyWriter,
    final @NotNull Function<V, JsonElement> valueWriter
  ) {
    if (map == null) {
      return this;
    }
    final var object = new JsonObject();
    for (final var entry : map.entrySet()) {
      object.add(keyWriter.apply(entry.getKey()), valueWriter.apply(entry.getValue()));
    }
    this.jsonObject.add(field, object);
    return this;
  }

  @Override
  public @NotNull JsonObject current() {
    return this.jsonObject;
  }

  @Override
  public @NotNull JsonObject end() {
    return this.jsonObject;
  }
}
