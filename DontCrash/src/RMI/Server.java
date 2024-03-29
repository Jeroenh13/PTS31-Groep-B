/*
 * Server that makes different clients communicate with eachother.
 *
 * Note: for optimalization System.out.println outcommented.
 */
package RMI;

import dontcrash.Chat;
import SharedInterfaces.IChat;
import SharedInterfaces.IAdministator;
import dontcrash.Administration;
import dontcrash.portsAndIps;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;

/**
 *
 * @author Bas
 */
public class Server {

    private static final int portNumber = portsAndIps.defaultServerPortChat;

    // Set binding name for student administration
    private static String bindingName = "Chat";

    // References to registry and student administration
    private Registry registry = null;
    private IChat chat = null;
    private IAdministator admin = null;
    // Constructor
    /**
     * Constructor
     */
    public Server() {

        // Print port number for registry
        System.out.println("Server: Port number " + portNumber);

        try {
            chat = (IChat) new Chat();
            System.out.println("Server: chatServer created");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create Chat");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            chat = null;
        }

        // Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind("Registry", registry);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }
                
        // Bind Mockeffectenbeurs using registry
        try {
            registry.rebind(bindingName, chat);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind chat");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }

    /**
     * creates a new server
     * @param port port the server uses
     * @param usage What kind of server
     */
    public Server(int port, String usage) {

        // Print port number for registry
        //System.out.println("Server: Port number " + port);

        switch (usage) {
            case "Administrator": 
                try {
                    admin = (IAdministator) new Administration();
                    System.out.println("Server:  newServerObj created");
                } catch (RemoteException ex) {
                    System.out.println("Server: Cannot create newServerObj");
                    System.out.println("Server: RemoteException: " + ex.getMessage());
                    chat = null;
                }

                // Create registry at port number
                try {
                    registry = LocateRegistry.createRegistry(port);
                    System.out.println("Server: Registry created on port number " + port);
                } catch (RemoteException ex) {
                    System.out.println("Server: Cannot create registry");
                    System.out.println("Server: RemoteException: " + ex.getMessage());
                    registry = null;
                }

                // Bind newServer using registry
                try {
                    registry.rebind(bindingName, admin);
                } catch (RemoteException ex) {
                    System.out.println("Server: Cannot bind student administration");
                    System.out.println("Server: RemoteException: " + ex.getMessage());
                }
                break;
            case "Chat":
            default:
                try {
                    chat = (IChat) new Chat();
                    System.out.println("Server: chatServer created");
                } catch (RemoteException ex) {
                    System.out.println("Server: Cannot create Chat");
                    System.out.println("Server: RemoteException: " + ex.getMessage());
                    chat = null;
                }

                // Create registry at port number
                try {
                    registry = LocateRegistry.createRegistry(port);
                    System.out.println("Server: Registry created on port number " + port);
                } catch (RemoteException ex) {
                    System.out.println("Server: Cannot create registry");
                    System.out.println("Server: RemoteException: " + ex.getMessage());
                    registry = null;
                }

                // Bind chat using registry
                try {
                    registry.rebind(bindingName, chat);
                } catch (RemoteException ex) {
                    System.out.println("Server: Cannot bind Chat");
                    System.out.println("Server: RemoteException: " + ex.getMessage());
                }
            break;
        }
    }

    // Print IP addresses and network interfaces
    private static void printIPAddresses() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: IP Address: " + localhost.getHostAddress());
            // Just in case this host has multiple IP addresses....
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null && allMyIps.length > 1) {
                System.out.println("Server: Full list of IP addresses:");
                for (InetAddress allMyIp : allMyIps) {
                    System.out.println("    " + allMyIp);
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server: Cannot get IP address of local host");
            System.out.println("Server: UnknownHostException: " + ex.getMessage());
        }

        try {
            System.out.println("Server: Full list of network interfaces:");
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                System.out.println("    " + intf.getName() + " " + intf.getDisplayName());
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    System.out.println("        " + enumIpAddr.nextElement().toString());
                }
            }
        } catch (SocketException ex) {
            System.out.println("Server: Cannot retrieve network interface list");
            System.out.println("Server: UnknownHostException: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Thread t = new Thread(new Sockets.SocketServer());
        t.start();
        
        // Welcome message
        System.out.println("SERVER USING REGISTRY");

        // Print IP addresses and network interfaces
        printIPAddresses();

        // Create server
        Server server = new Server();
        bindingName = "Admin";
        Server serverCommands = new Server(portsAndIps.ServerPort, "Administrator");
    }

    /**
     * Creates a new server
     * @param type type of server
     * @return port server is running on
     * @throws IOException 
     */
    public static int createNewServer(String type) throws IOException {
        int portnr = portsAndIps.getNewPort();
        Server server = new Server(portnr,type);
        return portnr;
    }


    /**
     * creates a new server
     * @param type type of server
     * @param bn bindingname 
     * @return port the server is running on
     * @throws IOException 
     */
    public static int createNewServer(String type,String bn) throws IOException {
        int portnr = portsAndIps.getNewPort();
        bindingName = bn;
        Server server = new Server(portnr,type);
        return portnr;
    }
    
    

}
