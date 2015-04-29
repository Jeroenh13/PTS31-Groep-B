/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedInterfaces;

import java.rmi.RemoteException;
import RemoteObserver.RemotePublisher;

/**
 *
 * @author Bas
 */
 public interface IChat extends RemotePublisher  {
    
    
    public void sendMessage(String msg) throws RemoteException;;

    public void startNewServer(int portNr) throws RemoteException;
}
