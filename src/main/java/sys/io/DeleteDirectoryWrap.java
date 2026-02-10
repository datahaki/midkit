// code by jph
package sys.io;

import java.nio.file.Path;

import ch.alpine.bridge.io.DeleteDirectory;

public enum DeleteDirectoryWrap {
  ;
  public static void of(Path directory, int max_depth, long max_count) throws Exception {
    DeleteDirectory deleteDirectory = DeleteDirectory.of(directory, max_depth, max_count);
    long deletedCount = deleteDirectory.fileCount();
    if (0 < deletedCount)
      System.out.println("deleted: " + deletedCount);
  }
}
