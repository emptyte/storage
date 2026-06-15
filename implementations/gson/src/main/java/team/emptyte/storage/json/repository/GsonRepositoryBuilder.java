package team.emptyte.storage.json.repository;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.emptyte.storage.Identity;
import team.emptyte.storage.repository.AsyncRepository;
import team.emptyte.storage.repository.builder.RepositoryBuilder;
import team.emptyte.storage.serialization.TypeSerializer;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Executor;

public class GsonRepositoryBuilder<T extends Identity<String>> extends RepositoryBuilder<T, String> {
  private final Path folderPath;
  private final TypeSerializer<T, JsonObject> typeSerializer;

  private boolean serializeNulls = true;
  private boolean prettyPrinting = false;

  public GsonRepositoryBuilder(final @NotNull Path folderPath, final @NotNull TypeSerializer<T, JsonObject> typeSerializer) {
    this.typeSerializer = typeSerializer;
    this.folderPath = folderPath;
  }

  @Contract("_ -> this")
  public GsonRepositoryBuilder<T> serializeNulls(final boolean serializeNulls) {
    this.serializeNulls = serializeNulls;
    return this;
  }

  @Contract("_ -> this")
  public GsonRepositoryBuilder<T> prettyPrinting(final boolean prettyPrinting) {
    this.prettyPrinting = prettyPrinting;
    return this;
  }

  @Override
  public @NotNull AsyncRepository<T, String> buildAsync(final @NotNull Executor executor) {
    Objects.requireNonNull(this.folderPath, "Folder path cannot be null");
    Objects.requireNonNull(this.typeSerializer, "Type serializer cannot be null");
    Objects.requireNonNull(executor, "Executor cannot be null");

    return new GsonRepository<>(executor, this.folderPath, this.typeSerializer, this.serializeNulls, this.prettyPrinting);
  }
}
