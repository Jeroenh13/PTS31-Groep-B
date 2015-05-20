/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import SharedInterfaces.IAdministator;
import SharedInterfaces.IChat;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Bas
 */
public class RMIClient {

    // Set binding name for student administration
    private String bindingName = "Chat";

    // References to registry and student administration
    private Registry registry = null;
    private IChat chat = null;
    private IAdministator admin = null;

    private final String ipAddress;
    private final int portNumber;

    // Constructor
    public RMIClient(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }
    // Constructor
    public RMIClient(String ipAddress, int portNumber,String bindingname) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.bindingName = bindingname;
    }

    public IChat setUpChat() {
        // Print IP address and port number for registry
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
                chat = (IChat) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind chat");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                chat = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind chat");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                chat = null;
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                chat = null;
            }
        }

        // Print result binding student administration
        if (chat != null) {
            System.out.println("Client: chat bound");
        } else {
            System.out.println("Client: chat is null pointer");
        }

        // Test RMI connection
        if (chat != null) {
            return chat;
        } else {
            return null;
        }
    }

    public IAdministator setUpNewAdministrator() {
         // Print IP address and port number for registry
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
                admin = (IAdministator) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind admin");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                admin = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind admin");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                admin = null;
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
                admin = null;
            }
        }

        // Print result binding student administration
        if (admin != null) {
            System.out.println("Client: admin bound");
        } else {
            System.out.println("Client: admin is null pointer");
        }

        // Test RMI connection
        if (admin != null) {
            return admin;
        } else {
            return null;
        }
    }
}
