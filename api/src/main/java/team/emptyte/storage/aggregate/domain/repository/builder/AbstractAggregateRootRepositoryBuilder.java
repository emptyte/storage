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
package team.emptyte.storage.aggregate.domain.repository.builder;

import java.util.concurrent.Executor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.emptyte.storage.aggregate.domain.AggregateRoot;
import team.emptyte.storage.aggregate.domain.repository.AggregateRootRepository;
import team.emptyte.storage.aggregate.domain.repository.AsyncAggregateRootRepository;
import team.emptyte.storage.aggregate.domain.repository.WithCacheAggregateRootRepository;

/**
 * This class is the base for all the {@link AsyncAggregateRootRepository} builders. It contains the essential methods to
 * build a {@link AsyncAggregateRootRepository} with a {@link Executor} and optionally a fallback {@link AggregateRootRepository}.
 *
 * @param <AggregateType> The {@link AggregateRoot} type that the repository will handle.
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class AbstractAggregateRootRepositoryBuilder<AggregateType extends AggregateRoot> {
  /**
   * Builds a new {@link AsyncAggregateRootRepository} with the specified {@link Executor}.
   *
   * @param executor The {@link Executor} that will be used to execute the asynchronous operations.
   * @return A new {@link AsyncAggregateRootRepository} with the specified {@link Executor}.
   * @since 1.0.0
   */
  @Contract("_ -> new")
  public abstract @NotNull AsyncAggregateRootRepository<AggregateType> build(final @NotNull Executor executor);

  /**
   * Builds a new {@link WithCacheAggregateRootRepository} with the specified {@link Executor} and fallback {@link AggregateRootRepository}.
   *
   * @param executor        The {@link Executor} that will be used to execute the asynchronous operations.
   * @param cacheRepository The cache {@link AggregateRootRepository} to use.
   * @return A new {@link WithCacheAggregateRootRepository} with the specified {@link Executor} and fallback {@link AggregateRootRepository}.
   * @since 1.0.0
   */
  @Contract("_, _ -> new")
  public @NotNull WithCacheAggregateRootRepository<AggregateType> buildWithFallback(
    final @NotNull Executor executor,
    final @NotNull AggregateRootRepository<AggregateType> cacheRepository
  ) {
    return new WithCacheAggregateRootRepository<>(executor, cacheRepository, this.build(executor));
  }
}
