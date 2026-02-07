// code by jph
package ch.alpine.midkit;

import javax.sound.midi.MidiMessage;

/** class exists because the constructor MidiMessage(byte[] data) is protected */
public class MidiMessageAdapter extends MidiMessage {
  public MidiMessageAdapter(byte[] data) {
    super(data);
  }

  @Override // from Cloneable
  public Object clone() {
    throw new UnsupportedOperationException();
  }
}
