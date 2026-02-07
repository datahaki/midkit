// code by jph
package ch.alpine.midkit.put;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.qty.QuantityMagnitude;

/** implementation of {@link MidiPut} */
public class DirectMidiPut implements MidiPut {
  private static final int SEQUENCE_LIMIT = 3;
  private static final long OVER_NS = 1000_000_000L;

  /** @param midiDevice
   * @return
   * @throws Exception */
  public static MidiPut of(MidiDevice midiDevice) throws Exception {
    // System.out.println(DateTime.now() + " static get recv");
    Receiver receiver = MidiPutPredicate.getReceiver(midiDevice); //
    // System.out.println(DateTime.now() + " get seq");
    Sequencer sequencer = MidiSystem.getSequencer(false);
    // System.out.println(DateTime.now());
    sequencer.getTransmitter().setReceiver(receiver);
    // System.out.println(DateTime.now());
    sequencer.open();
    if (!sequencer.isOpen())
      throw new RuntimeException("sequencer not open");
    // System.out.println(DateTime.now());
    return new DirectMidiPut(midiDevice, receiver, sequencer);
  }

  /** ensures that not more than a certain sequences are played within a short period of time */
  private final List<Long> list = new LinkedList<>();
  private final MidiDevice midiDevice;
  private final Receiver receiver;
  private final Sequencer sequencer;

  private DirectMidiPut(MidiDevice midiDevice, Receiver receiver, Sequencer sequencer) {
    this.midiDevice = midiDevice;
    this.receiver = receiver;
    this.sequencer = sequencer;
  }

  @Override // from MidiOutput
  public void startSequence(Sequence sequence) {
    // TODO MIDI extract functionality to separate class
    long toc = System.nanoTime();
    list.removeIf(tic -> OVER_NS < toc - tic);
    if (list.size() < SEQUENCE_LIMIT) {
      list.add(toc);
      // mechanism that prevents LoopBe1 from muting
      try {
        sequencer.setSequence(sequence); // throws InvalidMidiDataException
        sequencer.start();
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      }
    } else
      System.out.println("prevented midi sequencer over-clocking");
    // new Exception("prevented midi sequencer over-clocking").printStackTrace();
  }

  @Override // from MidiOutput
  public boolean isRunning() {
    return sequencer.isRunning();
  }

  @Override // from MidiOutput
  public long getTickPosition() {
    return sequencer.getTickPosition();
  }

  @Override // from MidiOutput
  public void setTempoInBPM(Scalar bpm) {
    float value = QuantityMagnitude.SI().in("min^-1").apply(bpm).number().floatValue();
    sequencer.setTempoInBPM(value);
  }

  @Override // from MidiOutput
  public void stopSequencers() {
    sequencer.stop();
  }

  @Override // from MidiOutput
  public void sendMessage(MidiMessage midiMessage, long timestamp_us) {
    receiver.send(midiMessage, timestamp_us);
  }

  @Override // from AutoCloseable
  public void close() throws Exception {
    stopSequencers();
    sequencer.close();
    receiver.close();
    midiDevice.close();
    // System.out.println("midi dev closed");
  }

  public Info getDeviceInfo() {
    return midiDevice.getDeviceInfo();
  }
}
