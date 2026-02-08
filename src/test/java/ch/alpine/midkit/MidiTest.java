// code by jph
package ch.alpine.midkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MidiTest {
  @Test
  void test() {
    assertEquals(Midi.CC_LSB, 32);
  }
}
