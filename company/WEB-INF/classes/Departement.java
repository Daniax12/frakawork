/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import etu1757.framework.*;
/**
 *
 * @author rango;
 */
public class Departement {
    private String idDepartement;
    private String nameDepartement;
    private String location;

    // Get all Departememt in database
    public static List<Departement> allDepartementInDb(Connection connection) throws Exception{

        boolean isOpen = false;
        if(connection == null){
            ConnectDatabase cb = new ConnectDatabase();
            connection = cb.dbConnect("postgres", "mdpProm15", "sprint_employe");
        } else {
            isOpen = true;
        }

        java.sql.Statement stm = null;
        ResultSet rs = null;

        try {
            List<Departement> result = new ArrayList<>();
            String sql = "SELECT * FROM departement";
            stm = connection.createStatement();
            rs = stm.executeQuery(sql);

            while(rs.next()){
                Departement departement = new Departement(rs.getString("iddepartement"), rs.getString("namedepartement"),  rs.getString("location"));
                result.add(departement);
            }
            return result;
        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on getting all departements on database");
        } finally {
            stm.close();
            rs.close();
            if(isOpen == false) connection.close();
        }
    }

    // COnstructors and Getters and Setters

    public Departement(String idDepartement, String nameDepartement, String location) {
        this.idDepartement = idDepartement;
        this.nameDepartement = nameDepartement;
        this.location = location;
    }

    public Departement(){}

    public String getIdDepartement() {
        return idDepartement;
    }
    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }
    public String getNameDepartement() {
        return nameDepartement;
    }
    public void setNameDepartement(String nameDepartement) {
        this.nameDepartement = nameDepartement;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    
  
}
