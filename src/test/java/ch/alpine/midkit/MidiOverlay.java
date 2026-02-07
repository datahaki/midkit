// code by jph
package ch.alpine.midkit;

import java.awt.Font;
import java.util.Objects;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import ch.alpine.bridge.ref.FieldsEditorParam;
import ch.alpine.bridge.ref.ann.FieldFuse;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.bridge.ref.util.DialogFieldsEditor;
import ch.alpine.bridge.ref.util.FieldsEditor;
import ch.alpine.bridge.ref.util.ReflectionMarkers;
import ch.alpine.bridge.swing.LookAndFeels;
import ch.alpine.midkit.put.MidiPutPredicate;

@ReflectionMarker
public class MidiOverlay {
  public final SingleTone singleTone = new SingleTone();
  @FieldFuse
  public Boolean program_change = false;
  @FieldFuse
  public Boolean on = false;
  @FieldFuse
  public Boolean off = false;

  static void main() throws Exception {
    ReflectionMarkers.INSTANCE.DEBUG_PRINT.set(true);
    LookAndFeels.LIGHT.updateComponentTreeUI();
    FieldsEditorParam.GLOBAL.textFieldFont = new Font(Font.DIALOG_INPUT, Font.PLAIN, 20);
    FieldsEditorParam.GLOBAL.textFieldFont_override = true;
    FieldsEditorParam.GLOBAL.checkBoxParam.override = true;
    FieldsEditorParam.GLOBAL.checkBoxParam.size = 30;
    // ---
    MidiDevice midiDevice = null;
    for (Info info : MidiDevices.getList(MidiPutPredicate.INSTANCE, 100)) {
      if (info.getName().matches(YamahaOnLinux.MIDI_PUT))
        midiDevice = MidiSystem.getMidiDevice(info);
    }
    if (Objects.isNull(midiDevice)) {
      System.err.println("not found.");
      return;
    }
    Receiver receiver = midiDevice.getReceiver();
    if (!midiDevice.isOpen())
      midiDevice.open();
    // ---
    MidiOverlay midiOverlay = new MidiOverlay();
    DialogFieldsEditor dialogFieldsEditor = DialogFieldsEditor.show(null, midiOverlay, "title");
    FieldsEditor fieldsEditor = dialogFieldsEditor.fieldsEditor();
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          int channel = midiOverlay.singleTone.channel;
          if (midiOverlay.program_change) {
            midiOverlay.program_change = false;
            receiver.send(new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, midiOverlay.singleTone.instrumentSelection.midiInstrument.ordinal(), 0), -1); // select
                                                                                                                                                               // piano
          }
          int pitch = midiOverlay.singleTone.pitch.intValue();
          int vel = midiOverlay.singleTone.vel.intValue();
          if (midiOverlay.on) {
            midiOverlay.on = false;
            receiver.send(new ShortMessage(ShortMessage.NOTE_ON, channel, pitch, vel), -1);
          }
          if (midiOverlay.off) {
            midiOverlay.off = false;
            receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, channel, pitch, vel), -1);
          }
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    };
    fieldsEditor.addUniversalListener(runnable);
  }
}
