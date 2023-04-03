/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author kumarkhadka
 */
public class HsmServer {
    public static void main(String[] args) throws RemoteException {
        try{
            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("HsmRemoteServer", new HsmServiceImplementation());
            System.out.println("Server is Runnning");
        }catch(RemoteException e){
            System.out.println("Remote Server Exception: " + e.toString());
        }
    }
    
}
