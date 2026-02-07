// code by jph
package ch.alpine.midkit;

import java.util.List;
import java.util.stream.IntStream;

public enum VoiceGroup {
  PIANO,
  CHROMATIC_PERC,
  ORGAN,
  GUITAR,
  BASS,
  STRINGS,
  ENSEMBLE,
  BRASS,
  REED,
  PIPE,
  SYNTH_LEAD,
  SYNTH_PAD,
  SYNTH_EFFECTS,
  ETHNIC,
  PERCUSSIVE,
  SOUND_EFFECTS;

  private static final int SIZE = 8;

  public List<MidiInstrument> list() {
    int min = ordinal() * SIZE;
    int max = min + SIZE;
    MidiInstrument[] midiInstruments = MidiInstrument.values();
    return IntStream.range(min, max).mapToObj(i -> midiInstruments[i]).toList();
  }

  public boolean isMember(MidiInstrument midiInstrument) {
    return ordinal() == midiInstrument.ordinal() / SIZE;
  }

  public static VoiceGroup of(MidiInstrument midiInstrument) {
    return values()[midiInstrument.ordinal() / SIZE];
  }
}
