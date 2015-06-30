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

     /**
      * 
      * TODO Create JAVADOC
      * 
      * @throws RemoteException 
      */
    public void startRun() throws RemoteException;

    public void Close() throws RemoteException;
    
}
