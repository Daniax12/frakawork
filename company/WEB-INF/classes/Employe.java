/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.*;

import etu1757.framework.*;

/**
 *
 * @author rango;
 */
public class Employe {
    private String idEmploye;
    private String nameEmploye;
    private String idDepartement;


    // Usefull function
    // Saving an employe but also returning an MOdelView
    @AnnotedClass(methodName = "save_emp")
    public ModelView save_employe() throws Exception{
        ModelView result = new ModelView("emp_form.jsp");
        try {        
            String save = this.save_employe_database(null);
            result.addItem("etat", "0");            
        } catch (Exception e) {
            result.addItem("etat", "1");
        } 
        List<Departement> departments = Departement.allDepartementInDb(null);
        result.addItem("departments", departments);
        return result;
    }


    // Simple functio of saving an employe
    public String save_employe_database(Connection connection) throws Exception{
        boolean isOpen = false;
        if(connection == null){
            ConnectDatabase cb = new ConnectDatabase();
            connection = cb.dbConnect("postgres", "mdpProm15", "sprint_employe");
        } else {
            isOpen = true;
        }

        PreparedStatement stm = null;
        ResultSet resultSet = null;

        try {
            List<Departement> result = new ArrayList<>();
            stm = connection.prepareStatement("INSERT INTO employe VALUES('EMP'||nextval('emp_seq'), ?, ?) returning idemploye");
            stm.setString(1, this.getNameEmploye());
            stm.setString(2, this.getIdDepartement());

            resultSet = stm.executeQuery();
            String id = null;
            while(resultSet.next()){
                id = resultSet.getString("idemploye");
            }

            if(isOpen == false) connection.commit();
            return id;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on getting all departements on database");
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    // Get the form of the employe
    @AnnotedClass(methodName = "emp_formulaire")
    public ModelView myForm() throws Exception{
        try {
            ModelView result = new ModelView("emp_form.jsp");
            List<Departement> departments = Departement.allDepartementInDb(null);
            result.addItem("departments", departments);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on sending the modelview");
        }      
    }

    // Test of the model view
    @AnnotedClass(methodName = "hello")
    public ModelView helloFunction() throws Exception{
        try {
            ModelView result = new ModelView("hello.jsp");
            Employe e1 = new Employe("e1", "Giannis", null);
            Employe e2 = new Employe("e2", "Jeanne", null);
            List<Employe> all = new ArrayList<>();
            all.add(e1); all.add(e2);
    
            result.addItem("liste", all);
            return result;
        } catch (Exception e) {
            throw new Exception("Error on Hello Function");
        }
       
    }

    // Conctructors and Getters and setter
    public Employe(String idEmploye, String nameEmploye, String idDepartement) throws Exception{
        try {
            this.setIdEmploye(idEmploye);
            this.setNameEmploye(nameEmploye);
            this.setIdDepartement(idDepartement);
        } catch (Exception e) {
            throw new Exception("Error on constructing the employe");
        }
    }
    public Employe(){}

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getNameEmploye() {
        return nameEmploye;
    }

    public void setNameEmploye(String nameEmploye) throws Exception{
        if(nameEmploye.trim().equals("") == true) {
            throw new Exception("Name not rally defined");
        }
        this.nameEmploye = nameEmploye;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }    
}
