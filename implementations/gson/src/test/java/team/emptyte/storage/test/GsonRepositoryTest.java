package team.emptyte.storage.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import team.emptyte.storage.json.repository.GsonRepository;
import team.emptyte.storage.repository.Repository;
import team.emptyte.storage.test.user.User;
import team.emptyte.storage.test.user.codec.UserTypeSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

class GsonRepositoryTest {
  @TempDir
  private Path tempDir;

  private Repository<User, String> repository;

  @BeforeEach
  void setUp() {
    final Executor executor = Runnable::run;

    this.repository = GsonRepository.builder(this.tempDir, UserTypeSerializer.INSTANCE)
      .serializeNulls(false)
      .buildAsync(executor);
  }

  @Test
  @DisplayName("saveSync should create a JSON file with the correct content and return the saved entity")
  void saveSync_ShouldCreateJsonFileAndReturnEntity() throws IOException {
    final User sampleEntity = new User("user-123", "John Doe");
    final User saved = this.repository.saveSync(sampleEntity);

    // Assert that the entity is returned correctly
    assertNotNull(saved, "La entidad debe ser devuelta");
    assertEquals("user-123", saved.id(), "El ID de la entidad no coincide");

    // Check if the file was created correctly in the temp directory
    final Path expectedPath = this.tempDir.resolve("user-123.json");
    assertTrue(Files.exists(expectedPath), "El archivo JSON debería existir en el disco");

    // Read the content of the file and assert it contains the expected JSON structure
    final String content = Files.readString(expectedPath);
    assertTrue(content.contains("{\"id\":\"user-123\",\"name\":\"John Doe\"}"), "El contenido del archivo JSON debe contener la estructura esperada");
  }
}
