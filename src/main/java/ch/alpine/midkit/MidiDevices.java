// code by jph
package ch.alpine.midkit;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;

/** to create virtual midi on linux:
 * sudo modprobe snd-virmidi midi_devs=1
 * 
 * resulting in
 * Java: VirMIDI [hw:3,0,0], [hw:3,0,1] etc.
 * Pianoteq: Virtual Raw MIDI 3-0
 * amidi -l: Virtual Raw MIDI (16 subdevices) */
public enum MidiDevices {
  ;
  public static final int REGISTRY_PORT = Registry.REGISTRY_PORT;

  // TODO MIDI API too complicated
  public static List<Info> getList(Predicate<Info> predicate, int limit) {
    List<Info> list = new ArrayList<>();
    Map<String, Integer> map = new HashMap<>();
    for (Info info : MidiSystem.getMidiDeviceInfo()) {
      String string = info.toString();
      int index = string.indexOf('[');
      if (0 < index)
        string = string.substring(0, index);
      if (!map.containsKey(string) || map.get(string) < limit)
        if (predicate.test(info)) {
          map.put(string, map.containsKey(string) ? map.get(string) + 1 : 1);
          list.add(info);
        }
    }
    return list;
  }

  public static Optional<MidiDevice> fromName(Predicate<Info> predicate, String name) {
    return Stream.of(MidiSystem.getMidiDeviceInfo()) //
        .filter(predicate) //
        .filter(info -> info.getName().equals(name)) //
        .findFirst() //
        .map(Midi::fromInfo);
  }
}
