/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import RMI.Server;

/**
 *
 * @author Bas
 */
class runServer implements Runnable {
    
    
    Socket so = null;

    OutputStream outStream ;
    InputStream inStream ;
    ObjectInputStream in ;
    ObjectOutputStream out;

    public runServer(Socket accept) {
        so = accept;
    }

    @Override
    public void run() {
        System.out.println("Open");
        String input = "";
        try {
            
            outStream = so.getOutputStream();
            inStream = so.getInputStream();
            in = new ObjectInputStream(inStream);
            out = new ObjectOutputStream(outStream);
            System.out.println("Open");
            input =(String) in.readObject();
            System.out.println("done reading");
        } catch (IOException ex) {
            Logger.getLogger(runServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(runServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("if");
        if(input.equals("newServer"))
        {
            try {
                
        System.out.println("new");
                Server.createNewServer("Admin");
                
        System.out.println("done");
            } catch (IOException ex) {
                Logger.getLogger(runServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
