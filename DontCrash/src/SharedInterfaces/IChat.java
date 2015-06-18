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
    
     /**
     * sends a message to all listeners
     * @param msg message to be send
     * @throws java.rmi.RemoteException
     */
    public void sendMessage(String msg) throws RemoteException;

}
