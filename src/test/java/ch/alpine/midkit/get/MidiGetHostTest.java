// code by jph
package ch.alpine.midkit.get;

import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

import org.junit.jupiter.api.Test;

import ch.alpine.midkit.MidiDevices;

class MidiGetHostTest {
  @Test
  void test() throws MidiUnavailableException, InterruptedException {
    String string = "eX [hw:2,0,1]";
    string = "LPK25 [hw:3,0,0]";
    // string = "eX [hw:3,0,1]";
    Optional<MidiDevice> optional = MidiDevices.fromName(MidiGetPredicate.INSTANCE, string);
    if (optional.isPresent()) {
      MidiDevice midiDevice = optional.orElseThrow();
      MidiGetHost.INSTANCE.set(midiDevice);
      // ---
      MidiGetHost.INSTANCE.addReceiver(ConsoleReceiver.INSTANCE);
      Thread.sleep(5_000);
      // ---
      MidiGetHost.INSTANCE.set(null);
      System.out.println("reopen");
      MidiGetHost.INSTANCE.set(midiDevice);
      Thread.sleep(5_000);
      MidiGetHost.INSTANCE.set(null);
    }
  }
}
