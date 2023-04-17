/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Model.InsulinProgress;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author kumarkhadka
 */
public class HsmServiceImplementation extends UnicastRemoteObject implements HsmService {

    public static Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public HsmServiceImplementation() throws RemoteException {
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
            query = "SELECT lg.username, lg.password, lg.salt, lg.type_name, ut.access_rights FROM db_if_hsm.login as lg "
                    + "inner join  db_if_hsm.user_type as ut  "
                    + "on ut.type_name = lg.type_name where lg.username='" + username + "'";
            PreparedStatement ps;
            ps = conn.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            int session = 20;//minute

            if (result.next()) {

                String userType = result.getString("type_name");

                if (password.equals(result.getString("password"))) {
                    String JWT = createJWT(username, session, userType);
                    String currentUser = "\n\n ---- User Information ---- \nUsername: "
                            + username.toUpperCase();
                    conn.close();
                    return new ImmutablePair<>(JWT, currentUser + "\n\n---- Session Information ---\n Session is Started for 15 Minutes for " + username.toUpperCase() + " ....");

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
    public String monitorInsulinLevel(String username, String JWT) throws RemoteException {
        Connection conn = getDatabaseConnection();
        Random r = new Random();
        String returnMessage = "";

        int low = 1;
        int high = 100;
        int insulinLevel = r.nextInt(high - low) + low;
        if (!decodeJWT(JWT, username, "check_insulin")) {
            return returnMessage;
        } else {

            try {
                //String query = "Insert into INSERT INTO db_if_hsm.insulin_progress(insulin_level,patient_name,data_time) values(?,?,?)";
                String query = " insert into db_if_hsm.insulin_progress (insulin_level, patient_name, data_time)"
                        + " values (?, ?, ?)";
//            Date date = new java.util.Date();
//            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//            java.sql.Timestamp sqlTime = new java.sql.Timestamp(date.getTime());

                Calendar calendar = Calendar.getInstance();
                java.sql.Date insulineDate = new java.sql.Date(calendar.getTime().getTime());

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, insulinLevel);
                ps.setString(2, username);
                ps.setDate(3, insulineDate);
                ps.executeUpdate();
                ps.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Insert Problem:" + e.toString());
            }
        }

        returnMessage = Integer.toString(insulinLevel);

        return returnMessage;
    }

    @Override
    public ArrayList<InsulinProgress> monitorInsulinProgress(String username, String JWT) throws RemoteException {
        String returnMessage = "";
        Connection conn = getDatabaseConnection();
        
        ArrayList<InsulinProgress> data = new ArrayList<>();
        if (!decodeJWT(JWT, username, "see_progress")) {
            return null;
        } else {

            try {
                if ("patient".equals(getUserType(username))) {
                    String query = "Select insulin_level, patient_name, data_time from db_if_hsm.insulin_progress";
                    PreparedStatement ps;
                    ps = conn.prepareStatement(query);
                    ResultSet result = ps.executeQuery();
                   

                    while (result.next()) {
                        
                        int insulin_level = result.getInt("insulin_level");

                        // creating sql date object
                        java.sql.Date sqlDate = result.getDate("data_time");

                        // creating util date object by passing gettime()
                        // method of sql date class
                        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                        // converting the util date into string format
                        final String stringDate = dateFormat.format(utilDate);
                        
                        InsulinProgress ip = new InsulinProgress();
                        ip.setInsulinLevel(insulin_level);
                        ip.setPatientName(username);
                        ip.setDate_time(utilDate);
                        data.add(ip);

                    } 
                }

            } catch (Exception e) {
                System.out.println("Server Side Message: User Management Error!" + e.toString());

            }
        }
        return data;
    }

    @Override
    public String callForAmbulance(String isEmergency, String JWT) throws RemoteException {
        return null;
    }

    @Override
    public String giveAccessToDoctor(String grantAccess, String JWT) throws RemoteException {
        return null;
    }

    @Override
    public String recordInsulineTaken(String isEmergency, String JWT) throws RemoteException {
        return null;
    }

    @Override
    public String printPatientReport(String isEmergency, String JWT) throws RemoteException {
        return null;
    }

    public boolean decodeJWT(String JWT, String username, String access_right) {
        boolean isValid = false;
        Date now = new Date();

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(JWT)
                    .getBody();

            if (claims.getExpiration().after(now)
                    && claims.getSubject().equals(username) && claims.containsKey("user_type")) {
                String user_type = claims.get("user_type", String.class);

                String access_rights = getAccessRights(user_type);

                List<String> splited_rights = Arrays.asList(access_rights.split(","));
                for (String right : splited_rights) {
                    right = right.toLowerCase();
                }
                isValid = splited_rights.contains(access_right);
            }

        } catch (Exception e) {
            System.out.println("Error in Token Validation!!" + e.toString());
            return false;
        }

        return isValid;

    }

    public String createJWT(String username, int session, String userType) {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, session);

        String JWT = Jwts.builder().setSubject(username)
                .claim("user_type", userType)
                .setIssuedAt(now)
                .setExpiration(cal.getTime())
                .signWith(signingKey)
                .compact();

        return JWT;
    }

    public String getUserType(String username) throws SQLException {
        String type;
        try ( Connection conn = getDatabaseConnection()) {
            String query = "SELECT type_name from db_if_hsm.login where username='" + username + "'";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            type = "";
            if (result.next()) {
                type = result.getString("type_name");
            }
        }
        return type;

    }

    public String getAccessRights(String user_type_name) throws SQLException {
        String access_rights;
        try ( Connection conn = getDatabaseConnection()) {
            String query = "SELECT access_rights from user_type where type_name='" + user_type_name + "'";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet result = ps.executeQuery();
            access_rights = "";
            if (result.next()) {
                access_rights = result.getString("access_rights");
            }
        }
        return access_rights;

    }

    public Connection getDatabaseConnection() {
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
