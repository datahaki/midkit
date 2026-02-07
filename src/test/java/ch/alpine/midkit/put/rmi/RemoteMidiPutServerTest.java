package ch.alpine.midkit.put.rmi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.ShortMessage;

import org.junit.jupiter.api.Test;

import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.put.DirectMidiPut;
import ch.alpine.midkit.put.MidiPutPredicate;
import ch.alpine.tensor.Unprotect;

class RemoteMidiPutServerTest {
  @Test
  void test() throws Exception {
    RemoteMidiPutSingleton.INSTANCE.name();
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "Gervill");
    MidiDevice midiDevice = optional.orElseThrow();
    // try (MidiPut midiPut = ) {
    try (RemoteMidiPutServer remoteMidiPutServer = new RemoteMidiPutServer(DirectMidiPut.of(midiDevice))) {
      ShortMessage shortMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80);
      remoteMidiPutServer.remote_sendMessageBytes(shortMessage.getMessage(), -1);
      Thread.sleep(1000);
    }
  }

  @Test
  void test2() throws Exception {
    RemoteMidiPutSingleton.INSTANCE.name();
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiPutPredicate.INSTANCE, "Gervill");
    MidiDevice midiDevice = optional.orElseThrow();
    // try (MidiPut midiPut = ) {
    try (RemoteMidiPutServer remoteMidiPutServer = new RemoteMidiPutServer(DirectMidiPut.of(midiDevice))) {
      ShortMessage shortMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 64, 80);
      remoteMidiPutServer.remote_sendMessageBytes(shortMessage.getMessage(), -1);
      Thread.sleep(1000);
      Path file = Unprotect.path("/mid/bwv1086.mid");
      byte[] bytes = Files.readAllBytes(file);
      remoteMidiPutServer.remote_startSequenceBytes(bytes);
      assertTrue(remoteMidiPutServer.remote_isRunning());
      Thread.sleep(3_000);
    }
  }
}
