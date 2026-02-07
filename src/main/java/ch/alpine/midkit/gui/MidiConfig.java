package ch.alpine.midkit.gui;

import java.util.List;
import java.util.stream.Stream;

import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
public class MidiConfig {
  public final List<MidiDeviceGetParam> gets;
  public final List<MidiDevicePutParam> puts;

  public MidiConfig(int limit) {
    gets = Stream.generate(MidiDeviceGetParam::new).limit(limit).toList();
    puts = Stream.generate(MidiDevicePutParam::new).limit(limit).toList();
  }
}
