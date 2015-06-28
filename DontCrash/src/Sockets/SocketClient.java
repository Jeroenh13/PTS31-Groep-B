/*
 * Client that creates a new chat server. and returns its ports.
 */
package Sockets;

import dontcrash.portsAndIps;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

/**
 *
 * @author Bas
 */
public class SocketClient implements Callable {

    private Socket client;
    private InputStream inStream;
    private ObjectInputStream in;
    private OutputStream outStream;
    private ObjectOutputStream out;
    private int port;

    /**
     * Initializes a new SocketClient
     */
    public SocketClient() {
    }

    /**
     * Creates a new chat
     *
     * @return port the chat is created upon.
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {

        try {
            client = new Socket(portsAndIps.IP, portsAndIps.SocketPort);
            outStream = client.getOutputStream();
            out = new ObjectOutputStream(outStream);
            inStream = client.getInputStream();
            in = new ObjectInputStream(inStream);
        } catch (UnknownHostException e) {
            System.out.println("Error setting up socket connection: unknown host at 127.0.0.1 : 1099");
        } catch (IOException e) {
            System.out.println("Error setting up socket connection: " + e);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            port = (int) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }
        return port;
    }

}
