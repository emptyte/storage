/*
 * This file is part of storage, licensed under the MIT License
 *
 * Copyright (c) 2025 Emptyte Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.emptyte.storage.json.repository;

import com.google.gson.JsonObject;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.emptyte.storage.Identity;
import team.emptyte.storage.exception.repository.RepositoryException;
import team.emptyte.storage.repository.AsyncRepository;
import team.emptyte.storage.serialization.TypeSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.IntFunction;
import java.util.logging.Logger;

public class GsonRepository<T extends Identity<String>> extends AsyncRepository<T, String> {
  private final static Logger LOGGER = Logger.getLogger(GsonRepository.class.getName());

  private final static String FILE_EXTENSION = ".json";
  private final static String FILE_FORMAT = "%s" + FILE_EXTENSION;

  private final Path folderPath;
  private final TypeSerializer<T, JsonObject> typeSerializer;
  private final boolean serializeNulls;
  private final boolean prettyPrinting;

  GsonRepository(
    final @NotNull Executor executor,
    final @NotNull Path folderPath,
    final @NotNull TypeSerializer<T, JsonObject> typeSerializer,
    final boolean serializeNulls,
    final boolean prettyPrinting
  ) {
    super(executor);

    this.folderPath = folderPath;
    this.typeSerializer = typeSerializer;
    this.serializeNulls = serializeNulls;
    this.prettyPrinting = prettyPrinting;
  }

  public static <T extends Identity<String>> GsonRepositoryBuilder<T> builder(final @NotNull Path folderPath, final @NotNull TypeSerializer<T, JsonObject> typeSerializer) {
    return new GsonRepositoryBuilder<>(folderPath, typeSerializer);
  }

  private @NotNull String fileName(final @NotNull String id) {
    return String.format(FILE_FORMAT, id);
  }

  private @NotNull Path filePath(final @NotNull String id) {
    return this.folderPath.resolve(this.fileName(id));
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
    try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.folderPath, "*" + FILE_EXTENSION)) {
      directoryStream.forEach(path -> {
        final T entity = this.internalFind(path);
        if (entity != null) {
          collection.add(entity);
        }
      });
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
    }
    return entity;
  }

  @Override
  public void deleteAllSync() {
    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(this.folderPath, "*" + FILE_EXTENSION)) {
      for (final Path path : stream) {
        try {
          Files.deleteIfExists(path);
        } catch (final IOException e) {
          LOGGER.warning("Failed to delete file: " + path + ". Skipping. Error: " + e.getMessage());
        }
      }
    } catch (final IOException e) {
      throw new RepositoryException("Failed to delete all entities", e);
    }
  }

  @Override
  public @NotNull T saveSync(@NotNull final T entity) {
    final Path path = this.filePath(entity.id());
    try {
      if (Files.exists(path)) {
        Files.createFile(path);
      }
      final team.emptyte.storage.json.serialization.JsonWriter gsonJsonWriter = new team.emptyte.storage.json.serialization.JsonWriter();
      this.typeSerializer.serialize(entity, gsonJsonWriter);
      final JsonObject jsonObject = gsonJsonWriter.end();
      try (final JsonWriter jsonWriter = new JsonWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
        jsonWriter.setSerializeNulls(this.serializeNulls);
        if (this.prettyPrinting) {
          jsonWriter.setIndent("  ");
        }
        TypeAdapters.JSON_ELEMENT.write(jsonWriter, jsonObject);
        return entity;
      }
    } catch (final IOException e) {
      throw new RepositoryException("Failed to create file: " + path, e);
    }
  }

  private @Nullable T internalFind(final @NotNull String id) {
    return this.internalFind(this.filePath(id));
  }

  private @Nullable T internalFind(final @NotNull Path path) {
    if (!Files.exists(path)) {
      return null;
    }
    try (final JsonReader jsonReader = new JsonReader(Files.newBufferedReader(path))) {
      final JsonObject jsonObject = new JsonObject();
      jsonReader.beginObject();
      while (jsonReader.hasNext()) {
        jsonObject.add(jsonReader.nextName(), TypeAdapters.JSON_ELEMENT.read(jsonReader));
      }
      jsonReader.endObject();
      return this.typeSerializer.deserialize(new team.emptyte.storage.json.serialization.JsonReader(jsonObject));
    } catch (final IOException e) {
      throw new RepositoryException("Failed to deserialize entity from file: " + path, e);
    }
  }
}
