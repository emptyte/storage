package es.revengenetwork.storage.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import es.revengenetwork.storage.codec.ModelCodec;
import es.revengenetwork.storage.codec.ModelReader;
import es.revengenetwork.storage.model.Model;
import es.revengenetwork.storage.repository.AsyncModelRepository;
import es.revengenetwork.storage.repository.builder.ModelRepositoryBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.Executor;
import java.util.function.Function;

@SuppressWarnings("unused")
public class RedisModelRepositoryBuilder
  <ModelType extends Model, Reader extends ModelReader<Reader, JsonObject>>
  extends ModelRepositoryBuilder<ModelType> {

  private Gson gson;
  private String tableName;
  private int expireAfterSave;
  private int expireAfterAccess;
  private JedisPool jedisPool;
  private Function<JsonObject, Reader> readerFactory;
  private ModelCodec.Writer<ModelType, JsonObject> writer;
  private ModelCodec.Reader<ModelType, JsonObject, Reader> reader;

  protected RedisModelRepositoryBuilder() {
  }

  @Contract("_ -> this")
  public @NotNull RedisModelRepositoryBuilder<ModelType, Reader> gson(final @NotNull Gson gson) {
    this.gson = gson;
    return this;
  }

  @Contract("_ -> this")
  public @NotNull RedisModelRepositoryBuilder<ModelType, Reader> tableName(final @NotNull String tableName) {
    this.tableName = tableName;
    return this;
  }

  @Contract("_ -> this")
  public @NotNull RedisModelRepositoryBuilder<ModelType, Reader> expireAfterSave(final int expireAfterSave) {
    this.expireAfterSave = expireAfterSave;
    return this;
  }

  @Contract("_ -> this")
  public @NotNull RedisModelRepositoryBuilder<ModelType, Reader> expireAfterAccess(final int expireAfterAccess) {
    this.expireAfterAccess = expireAfterAccess;
    return this;
  }

  @Contract("_ -> this")
  public @NotNull RedisModelRepositoryBuilder<ModelType, Reader> jedisPool(final @NotNull JedisPool jedisPool) {
    this.jedisPool = jedisPool;
    return this;
  }

  @Contract("_ -> this")
  public RedisModelRepositoryBuilder<ModelType, Reader> modelReader(
    final @NotNull ModelCodec.Reader<ModelType, JsonObject, Reader> reader
  ) {
    this.reader = reader;
    return this;
  }

  @Contract("_ -> this")
  public RedisModelRepositoryBuilder<ModelType, Reader> modelWriter(
    final @NotNull ModelCodec.Writer<ModelType, JsonObject> writer
  ) {
    this.writer = writer;
    return this;
  }

  @Contract("_ -> this")
  public RedisModelRepositoryBuilder<ModelType, Reader> readerFactory(
    final @NotNull Function<JsonObject, Reader> readerFactory
  ) {
    this.readerFactory = readerFactory;
    return this;
  }

  @Contract("_ -> new")
  public @NotNull AsyncModelRepository<ModelType> build(final @NotNull Executor executor) {
    if (expireAfterSave <= 0) {
      expireAfterSave = -1;
    }

    if (expireAfterAccess <= 0) {
      expireAfterAccess = -1;
    }

    return new RedisModelRepository<>(
      executor,
      gson,
      readerFactory,
      writer,
      reader,
      jedisPool,
      tableName,
      expireAfterSave,
      expireAfterAccess);
  }
}
