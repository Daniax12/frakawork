/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.beans.Encoder;
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
    private Integer numero;
    private java.util.Date dateEmbauche;
    private FileUpload badge;

    // Usefull function

    // Get the model view to show employe details(ModelaView with parameter)
    @AnnotedClass(methodName = "detail_emp")
    public ModelView detail_employe(String id) throws Exception{
        ModelView result = new ModelView("detail_employe.jsp");
        try {        
            Employe byId = Employe.employe_by_id(id, null);
            result.addItem("emp", byId);
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error", e.getMessage());
        } 
        return result;
    }

    // Get an employe by id
    public static Employe employe_by_id(String id, Connection connection) throws Exception{
        boolean isOpen = false;
        if(connection == null){
            ConnectDatabase cb = new ConnectDatabase();
            connection = cb.dbConnect("postgres", "mdpProm15", "sprint_employe");
        } else {
            isOpen = true;
        }

        PreparedStatement stm = null;
        ResultSet resultSet = null;
        Employe result = null;

        try {
            stm = connection.prepareStatement("SELECT * FROM employe WHERE idemploye = ?");
            stm.setString(1, id);

            resultSet = stm.executeQuery();

            while(resultSet.next()){
                String idEmp  = resultSet.getString("idemploye");
                String name = resultSet.getString("nameemploye");
                String iddepart = resultSet.getString("iddepartement");
                Integer num = resultSet.getInt("numero");
                java.sql.Date date = resultSet.getDate("dateembauche");

                java.util.Date theDate = new java.util.Date(date.getTime());
                result = new Employe(id, name, iddepart, num, theDate);
            }
            return result;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on getting employe by id: " + ex.getMessage());
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    // Saving an employe but also returning an MOdelView
    @AnnotedClass(methodName = "save_emp")
    public ModelView save_employe() throws Exception{
        ModelView result = new ModelView("emp_form.jsp");
        try {        
            String save = this.save_employe_database(null);
            result.addItem("etat", "0");            
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error on inserting with: ", e.getMessage());
        } 
        try {
            List<Departement> departments = Departement.allDepartementInDb(null);
            result.addItem("departments", departments); 
        } catch (Exception ex) {
            result.addItem("error :", ex.getMessage());
        }
        
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
            stm = connection.prepareStatement("INSERT INTO employe VALUES('EMP'||nextval('emp_seq'), ?, ?, ?, ?, ?) returning idemploye");
            stm.setString(1, this.getNameEmploye());
            stm.setString(2, this.getIdDepartement());
            stm.setInt(3, this.getNumero());
            java.sql.Date date = DateUtil.utilDateToSqlDate(java.sql.Date.class, this.getDateEmbauche());

            stm.setDate(4, date);
            stm.setBytes(5, this.getBadge().getFile());

            resultSet = stm.executeQuery();
            String id = null;
            while(resultSet.next()){
                id = resultSet.getString("idemploye");
            }

            if(isOpen == false) connection.commit();
            return id;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on saving employe: " + ex.getMessage() + " with name "+this.getNameEmploye());
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    // Get the form of the employe
    @AnnotedClass(methodName = "emp_formulaire")
    public ModelView myForm() throws Exception{
        ModelView result = new ModelView("emp_form.jsp");
        try {          
            List<Departement> departments = Departement.allDepartementInDb(null);
            result.addItem("departments", departments);

            return result;
        } catch (Exception e) {
            result.addItem("error on getting form with :", e.getMessage());
            e.printStackTrace();
        }      
        return result;
    }

    @AnnotedClass(methodName = "list_emp")
    public ModelView list_employe() throws Exception{
        ModelView result = new ModelView("all_employe.jsp");
        try {          
            List<Employe> all = Employe.findAll(null);
            result.addItem("employes", all);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.addItem("error", e.getMessage());
        }     
        return result;
    }

    // Get all employe in database
    public static List<Employe> findAll(Connection connection) throws Exception{
        boolean isOpen = false;
        if(connection == null){
            ConnectDatabase cb = new ConnectDatabase();
            connection = cb.dbConnect("postgres", "mdpProm15", "sprint_employe");
        } else {
            isOpen = true;
        }

        PreparedStatement stm = null;
        ResultSet resultSet = null;
        List<Employe> result = new ArrayList<>();
        try {
            stm = connection.prepareStatement("SELECT * FROM employe");

            resultSet = stm.executeQuery();

            while(resultSet.next()){
                Employe temp = null;
                String idEmp  = resultSet.getString("idemploye");
                String name = resultSet.getString("nameemploye");
                String iddepart = resultSet.getString("iddepartement");
                Integer num = resultSet.getInt("numero");
                java.sql.Date date = resultSet.getDate("dateembauche");

                java.util.Date theDate = new java.util.Date(date.getTime());
                temp = new Employe(idEmp, name, iddepart, num, theDate);
                result.add(temp);
            }
            return result;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on getting all employes: " + ex.getMessage());
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    // Test of the model view
    @AnnotedClass(methodName = "hello")
    public ModelView helloFunction() throws Exception{
        try {
            ModelView result = new ModelView("hello.jsp");
            Employe e1 = new Employe();
            Employe e2 = new Employe();
            List<Employe> all = new ArrayList<>();
            all.add(e1); all.add(e2);
    
            result.addItem("liste", all);
            return result;
        } catch (Exception e) {
            throw new Exception("Error on Hello Function");
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


    public Integer getNumero() {
        return numero;
    }


    public void setNumero(Integer numero) {
        this.numero = numero;
    }


    public java.util.Date getDateEmbauche() {
        return dateEmbauche;
    }


    public void setDateEmbauche(java.util.Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }    

    public Employe(String idEmploye, String nameEmploye, String idDepartement, Integer numero, java.util.Date dateEmbauche) throws Exception{
        try {
            this.setIdEmploye(idEmploye);
            this.setNameEmploye(nameEmploye);
            this.setIdDepartement(idDepartement);
            this.setNumero(numero);
            this.setDateEmbauche(dateEmbauche);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    public FileUpload getBadge() {
        return badge;
    }

    public void setBadge(FileUpload badge) {
        this.badge = badge;
    }
}
