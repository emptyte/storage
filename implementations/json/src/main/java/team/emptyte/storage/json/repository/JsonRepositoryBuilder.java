package team.emptyte.storage.json.repository;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.emptyte.storage.Identity;
import team.emptyte.storage.json.serialization.TypeSerializer;
import team.emptyte.storage.repository.AsyncRepository;
import team.emptyte.storage.repository.builder.RepositoryBuilder;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.json.JsonWriteFeature;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Executor;

public class JsonRepositoryBuilder<T extends Identity<String>> extends RepositoryBuilder<T, String> {
  private final Path folderPath;
  private final TypeSerializer<T> typeSerializer;

  private boolean prettyPrint = false;
  private boolean allowComments = true;
  private boolean escapeNonAscii = false;
  private boolean allowTrailingCommas = true;
  private boolean allowSingleQuotes = false;

  public JsonRepositoryBuilder(final @NotNull Path folderPath, final @NotNull TypeSerializer<T> typeSerializer) {
    this.typeSerializer = typeSerializer;
    this.folderPath = folderPath;
  }

  @Contract("_ -> this")
  public JsonRepositoryBuilder<T> prettyPrint(final boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
    return this;
  }

  @Contract("_ -> this")
  public JsonRepositoryBuilder<T> allowComments(final boolean allowComments) {
    this.allowComments = allowComments;
    return this;
  }

  @Contract("_ -> this")
  public JsonRepositoryBuilder<T> escapeNonAscii(final boolean escapeNonAscii) {
    this.escapeNonAscii = escapeNonAscii;
    return this;
  }

  @Contract("_ -> this")
  public JsonRepositoryBuilder<T> allowTrailingCommas(final boolean allowTrailingCommas) {
    this.allowTrailingCommas = allowTrailingCommas;
    return this;
  }

  @Contract("_ -> this")
  public JsonRepositoryBuilder<T> allowSingleQuotes(final boolean allowSingleQuotes) {
    this.allowSingleQuotes = allowSingleQuotes;
    return this;
  }

  @Override
  public @NotNull AsyncRepository<T, String> buildAsync(final @NotNull Executor executor) {
    Objects.requireNonNull(this.folderPath, "Folder path cannot be null");
    Objects.requireNonNull(this.typeSerializer, "Type serializer cannot be null");
    Objects.requireNonNull(executor, "Executor cannot be null");

    final JsonFactory jsonFactory = JsonFactory.builder()
      .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, this.allowComments)
      .configure(JsonReadFeature.ALLOW_YAML_COMMENTS, this.allowComments)
      .configure(JsonWriteFeature.ESCAPE_NON_ASCII, this.escapeNonAscii)
      .configure(JsonReadFeature.ALLOW_TRAILING_COMMA, this.allowTrailingCommas)
      .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, this.allowSingleQuotes)
      .build();

    return new JsonRepository<>(executor, jsonFactory, this.folderPath, this.typeSerializer, this.prettyPrint);
  }
}
