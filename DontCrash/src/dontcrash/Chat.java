package dontcrash;

import RMI.Server;
import SharedInterfaces.IChat;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bas
 */
public class Chat extends UnicastRemoteObject implements RemotePublisher, IChat{

    BasicPublisher bp ;
    
    public Chat() throws RemoteException
    {
        bp = new BasicPublisher(new String[]{"Chat"});
    }
    
    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeListener(listener, property);
    }
    
    @Override
    public void sendMessage(String msg)
    {
        bp.inform(this, "Chat", null, msg);
    }

    @Override
    public void startNewServer(int portNr) {
        Server s = new Server(portNr);
    }
    
}
