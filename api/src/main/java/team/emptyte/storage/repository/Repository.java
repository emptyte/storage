package team.emptyte.storage.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.emptyte.storage.Identity;

import java.util.Collection;
import java.util.function.IntFunction;

public interface Repository<T extends Identity<ID>, ID> {
  boolean exists(final @NotNull ID id);

  @Nullable T findSync(final @NotNull ID id);

  <C extends Collection<@NotNull T>> @NotNull C findAllSync(final @NotNull IntFunction<C> factory);

  boolean deleteSync(final @NotNull ID id);

  @Nullable T deleteAndRetrieveSync(final @NotNull ID id);

  void deleteAllSync();

  @NotNull T saveSync(final @NotNull T entity);
}
