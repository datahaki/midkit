package ch.alpine.midkit.gui;

import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.put.MidiPutPredicate;

@ReflectionMarker
public class MidiDevicePutParam {
  @FieldSelectionCallback("all")
  public String put = "";

  public static List<String> all() {
    return MidiDevices.getList(MidiPutPredicate.INSTANCE, 5).stream() //
        .map(Info::getName) //
        .toList();
  }
}
