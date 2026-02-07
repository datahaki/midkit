// code by jph
package ch.alpine.midkit;

import javax.sound.midi.MidiDevice.Info;

import ch.alpine.midkit.get.MidiGetPredicate;
import ch.alpine.midkit.put.MidiPutPredicate;

enum MidiDevicesDemo {
  ;
  private static void print(Info info) {
    System.out.println(" == " + info.getName()); // eX [hw:2,0,0]
    System.out.println(" \\- " + info.getDescription()); // ESI MIDIMATE eX, USB MIDI, ESI MIDIMATE eX
    System.out.println(" \\- " + info.getVendor());
    System.out.println(" \\- " + info.getVersion());
    System.out.println();
  }

  static void main() {
    System.out.println("OUT");
    for (Info info : MidiDevices.getList(MidiPutPredicate.INSTANCE, 100)) {
      print(info);
    }
    System.out.println("IN");
    for (Info info : MidiDevices.getList(MidiGetPredicate.INSTANCE, 100)) {
      print(info);
    }
  }
}
