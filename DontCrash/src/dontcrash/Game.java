/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;
import SharedInterfaces.IGame;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Timer;

/**
 *
 * @author Saya
 */
public class Game extends UnicastRemoteObject implements RemotePublisher, IGame, Serializable
{
    private int gameID;
    private List<Player> players;
    private Timer timer;
    
    /**
     * Initializes a new game with the given players
     * @param players of the game
     * @throws java.rmi.RemoteException
     */
    public Game(List<Player> players) throws RemoteException
    {
        this.players = players;
    }
    
    public Game()throws RemoteException{
        
    }
    
    /**
     * Updates the current game
     */
    public void Update()
    {
        
    }
    
    /**
     * 
     * @param object 
     * @return 
     */
    public Object getHitBy(Object object)
    {
        return null;
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startRun() {
        timer.
    }
}
