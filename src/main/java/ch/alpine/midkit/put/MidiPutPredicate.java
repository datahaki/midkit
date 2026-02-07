// code by jph
package ch.alpine.midkit.put;

import java.util.function.Predicate;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

/** audio is a singleton */
public enum MidiPutPredicate implements Predicate<Info> {
  INSTANCE;

  @Override
  public boolean test(Info info) {
    try {
      // TODO MIDI eliminate warning
      return hasReceiver(MidiSystem.getMidiDevice(info));
    } catch (MidiUnavailableException midiUnavailableException) {
      midiUnavailableException.printStackTrace();
    }
    return false;
  }

  public static Receiver getReceiver(MidiDevice midiDevice) throws Exception {
    if (hasReceiver(midiDevice)) {
      // System.out.println(DateTime.now() + " check open");
      if (!midiDevice.isOpen()) {
        // System.out.println(DateTime.now() + " open");
        // even for simple devices, open() takes around 1.3[s]
        midiDevice.open();
      }
      // System.out.println(DateTime.now() + " get recv");
      return midiDevice.getReceiver();
    }
    throw new RuntimeException("device has no receiver");
  }

  private static boolean hasReceiver(MidiDevice midiDevice) {
    // System.out.println(DateTime.now() + " max recv");
    int count = midiDevice.getMaxReceivers(); // -1 if an unlimited number is available
    return count != 0;
  }
}
