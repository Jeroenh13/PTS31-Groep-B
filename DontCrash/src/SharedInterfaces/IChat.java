/*
 * Interface that gets send over
 */
package SharedInterfaces;

import java.rmi.RemoteException;
import RemoteObserver.RemotePublisher;

/**
 *
 * @author Bas
 */
 public interface IChat extends RemotePublisher  {
    
    public void sendMessage(String msg) throws RemoteException;

}
