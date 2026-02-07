// code by jph
package ch.alpine.midkit;

import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
public class DrumKitParam {
  public DrumKit drumKit = DrumKit.COWBELL;
  @FieldClip(min = "0", max = "127")
  @FieldSlider(showValue = true)
  public Integer vel = 100;
}
