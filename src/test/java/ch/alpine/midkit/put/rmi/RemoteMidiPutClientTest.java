// code by jph
package ch.alpine.midkit.put.rmi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;

import org.junit.jupiter.api.Test;

import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.put.DirectMidiPut;
import ch.alpine.midkit.put.MidiPut;
import ch.alpine.midkit.put.MidiPutPredicate;
import ch.alpine.tensor.qty.Quantity;

class RemoteMidiPutClientTest {
  @Test
  void test1() throws Exception {
    RemoteMidiPutSingleton.INSTANCE.name();
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "Gervill");
    MidiDevice midiDevice = optional.orElseThrow();
    // try (MidiPut midiPut = ) {
    try (RemoteMidiPutServer remoteMidiPutServer = new RemoteMidiPutServer(DirectMidiPut.of(midiDevice))) {
      MidiPut midiPut = RemoteMidiPutClient.createAny().orElseThrow();
      midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80), -1);
      Thread.sleep(200);
      midiPut.sendMessage(new ShortMessage(ShortMessage.NOTE_OFF, 0, 64, 0), -1);
      Thread.sleep(200);
      try (InputStream inputStream = getClass().getResourceAsStream("/mid/bwv1086.mid")) {
        // File file = HomeDirectory.file("");
        Sequence sequence = MidiSystem.getSequence(inputStream);
        midiPut.startSequence(sequence);
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

  @Test
  void test2() throws Exception {
    RemoteMidiPutSingleton.INSTANCE.name();
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "Gervill");
    MidiDevice midiDevice = optional.orElseThrow();
    // try (MidiPut midiPut = ) {
    try (RemoteMidiPutServer remoteMidiPutServer = new RemoteMidiPutServer(DirectMidiPut.of(midiDevice))) {
      RemoteMidiPut remoteMidiPut = RemoteMidiPutClient.findAny().orElseThrow();
      remoteMidiPut.remote_setTempoInBPM(Quantity.of(3, "min^-1"));
    }
  }
}
