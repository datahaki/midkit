// code by jph
package ch.alpine.midkit.get;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import ch.alpine.bridge.lang.FriendlyFormat;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.UnitConvert;
import ch.alpine.tensor.sca.Round;

public enum ConsoleReceiver implements Receiver {
  INSTANCE;

  private final ScalarUnaryOperator scalarUnaryOperator = UnitConvert.SI().to("s");

  @Override
  public void send(MidiMessage midiMessage, long timeStamp) {
    Scalar scalar = scalarUnaryOperator.apply(Quantity.of(timeStamp, "us"));
    byte[] message = midiMessage.getMessage();
    System.out.println(FriendlyFormat.of(message) + " " + scalar.maps(Round._6));
  }

  @Override
  public void close() {
    // ---
  }
}
