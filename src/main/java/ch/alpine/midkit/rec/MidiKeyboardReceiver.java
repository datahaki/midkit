// code by jph
package ch.alpine.midkit.rec;

public interface MidiKeyboardReceiver {
  /** @param key
   * @param velocity
   * @param timestamp_ms of event in milli seconds after opening device. */
  void noteOn(int key, int velocity, long timestamp_ms);

  /** @param key
   * @param myLong timestamp of event in milli seconds after opening device. */
  void noteOff(int key, long timestamp_ms);

  /** @param sustain 0 for lifted state, != 0 for pressed state
   * @param myLong timestamp of event in milli seconds after opening device. */
  void sustain(boolean sustain, int myInt, long timestamp_ms);
}
