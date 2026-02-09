// code by jph
package sys.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.alpine.bridge.io.DeleteDirectory;
import ch.alpine.tensor.ext.PathName;

public enum FileUtils {
  ;
  public static boolean isIdentical(Path source, Path target) throws FileNotFoundException, IOException {
    return Files.mismatch(source, target) == -1;
  }

  public static boolean copyFileIfDifferent(Path source, Path target) throws IOException {
    if (Files.isRegularFile(target) && isIdentical(source, target))
      return false;
    // ---
    Files.copy(source, target);
    return true;
  }

  public static void rigorousTransfer(Path source, Path target, int max_depth, int max_count) throws IOException {
    if (!source.getFileName().equals(target.getFileName()))
      throw new RuntimeException();
    if (!Files.exists(source))
      throw new RuntimeException("source " + source + " does not exist");
    // ---
    DeleteDirectory.of(target, max_depth, max_count);
    copyRecursively(source, target.getParent());
  }

  private static void copyRecursively(Path source, Path target) throws FileNotFoundException, IOException {
    Files.walk(source).forEach(path -> {
      try {
        Path targetPath = target.resolve(source.relativize(path));
        if (Files.isDirectory(path)) {
          Files.createDirectories(targetPath);
        } else {
          Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public static List<Path> listFiles(Path directory, String... strings) throws IOException {
    Set<String> set = Stream.of(strings).map(String::toLowerCase).collect(Collectors.toSet());
    return Files.list(directory) //
        .filter(Files::isRegularFile) //
        .filter(filename -> set.contains(PathName.of(filename).extension().toLowerCase())) //
        .sorted() //
        .toList();
  }
}
