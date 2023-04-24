/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1757.framework;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;  
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/**
 *
 * @author rango
 */
public class Utilities {
    
    // Get the modelview by a servletCOntext path
    public static ModelView getViewByPath(HttpServletRequest request, String servletContext, HashMap<String, Mapping> mapUrl) throws Exception{
        ModelView result = new ModelView("/");
        if(servletContext == null) return result;
        String[] split = servletContext.split("/");

        try {
            if(mapUrl.containsKey(split[1]) == true){
                String className = mapUrl.get(split[1]).getClassName();          // Get the name of the class
                String methode = mapUrl.get(split[1]).getMethod();               // Get the method
                Class<?> classType = Class.forName(className);                                  // Recast the name => Classe
                Object main_object = classType.getDeclaredConstructor().newInstance();                 // INstantiate the object

                Enumeration enumeration = request.getParameterNames();          // Get all parameters name send in the request
                HashMap<String, Object> data_input = new HashMap<>();
                int count = 0;
                while(enumeration.hasMoreElements()){
                    count++;
                    String parameterName = (String) enumeration.nextElement();           // Get the name of the parameters
                    data_input.put(parameterName, ((String)request.getParameter(parameterName)).trim());        // Get the value of the parameters and trim the result
                }
                if(count != 0){                                                                         // Verify if there is no request sent -> MUST BE CAREFULL WITH THAT
                    Field[] obj_field = classType.getDeclaredFields();                     // Get all field of the object
                    for(int i = 0; i < obj_field.length; i++){                          
                        if(data_input.containsKey(obj_field[i].getName()) == true){                     // Verify if the data_input contains field_name of the object as key from parameters name
                            Method methode_obj = getSetter(main_object, obj_field[i]);                  // If so, call the method setters
                            methode_obj.invoke(main_object, data_input.get(obj_field[i].getName()));    // Invoke the method
                        }
                    }
                }
                result = (ModelView) classType.getDeclaredMethod(methode).invoke(main_object);          // Get the modelView      
            }
            return result;  

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.toString());
        } 
    }


    // Get all classes annoted with a specific annotation in a package
    public static HashMap<String,Mapping> getAnnotatedMethods(String packageName, Class<? extends Annotation> annotationClass) throws Exception {
        try {
            List<Class<?>> classes = getClassesInPackage(packageName);
            HashMap<String,Mapping> annotatedMethods = new HashMap<String,Mapping>();
            for (Class<?> cls : classes) {
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods) {
                    Annotation annotation = method.getAnnotation(annotationClass);
                    if (annotation != null) {
                        annotatedMethods.put(((AnnotedClass) annotation).methodName(), new Mapping( cls.getName(), method.getName()));
                    }
                }
            }
            return annotatedMethods;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all methods annoted");
        }
        
    }
    
    // Get all classes in a specific package
    public static List<Class<?>> getClassesInPackage(String scannedPackage) throws Exception {
        
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = scannedPackage.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            List<Class<?>> classes = new ArrayList<>();
            for (File directory : dirs) {
                classes.addAll(findClassesInDirectory(directory, scannedPackage));
            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on gettimg classes in package");
        }
        
    }

    // Get all classes in a specific Directory
    private static List<Class<?>> findClassesInDirectory(File directory, String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        try {
            if (!directory.exists()) {
                return classes;
            }
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                }
            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting classes in a specific directory");
        }
    }


        /**
     * MAKE A GETTER FROM AN OBJECT WITH ITS SPECIFIC FIELD
     * @param obj
     * @param field
     * @return
     * @throws Exception 
     */
    public static Method getGetter(Object obj, Field field) throws Exception {
        try {
            String nameField = field.getName();
            return obj.getClass().getDeclaredMethod("get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the Getters of "+field.getName());
        }

    }
    
    /**
     * MAKE A SETTER FROM AN OBJECT WITH ITS SPECIFIC FIELD
     * @param object
     * @param field
     * @return
     * @throws NoSuchMethodException 
     */
    public static Method getSetter(Object object, Field field) throws Exception {
        
        try {
            String nameField = field.getName();
            String temp = "set" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
            return object.getClass().getDeclaredMethod(temp, field.getType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on setting the field object");
        }   
    }
}
    


