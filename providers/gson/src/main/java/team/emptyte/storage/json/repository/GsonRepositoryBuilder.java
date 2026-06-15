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
