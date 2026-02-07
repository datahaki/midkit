// code by jph
package ch.alpine.midkit.rec;

import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/** receives midi messages for instance from the LPK25 keyboard.
 * the range of note pitch values is 0 ... 120.
 * audible/valid piano keys are 21 to 108 (88 keys),
 * or 12 to 108 (97 keys). */
public abstract class SustainableReceiver extends MidiMessageReceiver {
  final int offset;
  final int[] velocity = new int[121]; // LPK25
  boolean isSustain = false;
  final Set<Integer> sustain = new HashSet<>();
  final Set<Integer> downers = new HashSet<>();

  /** @param offset middle C corresponds to pitch 60-offset */
  public SustainableReceiver(int offset) {
    this.offset = offset;
  }

  @Override
  public void noteOn(int key, int velocity, long timestamp_ms) {
    this.velocity[key] = velocity;
    key = key2pitch(key);
    sustain.add(key);
    downers.add(key);
    keyDown(key, velocity, timestamp_ms);
  }

  @Override
  public void noteOff(int key, long timestamp_ms) {
    // for LPK25 the NOTE_OFF velocity in myByte[2] is always 127!
    key = key2pitch(key);
    if (!isSustain) {
      velocity[key + offset] = 0;
      sustain.remove(key);
      keysUp(new HashSet<>(List.of(key)), timestamp_ms); // new Integer[] { key }
    }
    downers.remove(key);
  }

  @Override
  public void sustain(boolean isSustain, int myInt, long timestamp_ms) {
    this.isSustain = isSustain;
    if (!isSustain) {
      sustain.removeAll(downers);
      if (!sustain.isEmpty()) {
        for (int sus : sustain)
          velocity[sus + offset] = 0;
        keysUp(sustain, timestamp_ms);
        sustain.clear();
      }
    }
  }

  private int key2pitch(int key) {
    return key - offset;
  }

  /** @param timestamp_ms for instance 88681000, corresponds to 88.681 seconds after starting
   * @param pitch
   * @param velocity */
  public abstract void keyDown(int pitch, int velocity, long timestamp_ms);

  /** @param timestamp_ms
   * @param mySet release of sustain might cause multiple notes to be lifted */
  public abstract void keysUp(Set<Integer> mySet, long timestamp_ms);

  public NavigableMap<Integer, Integer> getVelocityMap(int thresh) {
    NavigableMap<Integer, Integer> navigableMap = new TreeMap<>();
    // extended piano range 97 keys
    for (int c0 = 12; c0 <= 108; ++c0)
      if (thresh < velocity[c0])
        navigableMap.put(c0, velocity[c0]);
    return navigableMap;
  }

  public NavigableMap<Integer, Integer> getVelocityMap() {
    return getVelocityMap(0);
  }
}
