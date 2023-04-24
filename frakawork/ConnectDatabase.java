/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1757.framework;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author rango
 */
public class ConnectDatabase {
   // private static final SGBD sgbd = SGBD.POSTGRESQL;       // BY default
    // private static final String nameDatabase = "sprint_employe";
    // private static final String user = "postgres";
    // private static final String mdp = "mdpProm15";
    
 /**
 *
 * Connection 
 * 
 * @param sgbd oracle or postgres
 * @param user 
 * @param mdp
 * @param nameDatabase 
 * @return
 * @throws Exception
 */
    public Connection dbConnect(String user, String mdp, String nameDatabase) throws Exception{
        Connection result = null;

        try {      
            Class.forName("org.postgresql.Driver");
            result = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+nameDatabase, user, mdp);
            
            result.setAutoCommit(false);
            // System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Connection failed");
        }
        return result;
    }
}
