// code by jph
package ch.alpine.midkit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VoiceGroupTest {
  @Test
  void test() {
    assertTrue(VoiceGroup.PIANO.isMember(MidiInstrument.CLAVI));
    assertTrue(VoiceGroup.PIANO.isMember(MidiInstrument.HAPSICHORD));
    assertFalse(VoiceGroup.PIANO.isMember(MidiInstrument.VIOLA));
  }
}
