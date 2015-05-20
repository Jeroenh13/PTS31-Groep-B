/*
 * Sets up a chat.
 */
package dontcrash;

import RMI.RMIClient;
import RemoteObserver.RemotePropertyListener;
import SharedInterfaces.IChat;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bas
 */
public class ActualChat extends Observable implements RemotePropertyListener  {
    private IChat chat;
    private String name;
    
    /**
     * sets up the connection RMI
     * @param ip ip of the server
     * @param port port of the server
     * @param portObj port wanted to export this
     * @param name name of the player
     */
    public ActualChat(String ip, int port,int portObj, String name)
    {
        this.name = name;
        RMIClient  rmi = new RMIClient(ip, port,"Chat");
        chat = rmi.setUpChat();
        try {
            UnicastRemoteObject.exportObject(this, portObj);
            chat.addListener(this, "Chat");
        } catch (RemoteException ex) {
            Logger.getLogger(DontCrash.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }
        

    /**
     * if there is a new incoming message.
     * @param evt message
     * @throws RemoteException if the connection to RMI fails 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        this.setChanged();
        this.notifyObservers((String)evt.getNewValue());
    }

    /**
     * Sends a message to the server
     * @param text message that needs to be send
     * @throws RemoteException if there is a problem with the RMI connection
     */
    public void sendMessage(String text) throws RemoteException {
        chat.sendMessage(name+": " +text);
    }

    /**
     * Removes the connection
     * @throws RemoteException if the connection the RMI fails 
     */
    public void removeObserver() throws RemoteException {
        chat.removeListener(this, "Chat");
    }
}
