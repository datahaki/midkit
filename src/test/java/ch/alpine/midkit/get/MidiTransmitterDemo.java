// code by jph
package ch.alpine.midkit.get;

import java.util.Objects;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;

import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.YamahaOnLinux;

/** demo shows that a midi device may only be reserved once globally however
 * that any number of transmitters can be created that simultaneously listen
 * to events, and which can be closed individually */
/* package */ enum MidiTransmitterDemo {
  ;
  static void main() throws Exception {
    MidiDevice midiDevice = null;
    for (Info info : MidiDevices.getList(MidiGetPredicate.INSTANCE, 100)) {
      if (info.getName().matches(YamahaOnLinux.MIDI_GET))
        midiDevice = MidiSystem.getMidiDevice(info);
    }
    if (Objects.isNull(midiDevice)) {
      System.err.println("not found.");
      return;
    }
    if (!midiDevice.isOpen()) {
      System.out.println("open");
      midiDevice.open();
    }
    midiDevice.getTransmitters().forEach(System.out::println);
    for (int c = 0; c < 3; ++c) {
      Transmitter transmitter = midiDevice.getTransmitter(); // returns new instance everytime
      transmitter.setReceiver(ConsoleReceiver.INSTANCE);
      midiDevice.getTransmitters().forEach(System.out::println);
      System.out.println("---");
    }
    Thread.sleep(3_000);
    for (Transmitter transmitter : midiDevice.getTransmitters()) {
      transmitter.close();
      break;
    }
    midiDevice.getTransmitters().forEach(System.out::println);
    Thread.sleep(3_000);
    midiDevice.getTransmitters().forEach(System.out::println);
    System.out.println("close");
    midiDevice.close();
    midiDevice.getTransmitters().forEach(System.out::println);
    for (Transmitter transmitter : midiDevice.getTransmitters()) {
      transmitter.close();
    }
    // midiDevice.getTransmitter().setReceiver(ConsoleReceiver.INSTANCE);
  }
}
