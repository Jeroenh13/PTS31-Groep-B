/*
 * Handles the creation of a new server trough sockets.
 */
package Sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import RMI.Server;

/**
 *
 * @author Bas
 */
class RunServer implements Runnable {
    
    
    Socket so = null;

    OutputStream outStream ;
    InputStream inStream ;
    ObjectInputStream in ;
    ObjectOutputStream out;

    public RunServer(Socket accept) {
        so = accept;
    }

    @Override
    public void run() {
        try {
            outStream = so.getOutputStream();
            inStream = so.getInputStream();
            in = new ObjectInputStream(inStream);
            out = new ObjectOutputStream(outStream);
            out.writeObject(Server.createNewServer("Chat", "Chat"));
        } catch (IOException ex) {
            Logger.getLogger(RunServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
