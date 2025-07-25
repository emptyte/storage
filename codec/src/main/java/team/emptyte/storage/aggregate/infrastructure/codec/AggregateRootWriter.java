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
package team.emptyte.storage.aggregate.infrastructure.codec;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface AggregateRootWriter<WriteType> {
  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeThis(final @NotNull String key, final @Nullable WriteType value);

  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeDetailedUuid(final @NotNull String key, final @Nullable UUID uuid);

  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeDetailedUuids(
    final @NotNull String key,
    final @Nullable Collection<@NotNull UUID> uuids
  );

  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeUuid(final @NotNull String field, final @Nullable UUID uuid);

  @Contract("_, _ -> this")
  default @NotNull AggregateRootWriter<WriteType> writeDate(final @NotNull String field, final @Nullable Date date) {
    if (date == null) {
      return this.writeNumber(field, null);
    }
    return this.writeNumber(field, date.getTime());
  }

  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeString(final @NotNull String field, final @Nullable String value);

  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeNumber(final @NotNull String field, final @Nullable Number value);

  @Contract("_, _ -> this")
  @NotNull AggregateRootWriter<WriteType> writeBoolean(final @NotNull String field, final @Nullable Boolean value);

  @Contract("_, _, _ -> this")
  <T> @NotNull AggregateRootWriter<WriteType> writeObject(
    final @NotNull String field,
    final @Nullable T child,
    final @NotNull AggregateRootSerializer<T, WriteType> aggregateRootSerializer
  );

  @Contract("_, _ -> this")
  @NotNull <T> AggregateRootWriter<WriteType> writeRawCollection(
    final @NotNull String field,
    final @Nullable Collection<T> children
  );

  @Contract("_, _, _ -> this")
  @NotNull <T> AggregateRootWriter<WriteType> writeCollection(
    final @NotNull String field,
    final @Nullable Collection<T> children,
    final @NotNull AggregateRootSerializer<T, WriteType> aggregateRootSerializer
  );

  @Contract("_, _, _ -> this")
  default <T> @NotNull AggregateRootWriter<WriteType> writeMap(
    final @NotNull String field,
    final @Nullable Map<?, T> children,
    final @NotNull AggregateRootSerializer<T, WriteType> aggregateRootSerializer
  ) {
    if (children == null) {
      return this.writeCollection(field, null, aggregateRootSerializer);
    }
    return this.writeCollection(field, children.values(), aggregateRootSerializer);
  }

  @NotNull WriteType current();

  @NotNull WriteType end();
}
