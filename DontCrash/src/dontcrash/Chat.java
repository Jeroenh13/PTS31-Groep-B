package dontcrash;

import RMI.Server;
import SharedInterfaces.IChat;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;

/*
 * Class for sending chat messages
 */

/**
 * @author Bas
 */
public class Chat extends UnicastRemoteObject implements RemotePublisher, IChat{

    BasicPublisher bp ;

    /**
     * initializes a chat.
     * @throws RemoteException if there is a problem with the server. 
     */
    public Chat() throws RemoteException
    {
        bp = new BasicPublisher(new String[]{"Chat"});
    }
    
    /**
     * Adds a listener to the BasicPublisher
     * @param listener obj that will become listener
     * @param property listening to
     * @throws RemoteException RMI connection fails
     */
    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    /**
     * removes the earlier created listener to the BasicPublisher
     * @param listener obj that listens
     * @param property property that it was listening to
     * @throws RemoteException RMI connection fails
     */
    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }
    
    /**
     * sends a message to all listeners
     * @param msg message to be send
     */
    @Override
    public void sendMessage(String msg)
    {
        bp.inform(this, "Chat", null, msg);
    }
    
}
