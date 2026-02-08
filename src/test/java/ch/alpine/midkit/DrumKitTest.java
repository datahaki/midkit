// code by jph
package ch.alpine.midkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DrumKitTest {
  @Test
  void testOrdinal() {
    assertEquals(DrumKit.FLOOR_TOP_L.pitch(), 41);
    assertEquals(DrumKit.COWBELL.pitch(), 56);
    assertEquals(DrumKit.BELL_TREE.pitch(), 84);
  }
}
