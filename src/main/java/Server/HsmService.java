/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author kumarkhadka
 */
public interface HsmService extends Remote {
    /** Just to Test **/
    //void sayHello(String message) throws RemoteException;
    
    /** User Login
     * @param username
     * @param password
     * @return 
     * @throws java.rmi.RemoteException **/
    ImmutablePair<String, String>  login(String username, String password) throws RemoteException;
    
    /** For continuous glucose monitoring(CGM)  or insulin level
     * @param level
     * @return 
     * @throws java.rmi.RemoteException **/
    public String monitorInsulinLevel(int level) throws RemoteException;
    
    /** patient should be able to call an ambulance
     * @param isEmergency
     * @return 
     * @throws java.rmi.RemoteException **/
    public String callForAmbulance(String isEmergency) throws RemoteException;
    
    /** Patient can give access to doctor to check its insulin level data
     * @param grantAccess
     * @return 
     * @throws java.rmi.RemoteException **/
    public String giveAccessToDoctor(String grantAccess) throws RemoteException;
    
    /** Record the amount of insulin taken
     * @param isEmergency
     * @return 
     * @throws java.rmi.RemoteException **/
    public String recordInsulineTaken(String isEmergency) throws RemoteException;
    
    /** Print the report history
     * @param isEmergency
     * @return 
     * @throws java.rmi.RemoteException **/
    public String printPatientReport(String isEmergency) throws RemoteException;
}
