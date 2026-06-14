package team.emptyte.storage.caffeine.repository;

import com.github.benmanes.caffeine.cache.Cache;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import team.emptyte.storage.Identity;
import team.emptyte.storage.repository.Repository;

import java.util.Collection;
import java.util.function.IntFunction;

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
