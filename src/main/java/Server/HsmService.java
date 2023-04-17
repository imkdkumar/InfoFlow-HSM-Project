/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Server;

import Model.InsulinProgress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
     * @param username
     * @param JWT
     * @return 
     * @throws java.rmi.RemoteException **/
    public String monitorInsulinLevel(String username, String JWT) throws RemoteException;

    /**
     *
     * @param username
     * @param JWT
     * @return
     * @throws RemoteException
     */
    public ArrayList<InsulinProgress> monitorInsulinProgress(String username, String JWT) throws RemoteException;
    
    /** patient should be able to call an ambulance
     * @param isEmergency
     * @param JWT
     * @return 
     * @throws java.rmi.RemoteException **/
    public String callForAmbulance(String isEmergency, String JWT) throws RemoteException;
    
    /** Patient can give access to doctor to check its insulin level data
     * @param grantAccess
     * @param JWT
     * @return 
     * @throws java.rmi.RemoteException **/
    public String giveAccessToDoctor(String grantAccess, String JWT) throws RemoteException;
    
    /** Record the amount of insulin taken
     * @param isEmergency
     * @param JWT
     * @return 
     * @throws java.rmi.RemoteException **/
    public String recordInsulineTaken(String isEmergency, String JWT) throws RemoteException;
    
    /** Print the report history
     * @param isEmergency
     * @param JWT
     * @return 
     * @throws java.rmi.RemoteException **/
    public String printPatientReport(String isEmergency, String JWT) throws RemoteException;
}
