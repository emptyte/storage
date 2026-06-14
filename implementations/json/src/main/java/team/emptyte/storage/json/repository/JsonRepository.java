package team.emptyte.storage.json.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.emptyte.storage.Identity;
import team.emptyte.storage.exception.RepositoryException;
import team.emptyte.storage.exception.SerializationException;
import team.emptyte.storage.json.jackson.wrapper.PrettyPrintWriteContext;
import team.emptyte.storage.json.serialization.TypeSerializer;
import team.emptyte.storage.repository.AsyncRepository;
import tools.jackson.core.*;
import tools.jackson.core.json.JsonFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.IntFunction;
import java.util.logging.Logger;

public class JsonRepository<T extends Identity<String>> extends AsyncRepository<T, String> {
  private final static String FILE_EXTENSION = ".json";
  private final static String FILE_FORMAT = "%s" + FILE_EXTENSION;

  private final static Logger LOGGER = Logger.getLogger(JsonRepository.class.getName());

  private final Path folderPath;
  private final JsonFactory jsonFactory;
  private final TypeSerializer<T> typeSerializer;
  private final ObjectWriteContext objectWriteContext;

  JsonRepository(
    final @NotNull Executor executor,
    final @NotNull JsonFactory jsonFactory,
    final @NotNull Path folderPath,
    final @NotNull TypeSerializer<T> typeSerializer,
    final boolean prettyPrint
  ) {
    super(executor);

    this.folderPath = folderPath;
    this.jsonFactory = jsonFactory;
    this.typeSerializer = typeSerializer;
    this.objectWriteContext = new PrettyPrintWriteContext(prettyPrint);
  }

  private @NotNull String fileName(final @NotNull String id) {
    return String.format(FILE_FORMAT, id);
  }

  private @NotNull Path filePath(final @NotNull String id) {
    return this.folderPath.resolve(this.fileName(id));
  }

  private @NotNull T internalFind(final @NotNull Path filePath) {
    try (final JsonParser jsonParser = this.jsonFactory.createParser(ObjectReadContext.empty(), filePath)) {
      return this.typeSerializer.deserialize(jsonParser);
    } catch (final Exception e) {
      throw new SerializationException("Failed to deserialize model", e);
    }
  }

  private @Nullable T internalFind(final @NotNull String id) {
    final Path filePath = this.filePath(id);
    if (!Files.exists(filePath)) {
      return null;
    }
    return this.internalFind(filePath);
  }

  @Override
  public boolean exists(final @NotNull String id) {
    return Files.exists(this.filePath(id));
  }

  @Override
  public @Nullable T findSync(final @NotNull String id) {
    return this.internalFind(id);
  }

  @Override
  public @NotNull <C extends Collection<@NotNull T>> C findAllSync(final @NotNull IntFunction<C> factory) {
    final C collection = factory.apply(0);
    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(this.folderPath, "*" + FILE_EXTENSION)) {
      for (final Path path : stream) {
        try {
          collection.add(this.internalFind(path));
        } catch (final Exception e) {
          LOGGER.warning("Failed to deserialize entity from file: " + path + ". Skipping. Error: " + e.getMessage());
        }
      }
    } catch (final IOException e) {
      throw new RepositoryException("Failed to find all entities", e);
    }
    return collection;
  }

  @Override
  public boolean deleteSync(final @NotNull String id) {
    try {
      return Files.deleteIfExists(this.filePath(id));
    } catch (final IOException e) {
      throw new RepositoryException("Failed to delete entity with id: " + id, e);
    }
  }

  @Override
  public @Nullable T deleteAndRetrieveSync(final @NotNull String id) {
    final T entity = this.internalFind(id);
    if (entity != null) {
      this.deleteSync(id);
      return entity;
    }
    return null;
  }

  @Override
  public void deleteAllSync() {
    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(this.folderPath, "*" + FILE_EXTENSION)) {
      for (final Path path : stream) {
        Files.deleteIfExists(path);
      }
    } catch (final IOException e) {
      throw new RepositoryException("Failed to delete all entities", e);
    }
  }

  @Override
  public @NotNull T saveSync(@NotNull final T entity) {
    final Path filePath = this.filePath(entity.id());
    try (final JsonGenerator jsonGenerator = this.jsonFactory.createGenerator(this.objectWriteContext, filePath, JsonEncoding.UTF8)) {
      this.typeSerializer.serialize(entity, jsonGenerator);
    } catch (final Exception e) {
      throw new SerializationException("Failed to serialize entity with id: " + entity.id(), e);
    }
    return entity;
  }
}
