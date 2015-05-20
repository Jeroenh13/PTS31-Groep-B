/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SharedInterfaces;

import RemoteObserver.RemotePublisher;
import dontcrash.Player;
import dontcrash.Room;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Bas
 */
public interface IAdministator  extends RemotePublisher {
    public int startNewServer(String type) throws RemoteException;
    public Room newRoom(Player host)throws RemoteException;
    public List<Room> getRooms() throws RemoteException;
    public boolean joinRoom(Player player, int roomID) throws RemoteException;
}
