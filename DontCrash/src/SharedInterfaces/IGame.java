/*
 * Interface that gets send over
 */
package SharedInterfaces;

import RemoteObserver.RemotePublisher;
import java.rmi.RemoteException;

/**
 *
 * @author Bas
 */
 public interface IGame extends RemotePublisher {

    public void startRun() throws RemoteException;
    
}
