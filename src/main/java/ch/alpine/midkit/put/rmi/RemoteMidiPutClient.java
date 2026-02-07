// code by jph
package ch.alpine.midkit.put.rmi;

import java.io.ByteArrayOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import ch.alpine.midkit.Midi;
import ch.alpine.midkit.MidiDevices;
import ch.alpine.midkit.put.MidiPut;
import ch.alpine.tensor.Scalar;

public class RemoteMidiPutClient implements MidiPut {
  /** @return null if no true midi output is available */
  public static Optional<MidiPut> createAny() {
    return findAny().map(RemoteMidiPutClient::new);
  }

  public static Optional<RemoteMidiPut> findAny() {
    return available(MidiDevices.REGISTRY_PORT).stream() //
        .findFirst() //
        .map(RemoteMidiPutClient::getRemoteMidiOutput);
  }

  public static List<String> available(int port) {
    List<String> list = new LinkedList<>();
    try {
      Registry registry = LocateRegistry.getRegistry(port);
      for (String string : registry.list())
        if (string.startsWith(RemoteMidiPutServer.PREFIX))
          list.add(string);
    } catch (RemoteException remoteException) {
      remoteException.printStackTrace();
    }
    return list;
  }

  /** @param string from the list of available()
   * @return */
  private static RemoteMidiPut getRemoteMidiOutput(String string) {
    try {
      return (RemoteMidiPut) Naming.lookup("rmi:" + RemoteMidiPutServer.HOST + string);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
  }

  // ---
  private final RemoteMidiPut remoteMidiPut;

  private RemoteMidiPutClient(RemoteMidiPut remoteMidiPut) {
    this.remoteMidiPut = Objects.requireNonNull(remoteMidiPut);
  }

  @Override // from MidiOutput
  public void startSequence(Sequence sequence) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      MidiSystem.write(sequence, Midi.SUPPORTED_FILETYPE, byteArrayOutputStream);
      remoteMidiPut.remote_startSequenceBytes(byteArrayOutputStream.toByteArray());
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override // from MidiOutput
  public boolean isRunning() {
    try {
      return remoteMidiPut.remote_isRunning();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override // from MidiOutput
  public long getTickPosition() {
    try {
      return remoteMidiPut.remote_getTickPosition();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override // from MidiOutput
  public void setTempoInBPM(Scalar bpm) {
    try {
      remoteMidiPut.remote_setTempoInBPM(bpm);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override // from MidiOutput
  public void stopSequencers() {
    try {
      remoteMidiPut.remote_stopSequencers();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override // from MidiOutput
  public void sendMessage(MidiMessage midiMessage, long timestamp_us) {
    try {
      remoteMidiPut.remote_sendMessageBytes(midiMessage.getMessage(), timestamp_us);
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void close() throws Exception {
    // TODO MIDI close remoteMidiPut ?
  }
}
