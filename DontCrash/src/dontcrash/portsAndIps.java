/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Bas
 */
public class portsAndIps {
    /**
     * IP Address of the database
     */
    public final static String databaseIP="10.0.0.11";
    
    /**
     * IP Address of the server
     */
    public final static String IP = "10.0.0.11";
    /**
     * Default chat port of the server
     */
    public final static int defaultServerPortChat = 1099;
    private static int lastPort = 1100;
    
    private static boolean isPortInUse(int port) throws IOException {
        // Assume no connection is possible.
        boolean result = false;

        try {
            (new Socket(IP, port)).close();
            result = true;
        } catch (SocketException e) {
            // Could not connect.
        }

        return result;
    }
    
    /**
     * Gets a new open port
     * @return new open port.
     * @throws IOException 
     */
    public static int getNewPort() throws IOException
    {
        while(isPortInUse(lastPort)){
            //System.out.println(lastPort+" is in gebruik");
            lastPort++;}
        return lastPort;
    }
}
