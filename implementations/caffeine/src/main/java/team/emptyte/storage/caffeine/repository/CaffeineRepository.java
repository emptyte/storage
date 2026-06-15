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
package team.emptyte.storage.caffeine.repository;

import team.emptyte.storage.Identity;
import team.emptyte.storage.repository.Repository;

import java.util.Collection;
import java.util.function.IntFunction;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import org.jetbrains.annotations.NotNull;

import com.github.benmanes.caffeine.cache.Cache;

public class CaffeineRepository<T extends Identity<ID>, ID> implements Repository<T, ID> {
  private final Cache<ID, T> cache;

  private CaffeineRepository(final @NotNull Cache<ID, T> cache) {
    this.cache = cache;
  }

  public static @NotNull <T extends Identity<ID>, ID> CaffeineRepository<T, ID> of(final @NotNull Cache<ID, T> cache) {
    return new CaffeineRepository<>(cache);
  }

  @Override
  public boolean exists(@NonNull final ID id) {
    return this.cache.asMap().containsKey(id);
  }

  @Override
  public @Nullable T findSync(@NonNull final ID id) {
    return this.cache.getIfPresent(id);
  }

  @Override
  public @NonNull <C extends Collection<@NotNull T>> C findAllSync(final @NotNull IntFunction<C> factory) {
    final C collection = factory.apply(0);
    this.cache.asMap().forEach((id, entity) -> collection.add(entity));
    return collection;
  }

  @Override
  public boolean deleteSync(@NonNull final ID id) {
    return this.cache.asMap().remove(id) != null;
  }

  @Override
  public @Nullable T deleteAndRetrieveSync(@NonNull final ID id) {
    return this.cache.asMap().remove(id);
  }

  @Override
  public void deleteAllSync() {
    this.cache.invalidateAll();
  }

  @Override
  public @NonNull T saveSync(@NonNull final T entity) {
    this.cache.put(entity.id(), entity);
    return entity;
  }
}
