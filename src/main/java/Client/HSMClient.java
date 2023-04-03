package Client;

import Server.HsmService;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.lang3.tuple.ImmutablePair;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

/**
 *
 * @author kumarkhadka
 */
public class HSMClient {

    public static void main(String[] args)throws RemoteException, NotBoundException, MalformedURLException{
        try{
            HsmService service = (HsmService) Naming.lookup("rmi://localhost:5099/HsmRemoteServer");
            //service.sayHello("Dinesh");
            new HSMClient().userInteraction(service);
        }catch(RemoteException e){
           System.out.println("Client Side Exception!!: " + e.toString());
        }   
    }
    
    public void userInteraction(HsmService service){
        boolean exit = false;
        ArrayList<String> menu = new ArrayList<>();
        String username = "";
        String password = "";
        String jwt = "";
        menu.add("Press '1' TO LOGIN ");
        menu.add("Press '2' TO MONITOR INSULIN ");
        menu.add("Press '3' TO CALL AMBULANCE ");
        menu.add("Press '4' TO GIVE ACESS TO DOCTOR ");
        menu.add("Press '5' TO RECORD INSULIN DATA ");
        menu.add("Press '6' TO PRINT REPORT ");
        menu.add("Press '7' To EXIT");
        
       
        try{
            while(!exit){
                System.out.println("\n \n********* Choose From Options **********");
                System.out.println("----------------------------------------------");
                for (String item : menu) {
                    System.out.println("|  " + item + "  |");
                }
                System.out.println("----------------------------------------------");
                Scanner sc = new Scanner(System.in);
                System.out.println("Please Enter Your Choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice > 10 || choice < 1) {
                    System.out.println("\n\nPlease Choose From the Give Options !!");
                }else{
                    switch (choice) {
                        case 1 -> {
                            System.out.println("Username: ");
                            username = sc.nextLine();

                            System.out.println("Password: ");
                            password = sc.nextLine();
                        
                            ImmutablePair<String, String> pair = service.login(username, password);
                            jwt = pair.getLeft();
                            System.out.println(pair.getRight());
                        }
                        case 2 ->{
                            //monitor insuline
                        }
                        case 3 ->{
                            //call ambulance
                        }
                        case 4 ->{
                            //give docotor access
                        }
                        case 5 ->{
                            //record insuline
                        }
                        case 6 ->{
                            //print report
                        }
                        case 7 ->{
                            exit = true;
                        }
                    }
                    
                }
            }
        }catch(RemoteException e){
            
        }
    }
}
