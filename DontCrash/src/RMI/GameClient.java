/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import SharedInterfaces.IGame;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Bas
 */
public class GameClient {

    // Set binding name for student administration
    private String bindingName = "Game";

    // References to registry and student administration
    private Registry registry = null;
    private IGame game = null;

    private final String ipAddress;
    private final int portNumber;

    // Constructor
    /**
     * Constructor
     * @param ipAddress of the client
     * @param portNumber of the client
     */
    public GameClient(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }

        // Bind student administration using registry
        if (registry != null) {
            try {
                game = (IGame) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind chat");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                game = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind chat");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                game = null;
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                game = null;
            }
        }

        // Print result binding student administration
        if (game != null) {
            System.out.println("Client: chat bound");
        } else {
            System.out.println("Client: chat is null pointer");
        }
    }
    
    // Constructor
    /**
     * Constructor
     * @param ipAddress of the client
     * @param portNumber of the client
     * @param bindingname of the client
     */
    public GameClient(String ipAddress, int portNumber,String bindingname) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.bindingName = bindingname;
    }

    public IGame setUpChat() {
        return null;
        // Print IP address and port number for registry
        
    }
    
     public static void main(String[] args) {

        // Welcome message
        System.out.println("CLIENT USING REGISTRY");

        // Get ip address of server
        Scanner input = new Scanner(System.in);
        System.out.print("Client: Enter IP address of server: ");
        String ipAddress = input.nextLine();

        // Get port number
        System.out.print("Client: Enter port number: ");
        int portNumber = input.nextInt();

        // Create client
        GameClient client = new GameClient(ipAddress, portNumber);
    }
}
