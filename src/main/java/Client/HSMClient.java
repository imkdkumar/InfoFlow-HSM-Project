package Client;

import Model.InsulinProgress;
import Server.HsmService;
import Server.HsmServiceImplementation;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        try {
            HsmService service = (HsmService) Naming.lookup("rmi://localhost:5099/HsmRemoteServer");
            //service.sayHello("Dinesh");
            new HSMClient().userInteraction(service);
        } catch (RemoteException e) {
            System.out.println("Client Side Exception!!: " + e.toString());
        }
    }

    public void userInteraction(HsmService service) {
        boolean exit = false;
        ArrayList<String> menu = new ArrayList<>();
        String username = "";
        String password = "";
        String JWT = "";
        menu.add("Press '1' TO LOGIN ");
        menu.add("Press '2' TO MONITOR INSULIN LEVEL ");
        menu.add("Press '3' TO MONITOR INSULIN PROGRESS");
        menu.add("Press '4' TO CALL AMBULANCE ");
        menu.add("Press '5' TO GIVE ACESS TO DOCTOR ");
        menu.add("Press '6' TO RECORD INSULIN DATA ");
        menu.add("Press '7' TO PRINT REPORT ");
        menu.add("Press '8' To EXIT");

        try {
            while (!exit) {
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
                } else {
                    switch (choice) {
                        case 1 -> {
                            System.out.println("Username: ");
                            username = sc.nextLine();

                            System.out.println("Password: ");
                            password = sc.nextLine();

                            ImmutablePair<String, String> pair = service.login(username, password);
                            JWT = pair.getLeft();
                            System.out.println(pair.getRight());
                        }
                        case 2 -> {

                            String insulinLevel = service.monitorInsulinLevel(username, JWT);
                            if ("".equals(insulinLevel)) {
                                System.out.println("\n\n-------Insulin Check Information-------\nError!! -  Access denied");
                            } else {
                                System.out.println("\n\n----Insulin Check Information ---\nInsulin Check is in progress.........");
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(HsmServiceImplementation.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                System.out.println("\n\nYour Insulin Level: " + insulinLevel + " μU/mL");
                                int tempInsulinLevel = Integer.parseInt(insulinLevel);
                                if (tempInsulinLevel <= 4 || tempInsulinLevel > 16) {
                                    System.out.println("\n\nYour Insulin Level is Critical !! Please Call Ambulance.");
                                } else {
                                    System.out.println("\n\nCongratulations!! Your Insulin Level is Normal");
                                }
                                System.out.println("\n\n--------* Insulin Check is Done *--------");
                            }

                        }
                        case 3 -> {
                            //Monitor progress
                            ArrayList<InsulinProgress> insulinLevel = service.monitorInsulinProgress(username, JWT);
                            System.out.println("\n\n---------- Insulin Progress Information ----------\n");
                                System.out.println("\n----------------------------------------------------------------------");
                                System.out.println("|Patient Name      | Insulin Level      | Time      |");
                                System.out.println("------------------------------------------------------------------------");
                            for(InsulinProgress i : insulinLevel){
                                
                            
                                System.out.println("\n"+ i.getPatientName() +"                    "+ i.getInsulinLevel() +"              "+i.getDate_time()+"     ");
                            }
                        }
                        case 4 -> {
                            //call ambulance
                        }
                        case 5 -> {
                            //give docotor access
                        }
                        case 6 -> {
                            //record insuline
                        }
                        case 7 -> {
                            //print report
                        }
                        case 8 -> {
                            exit = true;
                        }
                    }

                }
            }
        } catch (RemoteException e) {
            System.out.println(e.toString());

        }
    }
}
