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
import java.net.UnknownHostException;

/**
 *
 * @author Bas
 */
public class client {

    static Socket client;
    static OutputStream outStream;
    static InputStream inStream;
    static ObjectOutputStream out;
    static ObjectInputStream in;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            System.out.println("open");
            client = new Socket("127.00.1", 1099);
            System.out.println("cl");
            outStream = client.getOutputStream();
            System.out.println("outs");
            inStream = client.getInputStream();
            System.out.println("ins");
            out = new ObjectOutputStream(outStream);
            System.out.println("out");
            in = new ObjectInputStream(inStream);
            System.out.println("in");
            String send = "newServer";
            System.out.println("send");
            out.writeObject(send);
            System.out.println("done");
        } catch (UnknownHostException e) {
            System.out.println("Error setting up socket connection: unknown host at 127.0.0.1 : 1099");
        } catch (IOException e) {
            System.out.println("Error setting up socket connection: " + e);
        }
    }

}
