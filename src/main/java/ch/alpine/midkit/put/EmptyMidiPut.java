// code by jph
package ch.alpine.midkit.put;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;

import ch.alpine.tensor.Scalar;

/** VoidMidiOutput ignores all MidiOutput */
public enum EmptyMidiPut implements MidiPut {
  INSTANCE;

  @Override
  public void startSequence(Sequence sequence) {
    // do nothing
  }

  @Override
  public boolean isRunning() {
    return false;
  }

  @Override
  public long getTickPosition() {
    return 0;
  }

  @Override
  public void setTempoInBPM(Scalar scalar) {
    // do nothing
  }

  @Override
  public void stopSequencers() {
    // do nothing
  }

  @Override
  public void sendMessage(MidiMessage midiMessage, long timestamp_us) {
    // do nothing
  }

  @Override
  public void close() throws Exception {
    // do nothing
  }
}
