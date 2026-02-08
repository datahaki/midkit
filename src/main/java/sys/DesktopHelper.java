// code by jph
package sys;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public enum DesktopHelper {
  ;
  /** @param file intended to open in file navigator/explorer window while selected */
  public static void navigateTo(Path file) {
    try {
      switch (OperatingSystem.TYPE) {
      case LINUX:
        new ProcessBuilder("nautilus", file.toAbsolutePath().toString()).start();
        break;
      case WINDOWS:
        new ProcessBuilder("explorer.exe", "/select,\"" + file + "\"").start();
        break;
      default:
        break;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /** can be used to open folders in explorer (without selecting any specific file in the folder)
   * 
   * @param file */
  public static void open(Path file) {
    try {
      Desktop.getDesktop().open(file.toFile());
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /** @param string is address in browser
   * @throws URISyntaxException
   * @throws IOException */
  public static void browse(String string) throws IOException, URISyntaxException {
    Desktop.getDesktop().browse(new URI(string));
  }
}
