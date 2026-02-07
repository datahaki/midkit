// code by jph
package ch.alpine.midkit.put;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.ShortMessage;

import org.junit.jupiter.api.Test;

import ch.alpine.midkit.MidiDevices;
import ch.alpine.tensor.Unprotect;

class DirectMidiPutTest {
  @Test
  void testGervill() throws Exception {
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "Gervill");
    MidiDevice midiDevice = optional.orElseThrow();
    try (MidiPut midiPut = DirectMidiPut.of(midiDevice)) {
      midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80), -1);
      Thread.sleep(500);
      midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_OFF, 0, 64, 0), -1);
      Thread.sleep(500);
      Path file = Unprotect.path("/mid/bwv1086.mid");
      midiPut.startSequence(MidiSystem.getSequence(file.toFile()));
      assertTrue(midiPut.isRunning());
      Thread.sleep(3_000);
      long tickPosition = midiPut.getTickPosition();
      assertTrue(100 < tickPosition);
      midiPut.stopSequencers();
      Thread.sleep(1_000);
      midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80), -1);
      Thread.sleep(1_000);
      midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_OFF, 0, 64, 0), -1);
    }
  }

  @Test
  void testEx() throws Exception {
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "eX [hw:3,0,0]");
    if (optional.isPresent()) {
      MidiDevice midiDevice = optional.orElseThrow();
      try (MidiPut midiPut = DirectMidiPut.of(midiDevice)) {
        midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80), -1);
        Thread.sleep(500);
        midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_OFF, 0, 64, 0), -1);
        Thread.sleep(500);
        Path file = Unprotect.path("/mid/bwv1086.mid");
        midiPut.startSequence(MidiSystem.getSequence(file.toFile()));
        assertTrue(midiPut.isRunning());
        Thread.sleep(3_000);
        long tickPosition = midiPut.getTickPosition();
        assertTrue(100 < tickPosition);
        midiPut.stopSequencers();
        Thread.sleep(1_000);
        midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80), -1);
        Thread.sleep(1_000);
        midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_OFF, 0, 64, 0), -1);
      }
    }
  }
}
