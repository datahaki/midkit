// code by jph
package ch.alpine.midkit.put;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;

import ch.alpine.tensor.Scalar;

public interface MidiPut extends AutoCloseable {
  /** @param sequence
   * @throws Exception */
  void startSequence(Sequence sequence);

  /** @return whether sequencer currently processing given sequence */
  boolean isRunning();

  /** @return */
  long getTickPosition();

  /** @param bpm with unit compatible to "min^-1" */
  void setTempoInBPM(Scalar bpm);

  /**
   * 
   */
  void stopSequencers(); // TODO TPF why plural sequencer_s

  /** @param midiMessage
   * @param ticks */
  void sendMessage(MidiMessage midiMessage, long ticks);
}
