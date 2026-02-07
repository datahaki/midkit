// code by jph
package ch.alpine.midkit;

import java.nio.file.Path;
import java.util.Objects;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import ch.alpine.midkit.put.MidiPutPredicate;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.ext.HomeDirectory;

enum MidiPlaySequenceDemo {
  ;
  static void main() throws Exception {
    MidiDevice midiDevice = null;
    for (Info info : MidiDevices.getList(MidiPutPredicate.INSTANCE, 100)) {
      if (info.getName().matches(YamahaOnLinux.MIDI_PUT)) {
        midiDevice = MidiSystem.getMidiDevice(info);
        break;
      }
    }
    if (Objects.isNull(midiDevice)) {
      System.err.println("not found.");
      return;
    }
    Receiver receiver = midiDevice.getReceiver();
    if (!midiDevice.isOpen())
      midiDevice.open();
    Sequencer sequencer = MidiSystem.getSequencer(false);
    sequencer.getTransmitter().setReceiver(receiver);
    sequencer.open();
    if (!sequencer.isOpen())
      throw new RuntimeException("sequencer not open");
    Path file = Unprotect.path("/mid/bwv1086.mid");
    file = HomeDirectory.Downloads.path("aw2.mid");
    Sequence sequence = MidiSystem.getSequence(file.toFile());
    sequencer.setSequence(sequence);
    sequencer.start();
    Thread.sleep(500_000);
    sequencer.stop();
    System.out.println("sequencer.stop ok");
    sequencer.close();
    System.out.println("sequencer.close ok"); // THIS IS IMPORTANT!
    receiver.close();
    System.out.println("receiver.close ok");
    midiDevice.close();
    System.out.println("midiDevice.close ok");
  }
}
