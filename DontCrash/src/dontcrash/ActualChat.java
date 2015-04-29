/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private String naam;
    
    public ActualChat(String ip, int port,int portObj, String naam)
    {
        this.naam = naam;
        RMIClient  rmi = new RMIClient(ip, port);
        chat = rmi.setUp();
        try {
            UnicastRemoteObject.exportObject(this, portObj);
            chat.addListener(this, "Chat");
        } catch (RemoteException ex) {
            Logger.getLogger(DontCrash.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        this.setChanged();
        this.notifyObservers((String)evt.getNewValue());
    }

    public void sendMessage(String text) throws RemoteException {
        chat.sendMessage(naam+": " +text);
    }

    public void newServer(int parseInt) {
        try {
            chat.startNewServer(parseInt);
        } catch (RemoteException ex) {
            Logger.getLogger(ActualChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
