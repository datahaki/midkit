// code by jph
package sys;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

import ch.alpine.tensor.ext.FileBaseName;
import ch.alpine.tensor.ext.FileExtension;

public class Filename implements Comparable<Filename>, Serializable {
  private final Path path;
  private final String extension;
  private final String title;

  public Filename(Path path) {
    this.path = path;
    title = FileBaseName.of(path);
    extension = FileExtension.of(path);
  }

  public Path file() {
    return path;
  }

  public String extension() {
    return extension;
  }

  public String title() {
    return title;
  }

  public boolean hasExtension(String string) {
    return extension.equalsIgnoreCase(string);
  }

  /** @param string non-null
   * @return */
  public Path withExtension(String string) {
    return path.getParent().resolve(title + FileExtension.DOT + Objects.requireNonNull(string));
  }

  public Path asDirectory() {
    return path.getParent().resolve(title);
  }

  @Override
  public int compareTo(Filename filename) {
    return path.compareTo(filename.path);
  }

  @Override
  public int hashCode() {
    return path.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Filename filename //
        && path.equals(filename.path);
  }
}
