// code by jph
package ch.alpine.midkit.put.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ch.alpine.midkit.put.MidiPut;
import ch.alpine.tensor.Scalar;

/** interface similar to {@link MidiPut} but each function potentially
 * throws {@link RemoteException}
 * 
 * @see MidiPut */
public interface RemoteMidiPut extends Remote {
  /** @param data
   * @throws RemoteException */
  void remote_startSequenceBytes(byte[] data) throws RemoteException;

  /** @return whether sequencer currently processing given sequence
   * @throws RemoteException */
  boolean remote_isRunning() throws RemoteException;

  /** @return
   * @throws RemoteException */
  long remote_getTickPosition() throws RemoteException;

  /** @param bpm with unit compatible to "min^-1"
   * @throws RemoteException */
  void remote_setTempoInBPM(Scalar bpm) throws RemoteException;

  /** @throws RemoteException */
  void remote_stopSequencers() throws RemoteException;

  /** @param data
   * @param ticks
   * @throws RemoteException */
  void remote_sendMessageBytes(byte[] data, long ticks) throws RemoteException;
}
