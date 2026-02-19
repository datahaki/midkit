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
  public static void of(Path path, BufferedImage bufferedImage) {
    // TODO check that image type is compatible with path extension
    try (OutputStream outputStream = Files.newOutputStream(path)) {
      ImageIO.write(bufferedImage, PathName.of(path).extension(), outputStream);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
