/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import etu1757.framework.AnnotedClass;

/**
 *
 * @author rango
 */

public class Etudiant {
    private String name;
    
    @AnnotedClass(methodName = "printing")
    public void printName(String name){
        System.out.println("name");
    }
    

    public void printName1(String name){
        System.out.println("name");
    }
    
     @AnnotedClass(methodName = "printing2")
    public void printName2(String name){
        System.out.println("name");
    }
    
}
