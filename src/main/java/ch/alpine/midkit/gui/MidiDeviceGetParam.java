// code by jph
package ch.alpine.midkit.gui;

import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.get.MidiGetPredicate;

@ReflectionMarker
public class MidiDeviceGetParam {
  @FieldSelectionCallback("all")
  public String get = "";

  public static List<String> all() {
    return MidiDevices.getList(MidiGetPredicate.INSTANCE, 5).stream() //
        .map(Info::getName) //
        .toList();
  }
}
