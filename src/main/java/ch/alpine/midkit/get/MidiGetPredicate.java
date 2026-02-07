// code by jph
package ch.alpine.midkit.get;

import java.util.Objects;
import java.util.function.Predicate;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public enum MidiGetPredicate implements Predicate<Info> {
  INSTANCE;

  @Override
  public boolean test(Info info) {
    try {
      return hasTransmitter(MidiSystem.getMidiDevice(info));
    } catch (MidiUnavailableException midiUnavailableException) {
      midiUnavailableException.printStackTrace();
    }
    return false;
  }

  private static boolean hasTransmitter(MidiDevice midiDevice) {
    int count = midiDevice.getMaxTransmitters(); // -1 if an unlimited number is available
    return count != 0;
  }

  private Transmitter singletonTransmitter = null;

  public void resetTransmitter() {
    if (singletonTransmitter != null) {
      Receiver receiver = singletonTransmitter.getReceiver();
      if (Objects.nonNull(receiver)) {
        System.out.println("receiver.close()");
        receiver.close();
      }
      singletonTransmitter.close();
      singletonTransmitter = null;
    }
  }
}
