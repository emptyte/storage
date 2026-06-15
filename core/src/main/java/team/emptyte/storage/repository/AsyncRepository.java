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
package team.emptyte.storage.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.emptyte.storage.Identity;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.IntFunction;

public abstract class AsyncRepository<T extends Identity<ID>, ID> implements Repository<T, ID> {
  private final Executor executor;

  public AsyncRepository(final @NotNull Executor executor) {
    this.executor = executor;
  }

  protected @NotNull Executor executor() {
    return this.executor;
  }

  public @NotNull CompletableFuture<@NotNull Boolean> existsAsync(final @NotNull ID id) {
    return CompletableFuture.supplyAsync(() -> this.exists(id), this.executor);
  }

  public @NotNull CompletableFuture<@Nullable T> findAsync(final @NotNull ID id) {
    return CompletableFuture.supplyAsync(() -> this.findSync(id), this.executor);
  }

  public <C extends @NotNull Collection<@NotNull T>> @NotNull CompletableFuture<C> findAllAsync(final @NotNull IntFunction<C> factory) {
    return CompletableFuture.supplyAsync(() -> this.findAllSync(factory), this.executor);
  }

  public @NotNull CompletableFuture<@NotNull Boolean> deleteAsync(final @NotNull ID id) {
    return CompletableFuture.supplyAsync(() -> this.deleteSync(id), this.executor);
  }

  public @NotNull CompletableFuture<@Nullable T> deleteAndRetrieveAsync(final @NotNull ID id) {
    return CompletableFuture.supplyAsync(() -> this.deleteAndRetrieveSync(id), this.executor);
  }

  public @NotNull CompletableFuture<@NotNull Void> deleteAllAsync() {
    return CompletableFuture.runAsync(this::deleteAllSync, this.executor);
  }

  public @NotNull CompletableFuture<@NotNull T> saveAsync(final @NotNull T entity) {
    return CompletableFuture.supplyAsync(() -> this.saveSync(entity), this.executor);
  }
}
