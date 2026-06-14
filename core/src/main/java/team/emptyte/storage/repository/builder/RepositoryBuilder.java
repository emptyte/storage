package team.emptyte.storage.repository.builder;

import org.jetbrains.annotations.NotNull;
import team.emptyte.storage.Identity;
import team.emptyte.storage.repository.AsyncRepository;

import java.util.concurrent.Executor;

public abstract class RepositoryBuilder<T extends Identity<ID>, ID> {
  public abstract @NotNull AsyncRepository<T, ID> buildAsync(final @NotNull Executor executor);
}
