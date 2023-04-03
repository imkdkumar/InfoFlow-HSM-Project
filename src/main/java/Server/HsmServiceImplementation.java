/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import io.jsonwebtoken.Jwts;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author kumarkhadka
 */
public class HsmServiceImplementation extends UnicastRemoteObject implements HsmService{
    
    public HsmServiceImplementation() throws RemoteException{
        super();
    }
//
//    @Override
//    public void sayHello(String message) {
//        System.out.println("Hello "+ message + " Welcome");
//    }

    @Override
    public ImmutablePair<String, String> login(String username, String password) throws RemoteException {
        Connection conn = getDatabaseConnection();
        String query;
        if (conn == null) {
            return new ImmutablePair<>("", "\n\n---- Error Information ---\nDatabase Connection Problem");

        }

        try {
            query = "SELECT username, password, salt, user_type_id, cpr_no from login where username= '" + username + "'";
            PreparedStatement ps;
            ps = conn.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            int session = 20;//minute

            if (result.next()) {
                
                if (password.equals(result.getString("password"))) {
                    String jwt = createJWT(username, session);
                    String currentUser = "\n\n ---- User Information ---- \nUsername: "
                            + username.toUpperCase();
                    conn.close();
                    return new ImmutablePair<>(jwt, currentUser + "\n\n---- Session Information ---\n Session is Started for 15 Minutes for " +username.toUpperCase() +" ....");

                } else {
                    System.out.println("Server Side Message: Password do not match");
                    return new ImmutablePair<>("", "\n\n---- Error Information ---\nPassword do not match!! Please Try Again.");
                }
            } else {
                return new ImmutablePair<>("", "\n\n---- Error Information ---\n Username do not Match!!");
            }

        } catch (Exception e) {
            System.out.println("Server Side Message: User Management Error!" + e.toString());

        }

        return null;
    }

    @Override
    public String monitorInsulinLevel(int level) throws RemoteException {
        return null;
    }

    @Override
    public String callForAmbulance(String isEmergency) throws RemoteException {
        return null;
    }

    @Override
    public String giveAccessToDoctor(String grantAccess) throws RemoteException {
        return null;
    }

    @Override
    public String recordInsulineTaken(String isEmergency) throws RemoteException {
        return null;
    }

    @Override
    public String printPatientReport(String isEmergency) throws RemoteException {
        return null;
    }
    
    public String createJWT(String username, int session) {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, session);

        String jwt = Jwts.builder().setSubject(username)
                .setIssuedAt(now)
                .setExpiration(cal.getTime())
                .compact();

        return jwt;
    }
    
    public Connection getDatabaseConnection(){
        Properties properties = new Properties();
        String path = System.getProperty("user.dir");
        path += "/src/db_cred.txt";
        //System.out.println(path)
        Connection conn = null;
        try ( FileInputStream fin = new FileInputStream(path);) {
            properties.load(fin);
            String username = properties.getProperty("db_user");
            String password = properties.getProperty("db_pass");
            String db_name = properties.getProperty("db");
            String url = properties.getProperty("db_url");
            url += db_name;
            conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                System.out.println("Database Connection Successful");
            }
        } catch (Exception ex) {
            System.out.println("Database connection failure: " + ex);
            ex.printStackTrace();
            return null;
        }
        return conn;
    }
}
