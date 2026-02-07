package ch.alpine.midkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MidiInstrumentTest {
  @Test
  void test() {
    assertEquals(MidiInstrument.GRAND_PIANO.ordinal() + 1, 1);
    assertEquals(MidiInstrument.CELESTA.ordinal() + 1, 9);
    assertEquals(MidiInstrument.DRAWBAR_ORGAN.ordinal() + 1, 17);
    assertEquals(MidiInstrument.ACOUSTIC_BASS.ordinal() + 1, 33);
    assertEquals(MidiInstrument.VIOLIN.ordinal() + 1, 41);
    assertEquals(MidiInstrument.STRINGS_1.ordinal() + 1, 49);
    assertEquals(MidiInstrument.TRUMPET.ordinal() + 1, 57);
    assertEquals(MidiInstrument.SYNTH_BRASS_1.ordinal() + 1, 63);
    assertEquals(MidiInstrument.SOPRANO_SAX.ordinal() + 1, 65);
    assertEquals(MidiInstrument.PICCOLO.ordinal() + 1, 73);
    assertEquals(MidiInstrument.SQUARE_LEAD.ordinal() + 1, 81);
    assertEquals(MidiInstrument.NEW_AGE_PAD.ordinal() + 1, 89);
    assertEquals(MidiInstrument.RAIN.ordinal() + 1, 97);
    assertEquals(MidiInstrument.SITAR.ordinal() + 1, 105);
    assertEquals(MidiInstrument.TINKLE_BELL.ordinal() + 1, 113);
    assertEquals(MidiInstrument.GUITAR_FRET_NOISE.ordinal() + 1, 121);
    assertEquals(MidiInstrument.GUNSHOT.ordinal() + 1, 128);
  }

  @Test
  void testSpecial() {
    MidiInstrument midiInstrument = MidiInstrument.values()[0x30];
    assertEquals(midiInstrument, MidiInstrument.STRINGS_1);
  }

  @ParameterizedTest
  @EnumSource
  void testNameToString(MidiInstrument midiInstrument) {
    assertEquals(midiInstrument.name(), midiInstrument.toString());
  }
}
