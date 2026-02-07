package sys.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ch.alpine.tensor.Unprotect;

class FileUtilsTest {
  @TempDir
  Path tempDir;

  @Test
  void testSome() throws Exception {
    Path src = Unprotect.path("/mid/bwv1086.mid");
    Path dst = tempDir.resolve("target.mid");
    Files.copy(src, dst);
    assertTrue(Files.exists(dst));
  }
}
