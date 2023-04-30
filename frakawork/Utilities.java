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
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                String className = mapUrl.get(split[1]).getClassName();                         // Get the name of the class
                String method_string = mapUrl.get(split[1]).getMethod();                              // Get the method
                Class<?> classType = Class.forName(className);                                  // Recast the name => Classe
                Object main_object = classType.getDeclaredConstructor().newInstance();          // INstantiate the object

                Enumeration enumeration = request.getParameterNames();                          // Get all parameters name sent in the request
                HashMap<String, String> data_input = new HashMap<>();
                while(enumeration.hasMoreElements()){
                    String parameterName = (String) enumeration.nextElement();                  // Get the name of the parameters
                    data_input.put(parameterName, ((String) request.getParameter(parameterName)).trim());        // Get the value of the parameters and trim the result
                }
                
                Method method = null;

                Field[] obj_field = classType.getDeclaredFields();                              // Get all field of the object
                main_object = FieldUtil.insertDataInObject(main_object, data_input, obj_field); // Insert all fields data if some parameter name is same as object field name

                Class[] parameter_class = FieldUtil.method_parameters_class(main_object, method_string);    // Get the method parameter class if it has
                if(parameter_class.length == 0) {   
                    method = classType.getDeclaredMethod(method_string);
                    result = (ModelView) method.invoke(main_object);                                    // -> No parameters: give the second parameters of INVOKE and GETDECLAREDMETHOD as null
                }else{
                    method = classType.getDeclaredMethod(method_string, parameter_class);               // -> GIve the second parameters 
                    Object[] parameters = FieldUtil.method_parameters_object(method, data_input);                 // Get all value of all each parameter from data from input INTO Object array
                    result = (ModelView) method.invoke(main_object, parameters);    
                }                                
            }
            return result;  
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the modelView. Error:" + e.toString());
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
            if (directory.exists() == false) {                                                                  // If the directory doesnt exist                  
                return classes;
            }
            File[] files = directory.listFiles();                                                               // Get all file in the directory
            for (File file : files) {
                if (file.isDirectory() == true) {                                                               // Verify again if file is a directory itself
                    assert !file.getName().contains(".");                                                     // Verify if the name doesnt contains a dot                                    
                    classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName()));           // Recurse the function to get all class
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);    // Delete the .class
                    classes.add(Class.forName(className));                                                      // Cast the class and insert it tn the result
                }
            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting classes in a specific directory");
        }
    }

}
    


