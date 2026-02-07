// code by jph
package ch.alpine.midkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.OrderedQ;

class XgMidiInstrumentTest {
  @ParameterizedTest
  @EnumSource
  void test(MidiInstrument midiInstrument) {
    XgMidiInstrument xgMidiInstrument = XgMidiInstrument.valueOf(midiInstrument.name());
    assertEquals(xgMidiInstrument.prg(), midiInstrument.ordinal());
  }

  @Test
  void testIncr() {
    OrderedQ.require(Tensor.of(Stream.of(XgMidiInstrument.values()).map(s -> s.prg()).map(RealScalar::of)));
  }

  @Test
  void testIncr2() {
    OrderedQ.require(Tensor.of(Stream.of(XgMidiInstrument.values()).map(s -> s.prg() * 128 + s.lsb()).map(RealScalar::of)));
  }

  @Test
  void testDiff() {
    Set<Integer> set = Stream.of(XgMidiInstrument.values()).map(s -> s.prg() * 128 + s.lsb()).collect(Collectors.toSet());
    assertEquals(XgMidiInstrument.values().length, set.size());
  }
}
