// code by jph
package ch.alpine.midkit.rec;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import ch.alpine.bridge.lang.FriendlyFormat;
import ch.alpine.midkit.Midi;

public abstract class MidiMessageReceiver implements Receiver, MidiKeyboardReceiver {
  @Override
  public final void send(MidiMessage midiMessage, final long timestamp_us) {
    // typical values of myLong are 11602000, 12370000, ...
    if (timestamp_us % 1000 != 0)
      throw new RuntimeException("better than milliseconds resolution not supported.");
    // ---
    long timestamp_ms = timestamp_us / 1000; // convert from micro to milli seconds. (the LPK25 has milliseconds resolution.)
    // ---
    byte[] message = midiMessage.getMessage();
    switch (message[0]) {
    case (byte) ShortMessage.NOTE_ON:
      noteOn(message[1], message[2], timestamp_ms);
      break;
    case (byte) ShortMessage.NOTE_OFF:
      /* for LPK25 the NOTE_OFF velocity in myByte[2] is always 127! */
      noteOff(message[1], timestamp_ms);
      break;
    case (byte) ShortMessage.CONTROL_CHANGE:
      switch (message[1]) {
      case Midi.SUSTAIN_PEDAL:
        /* Engages the Sustain Resonance, if it is active, and holds
         * the sustain segment of the envelopes of sounding notes. */
        sustain(message[2] != 0, message[2], timestamp_ms);
        break;
      case Midi.SOSTENUTO_PEDAL:
        /* Functions as on a real acoustic grand piano, sustaining only
         * on those notes that were depressed when the pedal went down. */
        break;
      case Midi.SOFT_PEDAL:
        /* Engages the soft pedal samples for that Keyset, if they are active. */
        break;
      }
      break;
    }
    throw new RuntimeException("unexpected midi command: " + FriendlyFormat.of(message));
  }

  @Override
  public final void close() {
    // ---
  }
}
