package ch.alpine.midkit;

public enum YamahaOnLinux {
  ;
  public static final String MIDI_PUT = "eX \\[hw:\\d+,0,0\\]";
  public static final String MIDI_GET = "eX \\[hw:\\d+,0,1\\]";
}
