// code by jph
package ch.alpine.midkit.put.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ch.alpine.midkit.MidiDevices;

/** Careful: do not invoke twice on same pc */
public enum RemoteMidiPutSingleton {
  INSTANCE;

  private final Registry registry;

  RemoteMidiPutSingleton() {
    try {
      registry = LocateRegistry.createRegistry(MidiDevices.REGISTRY_PORT);
    } catch (RemoteException remoteException) {
      throw new RuntimeException(remoteException);
    }
  }

  public Registry registry() {
    return registry;
  }
}
