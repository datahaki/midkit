// code by jph
package sys.gui;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import ch.alpine.tensor.ext.PathName;

public enum ImageExport {
  ;
  public static void of(Path file, BufferedImage bufferedImage) {
    try (OutputStream outputStream = Files.newOutputStream(file)) {
      ImageIO.write(bufferedImage, PathName.of(file).extension(), outputStream);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
