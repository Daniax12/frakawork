/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import etu1757.framework.AnnotedClass;
import etu1757.framework.ModelView;

/**
 *
 * @author rango;
 */
public class Employe {
    private String idEmploye;
    private String nameEmploye;



    // Usefull function
    @AnnotedClass(methodName = "hello")
    public ModelView helloFunction(){
        ModelView result = new ModelView("hello.jsp");
        Employe e1 = new Employe("e1", "Jean");
        Employe e2 = new Employe("e2", "Jeanne");
        List<Employe> all = new ArrayList<>();
        all.add(e1); all.add(e2);

        result.addItem("liste", all);
        return result;
    }

    // Conctructors and Getters and setter
    public Employe(String idEmploye, String nameEmploye) {
        this.setIdEmploye(idEmploye);
        this.setNameEmploye(nameEmploye);
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

    public void setNameEmploye(String nameEmploye) {
        this.nameEmploye = nameEmploye;
    }
}
