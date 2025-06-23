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
package team.emptyte.storage.aggregate.domain;

import org.jetbrains.annotations.NotNull;

/**
 * This class is the base for all the aggregate roots, it contains the essential methods to
 * interact with the database, cache, or whatever you want to use to store your data.
 *
 * @since 0.0.1
 */
public abstract class AggregateRoot {
  private final String id;

  /**
   * Creates a new {@link AggregateRoot} with the specified id.
   *
   * @param id The id of the {@link AggregateRoot}.
   * @since 0.0.1
   */
  public AggregateRoot(final @NotNull String id) {
    this.id = id;
  }

  /**
   * Returns the id of the {@link AggregateRoot}.
   *
   * @return The id of the {@link AggregateRoot}.
   * @since 0.0.1
   */
  public @NotNull String id() {
    return this.id;
  }
}
