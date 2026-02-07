// code by jph
package ch.alpine.midkit;

import java.util.List;

import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
public class InstrumentSelection {
  public VoiceGroup voiceGroup;
  @FieldSelectionCallback("inGroup")
  public MidiInstrument midiInstrument;

  public InstrumentSelection() {
    this(MidiInstrument.GRAND_PIANO);
  }

  public InstrumentSelection(MidiInstrument midiInstrument) {
    this.midiInstrument = midiInstrument;
    this.voiceGroup = VoiceGroup.of(midiInstrument);
  }

  @ReflectionMarker
  public List<MidiInstrument> inGroup() {
    return voiceGroup.list();
  }
}
