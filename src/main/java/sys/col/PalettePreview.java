// code by jph
package sys.col;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.alpine.bridge.awt.ColumnPanel;
import ch.alpine.bridge.col.HueFromColor;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.img.ColorDataLists;

@ReflectionMarker
class PalettePreview implements ManipulateProvider {
  private static final int LENGTH = 16;
  // ---
  public ColorDataLists cdl = ColorDataLists._097;
  @FieldSlider
  @FieldClip(min = "0", max = "1")
  public Scalar s = RealScalar.ONE;
  @FieldSlider
  @FieldClip(min = "0", max = "1")
  public Scalar v = RealScalar.ONE;
  private final List<JLabel> jLabels = new ArrayList<>();
  private final JPanel jPanel;

  public PalettePreview() {
    jPanel = new ColumnPanel();
    for (int count = 0; count < LENGTH; ++count) {
      JLabel jLabel = new JLabel();
      jLabel.setOpaque(true);
      jPanel.add(jLabel);
      jLabels.add(jLabel);
    }
  }

  @Override
  public Container getContainer() {
    HuePalette huePalette = HuePalette.of(cdl.cyclic());
    for (int count = 0; count < LENGTH; ++count) {
      Color color = huePalette.getColor(count, s.number().doubleValue(), v.number().doubleValue(), 1);
      HueFromColor hue = HueFromColor.of(color);
      jLabels.get(count).setText(hue.toFriendlyString() + " " + Math.round(hue.hue() * 360));
      jLabels.get(count).setBackground(color);
    }
    return jPanel;
  }

  static void main() {
    new PalettePreview().runStandalone();
  }
}
