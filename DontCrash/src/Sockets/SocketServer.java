/*
 * Server for creating new chats for rooms.
 */
package Sockets;
import dontcrash.portsAndIps;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bas  
 */
public class SocketServer implements Runnable {
   
    /**
     * Creates a new server for input to create rooms from clients.
     */
    @Override
    public void run() {
         ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portsAndIps.SocketPort);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 1095.");
            System.exit(1);
        }
               
        while (true) {
             try {
                 // create new Thread which runs Multiserverrunnable
                 Thread t = new Thread(new AcceptServer(serverSocket.accept()));
                 // start Thread
                 t.start();
             } catch (IOException ex) {
                 Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }
}
