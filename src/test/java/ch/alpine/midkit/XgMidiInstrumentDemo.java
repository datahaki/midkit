// code by jph
package ch.alpine.midkit;

enum XgMidiInstrumentDemo {
  ;
  static void main() {
    for (XgMidiInstrument xgMidiInstrument : XgMidiInstrument.values()) {
      String s = String.format("%30s %3d %3d", xgMidiInstrument, xgMidiInstrument.lsb(), xgMidiInstrument.prg() + 1);
      System.out.println(s);
    }
  }
}
