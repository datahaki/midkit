// code by jph
package ch.alpine.midkit.put.rmi;

import java.io.ByteArrayInputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.MidiMessageAdapter;
import ch.alpine.midkit.put.MidiPut;
import ch.alpine.tensor.Scalar;

public class RemoteMidiPutServer extends UnicastRemoteObject implements RemoteMidiPut, AutoCloseable {
  public static final String HOST = "//localhost:" + MidiDevices.REGISTRY_PORT + "/";
  public static final String PREFIX = RemoteMidiPut.class.getSimpleName() + ".";
  // ---
  private final MidiPut midiPut;
  private final String bind;

  public RemoteMidiPutServer(MidiPut midiPut) throws Exception {
    this.midiPut = Objects.requireNonNull(midiPut);
    // TODO MIDI better name
    bind = HOST + PREFIX + "idontknow";
    Naming.bind(bind, this);
  }

  @Override // from RemoteMidiOutput
  public void remote_startSequenceBytes(byte[] data) throws RemoteException {
    try {
      Sequence sequence = MidiSystem.getSequence(new ByteArrayInputStream(data));
      midiPut.startSequence(sequence);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override // from RemoteMidiOutput
  public boolean remote_isRunning() throws RemoteException {
    return midiPut.isRunning();
  }

  @Override // from RemoteMidiOutput
  public long remote_getTickPosition() throws RemoteException {
    return midiPut.getTickPosition();
  }

  @Override // from RemoteMidiOutput
  public void remote_setTempoInBPM(Scalar bpm) throws RemoteException {
    midiPut.setTempoInBPM(bpm);
  }

  @Override // from RemoteMidiOutput
  public void remote_stopSequencers() throws RemoteException {
    midiPut.stopSequencers();
  }

  @Override // from RemoteMidiOutput
  public void remote_sendMessageBytes(byte[] data, long ticks) throws RemoteException {
    try {
      midiPut.sendMessage(new MidiMessageAdapter(data), ticks);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Override // from RemoteMidiOutput
  public void close() throws Exception {
    Naming.unbind(bind);
    midiPut.close();
  }
}
