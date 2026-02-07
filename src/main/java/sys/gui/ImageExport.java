// code by jph
package sys.gui;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import ch.alpine.tensor.ext.FileExtension;

public enum ImageExport {
  ;
  public static void of(Path file, BufferedImage bufferedImage) {
    try {
      ImageIO.write(bufferedImage, FileExtension.of(file), file.toFile());
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
