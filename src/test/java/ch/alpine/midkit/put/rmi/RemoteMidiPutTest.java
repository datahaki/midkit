package ch.alpine.midkit.put.rmi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.ShortMessage;

import org.junit.jupiter.api.Test;

import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.put.DirectMidiPut;
import ch.alpine.midkit.put.MidiPut;
import ch.alpine.midkit.put.MidiPutPredicate;
import ch.alpine.tensor.Unprotect;

class RemoteMidiPutTest {
  @Test
  void testDualUse() throws Exception {
    RemoteMidiPutSingleton.INSTANCE.name();
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "Gervill");
    MidiDevice midiDevice = optional.orElseThrow();
    // try (MidiPut midiPut = ) {
    try (RemoteMidiPutServer remoteMidiPutServer = new RemoteMidiPutServer(DirectMidiPut.of(midiDevice))) {
      MidiPut midiPut1 = RemoteMidiPutClient.createAny().orElseThrow();
      MidiPut midiPut2 = RemoteMidiPutClient.createAny().orElseThrow();
      midiPut1.sendMessage(new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80), -1);
      Thread.sleep(1000);
      midiPut2.sendMessage(new ShortMessage(ShortMessage.NOTE_OFF, 0, 64, 0), -1);
      Thread.sleep(500);
      // ---
      Path file = Unprotect.path("/mid/bwv1086.mid");
      midiPut1.startSequence(MidiSystem.getSequence(file.toFile()));
      assertTrue(midiPut2.isRunning());
      Thread.sleep(2_000);
      long tickPosition = midiPut2.getTickPosition();
      assertTrue(100 < tickPosition);
      midiPut2.stopSequencers();
      assertFalse(midiPut1.isRunning());
    }
  }
}
