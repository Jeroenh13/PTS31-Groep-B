/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Bas
 */
public class newServer {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1099);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 1099.");
            System.exit(1);
        }
        boolean calc = true;
        
        
        while (calc) {
            // create new Thread which runs Multiserverrunnable
            Thread t = new Thread(new runServer(serverSocket.accept()));
            // start Thread
            t.start();
        }
    }
}
