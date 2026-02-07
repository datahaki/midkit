// code by jph
package ch.alpine.midkit.get;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import ch.alpine.bridge.util.CopyOnWriteLinkedSet;

public enum MidiGetHost {
  INSTANCE;

  private MidiDevice midiDevice;

  public void set(MidiDevice midiDevice_next) throws MidiUnavailableException {
    if (Objects.nonNull(midiDevice))
      midiDevice.close();
    map.clear();
    // ---
    midiDevice = midiDevice_next;
    if (Objects.nonNull(midiDevice)) {
      if (!midiDevice.isOpen())
        midiDevice.open();
      for (Receiver receiver : set) {
        try {
          private_add(receiver);
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    }
  }

  private final Set<Receiver> set = new CopyOnWriteLinkedSet<>();
  private final Map<Receiver, Transmitter> map = new HashMap<>();

  public void addReceiver(Receiver receiver) throws MidiUnavailableException {
    set.add(receiver);
    if (Objects.nonNull(midiDevice))
      private_add(receiver);
  }

  private void private_add(Receiver receiver) throws MidiUnavailableException {
    Transmitter transmitter = midiDevice.getTransmitter();
    transmitter.setReceiver(receiver);
    map.put(receiver, transmitter);
  }

  public void removeReceiver(Receiver receiver) {
    set.remove(receiver);
    map.remove(receiver).close();
  }
}
