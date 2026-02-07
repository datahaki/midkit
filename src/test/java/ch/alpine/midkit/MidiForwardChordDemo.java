// code by jph
package ch.alpine.midkit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import ch.alpine.midkit.get.MidiGetPredicate;
import ch.alpine.midkit.put.MidiPutPredicate;

/** for each key input the program sends a chord back */
enum MidiForwardChordDemo {
  ;
  static void main() throws Exception {
    MidiDevice midiDevice0 = null;
    for (Info info : MidiDevices.getList(MidiPutPredicate.INSTANCE, 100)) {
      if (info.getName().matches(YamahaOnLinux.MIDI_PUT))
        midiDevice0 = MidiSystem.getMidiDevice(info);
    }
    if (Objects.isNull(midiDevice0)) {
      System.err.println("not found.");
      return;
    }
    Receiver receiver = midiDevice0.getReceiver();
    if (!midiDevice0.isOpen())
      midiDevice0.open();
    receiver.send(new ShortMessage(ShortMessage.PROGRAM_CHANGE + 0, 56, 0), -1); // select instrument
    receiver.send(new ShortMessage(ShortMessage.PROGRAM_CHANGE + 1, 73, 0), -1); // select instrument
    MidiDevice midiDevice1 = null;
    for (Info info : MidiDevices.getList(MidiGetPredicate.INSTANCE, 100)) {
      if (info.getName().matches(YamahaOnLinux.MIDI_GET))
        midiDevice1 = MidiSystem.getMidiDevice(info);
    }
    if (Objects.isNull(midiDevice1)) {
      System.err.println("not found.");
      return;
    }
    int[] chord = { 0, 24 };
    midiDevice1.getTransmitter().setReceiver(new Receiver() {
      @Override
      public void send(MidiMessage midiMessage, long timeStamp) {
        byte[] message = midiMessage.getMessage();
        int cmd = message[0] & 0xff;
        if (cmd == ShortMessage.NOTE_ON || cmd == ShortMessage.NOTE_OFF) {
          if (cmd == ShortMessage.NOTE_ON) {
            Duration duration = Duration.of(timeStamp, ChronoUnit.MICROS);
            System.out.println(duration + " " + cmd);
          }
          try {
            int c = 0;
            for (int tonic : chord) {
              ShortMessage shortMessage = new ShortMessage(cmd + ((c++) % 2), (message[1] + tonic) & 0xff, message[2] & 0xff);
              receiver.send(shortMessage, -1);
            }
          } catch (InvalidMidiDataException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void close() {
        // ---
      }
    });
    if (!midiDevice1.isOpen())
      midiDevice1.open();
  }
}
