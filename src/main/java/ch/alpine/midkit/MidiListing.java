// code by jph
// https://metacpan.org/module/MIDI::XML::SmpteOffset
package ch.alpine.midkit;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import ch.alpine.bridge.lang.EnumValue;

public class MidiListing {
  static final Charset CHARSET = StandardCharsets.UTF_8;
  // ---
  final SortedMap<Long, List<String>> sortedMap = new TreeMap<>();
  final Set<Integer> nprnSet = new TreeSet<>();

  /** @param file midi file
   * @throws Exception if file is corrupt */
  public MidiListing(Path file) throws Exception {
    this(MidiSystem.getSequence(file.toFile()));
  }

  public MidiListing(Sequence sequence) {
    {
      StringBuilder stringBuffer = new StringBuilder();
      float divisionType = sequence.getDivisionType();
      stringBuffer.append("DivisionType: ");
      stringBuffer.append(divisionType == Sequence.PPQ ? "PPQ with quarter=" : divisionType + " with res=");
      stringBuffer.append(sequence.getResolution());
      sortedMap.put(-1L, List.of(stringBuffer.toString()));
    }
    int track_count = 0;
    byte nrpn_msb = 0;
    for (Track track : sequence.getTracks()) {
      for (int index = 0; index < track.size(); ++index) {
        MidiEvent midiEvent = track.get(index);
        long tick = midiEvent.getTick();
        MidiMessage midiMessage = midiEvent.getMessage();
        byte[] message = midiMessage.getMessage();
        StringBuilder stringBuilder = new StringBuilder();
        {
          int pos = 0;
          for (byte val : message) {
            int myInt = val & 0xff;
            if (pos == 0) {
              String string = String.format("%02x  ", myInt);
              boolean myBoolean = (message[0] & Midi.MASK_0xF0) != Midi.MASK_0xF0;
              stringBuilder.append(myBoolean ? string.substring(0, 1) + ":" + string.substring(1, 3) : string);
            } else
              stringBuilder.append(String.format("%02x ", myInt));
            ++pos;
          }
        }
        switch (message[0] & Midi.MASK_0xF0) { // as "command value"
        case ShortMessage.NOTE_OFF: // 0x80
          stringBuilder.append(String.format("%5d off", message[1] & 0xff)); // KeySignature.C.chromaticScale().getTone().toString())
          break;
        case ShortMessage.NOTE_ON: // 0x90
          stringBuilder.append(String.format("%5d on", message[1] & 0xff)); // KeySignature.C.chromaticScale().getTone(message[1]).toString()
          if (message[2] == 0)
            stringBuilder.append(" (mute)");
          break;
        case ShortMessage.POLY_PRESSURE: // 0xa0
          stringBuilder.append("POLY_PRESSURE");
          break;
        case ShortMessage.CONTROL_CHANGE: // 0xb0
          switch (message[1] & 0xff) {
          case Midi.DATA_ENTRY_MSB:
            stringBuilder.append("data " + message[2]);
            break;
          case Midi.VOLUME:
            stringBuilder.append("volume " + message[2]);
            break;
          case Midi.SUSTAIN_PEDAL:
            stringBuilder.append("sustain pedal " + message[2]);
            break;
          case Midi.SOSTENUTO_PEDAL:
            stringBuilder.append("sostenuto pedal " + message[2]);
            break;
          case Midi.SOFT_PEDAL:
            stringBuilder.append("soft pedal " + message[2]);
            break;
          case Midi.EFFECTS_1_DEPTH:
            stringBuilder.append("reverb " + message[2]);
            break;
          case Midi.EFFECTS_3_DEPTH:
            stringBuilder.append("chorus " + message[2]);
            break;
          case Midi.NRPN_LSB: {
            int nrpn = nrpn_msb * 128 + message[2];
            nprnSet.add(nrpn);
            stringBuilder.append("nr-param# lsb " + message[2] + " (" + nrpn + ")");
            break;
          }
          case Midi.NRPN_MSB:
            nrpn_msb = message[2];
            stringBuilder.append("nr-param# msb " + message[2]);
            break;
          case Midi.ALL_NOTES_OFF:
            stringBuilder.append("all notes off");
            break;
          default:
            stringBuilder.append("CONTROL_CHANGE");
            break;
          }
          break;
        case ShortMessage.PROGRAM_CHANGE: // 0xc0
          stringBuilder.append("PROGRAM_CHANGE");
          stringBuilder.append(" " + EnumValue.fromOrdinal(MidiInstrument.class, message[1] & 0xff));
          break;
        case ShortMessage.CHANNEL_PRESSURE: // 0xd0
          stringBuilder.append("CHANNEL_PRESSURE");
          break;
        case ShortMessage.PITCH_BEND: // 0xe0 pitch wheel change
          int pval = (message[1]) + ((message[2] & 0xff) << 7) - Midi.PITCH_BEND_CENTER;
          stringBuilder.append("PITCH_BEND " + pval); // value & 0x7f, value >> 7
          break;
        case Midi.MASK_0xF0: // e.g. 0xff
          switch (message[0] & 0xff) { // as "status byte"
          case Midi.SYSTEM_EXCLUSIVE: // System_Exclusive
            stringBuilder.append("SYSTEM_EXCLUSIVE");
            break;
          case ShortMessage.MIDI_TIME_CODE: // 0xf1
            stringBuilder.append("MIDI_TIME_CODE");
            break;
          case ShortMessage.SONG_POSITION_POINTER: // 0xf2
            stringBuilder.append("SONG_POSITION_POINTER");
            break;
          case ShortMessage.SONG_SELECT: // 0xf3
            stringBuilder.append("SONG_SELECT");
            break;
          case ShortMessage.TUNE_REQUEST: // 0xf6
            stringBuilder.append("TUNE_REQUEST");
            break;
          case ShortMessage.END_OF_EXCLUSIVE: // 0xf7
            stringBuilder.append("END_OF_EXCLUSIVE");
            break;
          case ShortMessage.TIMING_CLOCK: // 0xf8
            stringBuilder.append("TIMING_CLOCK");
            break;
          case ShortMessage.START: // 0xfa
            stringBuilder.append("START");
            break;
          case ShortMessage.CONTINUE: // 0xfb
            stringBuilder.append("CONTINUE");
            break;
          case ShortMessage.STOP: // 0xfc
            stringBuilder.append("STOP");
            break;
          // 0xfd undefined (reserved)
          case ShortMessage.ACTIVE_SENSING: // 0xfe
            stringBuilder.append("ACTIVE_SENSING");
            break;
          case ShortMessage.SYSTEM_RESET: // 0xff
            switch (message[1] & 0xff) { //
            case Midi.CHANNEL_PREFIX:
              stringBuilder.append("CHANNEL_PREFIX " + message[3]);
              break;
            case Midi.PORT:
              stringBuilder.append("PORT " + message[3]);
              break;
            case Midi.TRACK_END:
              stringBuilder.append("TRACK_END");
              break;
            case Midi.TEMPO:
              stringBuilder.append("TEMPO " + Midi.parseTempo(message));
              break;
            case Midi.SMPTE_OFFSET:
              stringBuilder.append("SMPTE_OFFSET framerate=" + Midi.getFrameRate(message[3]));
              break;
            case Midi.TIME_SIGNATURE:
              stringBuilder.append("TIME_SIGNATURE " + message[3] + "/" + (1 << message[4]));
              break;
            case Midi.KEY_SIGNATURE:
              stringBuilder.append("KEY_SIGNATURE " + message[3]);
              // in all our data myByte[4]==0, i.e. major/minor is not encoded
              break;
            case Midi.SEQUENCER_SPECIFIC:
              stringBuilder.append("SEQUENCER_SPECIFIC");
              break;
            case Midi.TEXT: // 1
            case Midi.COPYRIGHT_NOTICE: // 2
            case Midi.TRACK_NAME: // 3
            case Midi.INSTRUMENT_NAME: // 4
            case Midi.LYRICS: // 5
            case Midi.MARKER: // 6
            case Midi.CUE_POINT: // 7
              // replace hex with ASCII (potentially unsafe)
              stringBuilder.delete(4 + 2 * 3, stringBuilder.length());
              stringBuilder.append(new String(message, 3, message.length - 3, CHARSET));
              break;
            default:
              break;
            }
            break;
          }
          break;
        }
        if (!sortedMap.containsKey(tick))
          sortedMap.put(tick, new LinkedList<>());
        sortedMap.get(tick).add(String.format("%2d %s", track_count, stringBuilder));
      }
      ++track_count;
    }
  }

  private void dump(PrintStream printStream) {
    if (!sortedMap.isEmpty()) {
      String format = "%" + sortedMap.lastKey().toString().length() + "d";
      printStream.println("TICKS TRACK message");
      for (Entry<Long, List<String>> entry : sortedMap.entrySet())
        for (String string : entry.getValue())
          printStream.println(String.format(format, entry.getKey()) + " " + string);
    }
  }

  public void exportTo(Path file) throws Exception {
    try (PrintStream printStream = new PrintStream(Files.newOutputStream(file))) {
      dump(printStream);
    }
  }

  public void exportToHtml(Path file) throws Exception {
    try (PrintStream printStream = new PrintStream(Files.newOutputStream(file))) {
      printStream.append("<html><body><pre>\n");
      dump(printStream);
      printStream.append("</pre></body></html>\n");
    }
  }
}
