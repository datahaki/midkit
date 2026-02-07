// code by jph
// http://www.midi.org/techspecs/midimessages.php
// http://computermusicresource.com/Control.Change.html
// http://tweakheadz.com/midi-controllers/
package ch.alpine.midkit;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.function.IntUnaryOperator;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import ch.alpine.bridge.lang.SI;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.Integers;

/** for more constants see {@link ShortMessage} */
public enum Midi {
  ;
  public static final int TEXT = 0x01; // in Sibelius Track 0: composer/comment
  public static final int COPYRIGHT_NOTICE = 0x02;
  public static final int TRACK_NAME = 0x03; // in Sibelius Track 0: title of piece
  /** The name of an instrument in the current track */
  public static final int INSTRUMENT_NAME = 0x04;
  /** Lyrics; in any track (not just 0). */
  public static final int LYRICS = 0x05;
  /** The text of a marker; in any track (not just 0). */
  public static final int MARKER = 0x06;
  public static final int CUE_POINT = 0x07;
  public static final int PROGRAM_NAME = 0x08;
  public static final int DEVICE_NAME = 0x09;
  public static final int CHANNEL_PREFIX = 0x20; // FF 20 01 cc
  public static final int CC_MSB = 0x00;
  public static final int CC_LSB = 0x20;
  public static final int PORT = 0x21; // FF 21 01 pp
  /** Mandatory event must be the last event in each MTrk chunk, and that should be the only occurrence per track. */
  public static final int TRACK_END = 0x2f; //
  public static final int TEMPO = 0x51;
  /** SMPTE time to denote playback offset from the beginning */
  public static final int SMPTE_OFFSET = 0x54;
  /** Time signature, metronome clicks, and size of a beat in 32nd notes */
  public static final int TIME_SIGNATURE = 0x58;
  /** byte 0 is inverted tonic byte 1 is flag for major/minor */
  public static final int KEY_SIGNATURE = 0x59;
  public static final int SEQUENCER_SPECIFIC = 0x7F; //
  // ---
  // for CONTROL_CHANGE
  public static final int BANK_SELECT = 0x00;
  public static final int MODULATION_WHEEL = 0x01;
  public static final int BREATH_CONTROLLER = 0x02;
  public static final int DATA_ENTRY_MSB = 0x06;
  public static final int VOLUME = 0x07;
  public static final int PAN_POSITION = 0x0a;
  public static final int SUSTAIN_PEDAL = 0x40; // Damper Pedal on/off
  public static final int PORTAMENTO = 0x41;
  public static final int SOSTENUTO_PEDAL = 0x42;
  public static final int SOFT_PEDAL = 0x43;
  public static final int LEGATO_FOOTSWITCH = 0x44;
  /** "hold 2 pedal", "harmonic pedal" in Pianoteq see also
   * <a href="http://www.harmonicpianopedal.com/">harmonicpianopedal.com</a> */
  public static final int HARMONIC_PEDAL = 0x45; // previously ALT_SUSTAIN_PEDAL
  /** default: Reverb Send Level - see MMA RP-023 formerly External Effects Depth */
  public static final int EFFECTS_1_DEPTH = 0x5b; // 91
  /** formerly Tremolo Depth */
  public static final int EFFECTS_2_DEPTH = 0x5c; // 92
  /** default: Chorus Send Level - see MMA RP-023 formerly Chorus Depth */
  public static final int EFFECTS_3_DEPTH = 0x5d; // 93
  /** formerly Celeste [Detune] Depth */
  public static final int EFFECTS_4_DEPTH = 0x5e; // 94
  /** formerly Phaser Depth */
  public static final int EFFECTS_5_DEPTH = 0x5f; // 95
  /** Non-Registered Parameter Number (NRPN) - LSB */
  public static final int NRPN_LSB = 0x62; // 98
  /** Non-Registered Parameter Number (NRPN) - MSB */
  public static final int NRPN_MSB = 0x63; // 99
  public static final int ALL_SOUND_OFF = 0x78; // 120
  public static final int ALL_CONTROLLERS_OFF = 0x79; // 121
  public static final int ALL_NOTES_OFF = 0x7b; // 123
  // ---
  public static final int MASK_0xF0 = 0xf0;
  public static final int SYSTEM_EXCLUSIVE = SysexMessage.SYSTEM_EXCLUSIVE; // 0xf0;
  // ---
  public static final int PITCH_BEND_CENTER = 0x2000; // 8192
  public static final int MAX_DETUNE_VALUE = 0x3fff; // 16383
  // ---
  /** default velocity for note off command */
  public static final int NOTEOFF_VELOCITY_DEFAULT = 127;
  public static final int DRUMKIT_CHANNEL = 9;
  // ---
  /** MidiSystem.getMidiFileTypes() == new int[] {0, 1}
   * but format type 0 throws "MIDI file type is not supported" java supports MIDI format type 1 */
  public static final int SUPPORTED_FILETYPE = 1;
  // ---
  private static final int[] FRAMERATE = new int[] { 24, 25, 30, 30 };
  private static final IntUnaryOperator CLIP = Integers.clip(0, 127);

  public static int getFrameRate(byte value) {
    return FRAMERATE[value >>> 5];
  }

  public static int clip7bit(int value) {
    return CLIP.applyAsInt(value);
  }

  private static final Scalar BASE = SI.PER_MINUTE.quantity(60000000);

  /** @param data with at least 6 bytes to read from
   * @return with unit min^-1 */
  public static Scalar parseTempo(byte[] _data) {
    Integers.requireEquals(_data[1], TEMPO);
    byte[] data = _data.clone();
    data[2] = 0;
    return BASE.divide(RealScalar.of(ByteBuffer.wrap(data, 2, 4).getInt()));
  }

  /** @param bpm with unit compatible to min^-1
   * @return
   * @throws InvalidMidiDataException */
  public static MidiMessage tempo(Scalar bpm) throws InvalidMidiDataException {
    int muq = SI.DIMENSIONLESS.intValue(BASE.divide(bpm));
    byte[] data = new byte[3]; // 0 is most significant byte, then 1 and 2
    data[2] = (byte) muq;
    muq >>= 8;
    data[1] = (byte) muq;
    muq >>= 8;
    data[0] = (byte) muq;
    return new MetaMessage(TEMPO, data, data.length);
  }

  public static MidiMessage timeSignature(int num, int den) throws InvalidMidiDataException {
    byte[] data = new byte[4]; // 0 is most significant byte, then 1 and 2
    data[0] = (byte) num;
    data[1] = (byte) Integers.log2Exact(den);
    // The standard MIDI clock ticks every 24 times every quarter note (crotchet).
    // A value of 24 means that the metronome clicks once every quarter note.
    data[2] = 24;
    // This value specifies the number of 1/32nds of a note happen every MIDI quarter note.
    // It is usually 8 which means that a quarter note happens every quarter note.
    data[3] = 8;
    return new MetaMessage(TIME_SIGNATURE, data, data.length);
  }

  public static MidiDevice fromInfo(Info info) {
    try {
      return MidiSystem.getMidiDevice(info);
    } catch (MidiUnavailableException midiUnavailableException) {
      throw new RuntimeException(midiUnavailableException);
    }
  }

  /** invokes MidiSystem::write for file type 1
   * 
   * @param sequence
   * @param file
   * @throws Exception */
  public static void write(Sequence sequence, Path file) throws Exception {
    MidiSystem.write(sequence, SUPPORTED_FILETYPE, file.toFile());
  }
}
