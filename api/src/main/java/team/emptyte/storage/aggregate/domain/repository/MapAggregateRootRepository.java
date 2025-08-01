/*
 * This file is part of storage, licensed under the MIT License
 *
 * Copyright (c) 2023 FenixTeam
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
package team.emptyte.storage.aggregate.domain.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.emptyte.storage.aggregate.domain.AggregateRoot;

/**
 * This is a simple implementation of {@link AggregateRootRepository} that uses a {@link Map} as the storage.
 * It's designed to be used with the {@link WithCacheAggregateRootRepository} class to provide a simple and
 * efficient way to store your data in memory.
 *
 * @param <AggregateType>> The {@link AggregateType} type that this repository will handle.
 * @since 1.0.0
 */
public final class MapAggregateRootRepository<AggregateType extends AggregateRoot> implements AggregateRootRepository<AggregateType> {
  private final Map<String, AggregateType> cache;

  /**
   * Creates a new {@link MapAggregateRootRepository} with the specified {@link Map} as the storage.
   *
   * @param cache The {@link Map} to use as the storage.
   * @since 1.0.0
   */
  private MapAggregateRootRepository(final @NotNull Map<String, AggregateType> cache) {
    this.cache = cache;
  }

  /**
   * Creates a new {@link MapAggregateRootRepository} with a {@link HashMap} as the storage.
   *
   * @param <T> The {@link AggregateRoot} type that this repository will handle.
   * @return A new {@link MapAggregateRootRepository} with a {@link HashMap} as the storage.
   * @since 1.0.0
   */
  @Contract(" -> new")
  public static <T extends AggregateRoot> @NotNull MapAggregateRootRepository<T> withHashMap() {
    return MapAggregateRootRepository.create(new HashMap<>());
  }

  /**
   * Creates a new {@link MapAggregateRootRepository} with a {@link ConcurrentHashMap} as the storage.
   *
   * @param <T> The {@link AggregateRoot} type that this repository will handle.
   * @return A new {@link MapAggregateRootRepository} with a {@link ConcurrentHashMap} as the storage.
   * @since 1.0.0
   */
  @Contract(" -> new")
  public static <T extends AggregateRoot> @NotNull MapAggregateRootRepository<T> withConcurrentHashMap() {
    return MapAggregateRootRepository.create(new ConcurrentHashMap<>());
  }

  /**
   * Creates a new {@link MapAggregateRootRepository} with the specified {@link Map} as the storage.
   *
   * @param cache The {@link Map} to use as the storage.
   * @param <T>   The {@link AggregateRoot} type that this repository will handle.
   * @return A new {@link MapAggregateRootRepository} with the specified {@link Map} as the storage.
   * @since 1.0.0
   */
  @Contract("_ -> new")
  private static <T extends AggregateRoot> @NotNull MapAggregateRootRepository<T> create(final @NotNull Map<String, T> cache) {
    return new MapAggregateRootRepository<>(cache);
  }

  /**
   * Returns the {@link Map} used as the storage.
   *
   * @return The {@link Map} used as the storage.
   * @since 1.0.0
   */
  public @NotNull Map<String, AggregateType> cache() {
    return this.cache;
  }

  @Override
  public boolean deleteSync(final @NotNull String id) {
    return this.cache.remove(id) != null;
  }

  @Override
  public void deleteAllSync() {
    this.cache.clear();
  }

  @Override
  public @Nullable AggregateType deleteAndRetrieveSync(final @NotNull String id) {
    return this.cache.remove(id);
  }

  @Override
  public boolean existsSync(final @NotNull String id) {
    return this.cache.containsKey(id);
  }

  @Override
  public @Nullable AggregateType findSync(final @NotNull String id) {
    return this.cache.get(id);
  }

  @Override
  public <C extends Collection<@NotNull AggregateType>> @Nullable C findAllSync(final @NotNull Consumer<@NotNull AggregateType> postLoadAction, final @NotNull IntFunction<@NotNull C> factory) {
    final Collection<AggregateType> values = this.cache.values();
    if (values.isEmpty()) {
      return null;
    }
    final C collection = factory.apply(values.size());
    for (final AggregateType aggregate : values) {
      postLoadAction.accept(aggregate);
      collection.add(aggregate);
    }
    return collection;
  }

  @Override
  public @NotNull Set<@NotNull String> findIdsSync() {
    return this.cache.keySet();
  }

  @Override
  public @NotNull Iterator<AggregateType> iterator() {
    return this.cache.values().iterator();
  }

  @Override
  public @NotNull Iterator<String> iteratorIdsSync() {
    return this.cache.keySet().iterator();
  }

  @Override
  public @NotNull AggregateType saveSync(@NotNull final AggregateType aggregateType) {
    this.cache.put(aggregateType.id(), aggregateType);
    return aggregateType;
  }
}
