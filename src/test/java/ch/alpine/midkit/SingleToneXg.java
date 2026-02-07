// code by jph
package ch.alpine.midkit;

import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
public class SingleToneXg {
  @FieldClip(min = "0", max = "15")
  public Integer channel = 0;
  public Integer pitch = 64;
  @FieldClip(min = "0", max = "127")
  @FieldSlider(showValue = true)
  public Integer vel = 100;
  public XgMidiInstrument xgMidiInstrument = XgMidiInstrument.GRAND_PIANO;

  public SingleToneXg(int channel) {
    this.channel = channel;
  }

  public SingleToneXg() {
  }
}
