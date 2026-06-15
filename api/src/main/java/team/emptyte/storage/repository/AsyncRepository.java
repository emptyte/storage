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
