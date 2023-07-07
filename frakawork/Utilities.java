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
import java.util.Collection;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

/**
 *
 * @author rango
 */
@MultipartConfig
public class Utilities {
    
    // Get the modelview by a servletCOntext path
    public static ModelView getViewByPath(HttpServletRequest request, String servletContext, HashMap<String, Mapping> mapUrl) throws Exception{
        
        ModelView result = new ModelView("/");
        if(servletContext == null) return result;
        String[] split = servletContext.split("/");

        try {
            if(mapUrl.containsKey(split[1]) == true){
                String className = mapUrl.get(split[1]).getClassName();                             // Get the name of the class
                String method_string = mapUrl.get(split[1]).getMethod();                            // Get the method
                Class<?> classType = Class.forName(className);                                      // Recast the name => Classe
                Object main_object = classType.getDeclaredConstructor().newInstance();              // INstantiate the object       -->> SINGLETON

                Field[] obj_field = classType.getDeclaredFields();                                  // Get all field of the object
                Field upload_trigg = FieldUtil.file_upload_field(obj_field);                        // TO know if there is upload_trigg

                Enumeration enumeration = request.getParameterNames();                              // Get all parameters name sent in the request
                Collection<Part> parts = null;
                System.out.println("THe method is "+request.getMethod());
                if(request.getMethod().equals("POST") == true){
                    parts = request.getParts();
                }

                HashMap<String, String> data_input = new HashMap<>();

                while(enumeration.hasMoreElements()){
                    String parameterName = (String) enumeration.nextElement();                      // Get the name of the parameters
                    //System.out.println("param is : "+ parameterName);
                    data_input.put(parameterName, ((String) request.getParameter(parameterName)).trim());        // Get the value of the parameters and trim the result       
                }
                
                Method method = null;
              //  System.out.println("Verificatio is "+ has_field_upload(parts, upload_trigg));
               

                main_object = FieldUtil.insertDataInObject(main_object, data_input, obj_field);     // Insert all fields data if some parameter name is same as object field name

                if(parts!= null && has_field_upload(parts, upload_trigg) == true){
                    //   System.out.println("I enter here");
                       treat_file_upload(main_object, upload_trigg, request);
                }

                Class[] parameter_class = FieldUtil.method_parameters_class(main_object, method_string);    // Get the method parameter class if it has
                if(parameter_class.length == 0) {   
                    method = classType.getDeclaredMethod(method_string);
                    result = (ModelView) method.invoke(main_object);                                    // -> No parameters: give the second parameters of INVOKE and GETDECLAREDMETHOD as null
                }else{
                    method = classType.getDeclaredMethod(method_string, parameter_class);               // -> GIve the second parameters 
                    Object[] parameters = FieldUtil.method_parameters_object(method, data_input);       // Get all value of all each parameter from data from input INTO Object array
                    result = (ModelView) method.invoke(main_object, parameters);    
                }                                
            }
            return result;  
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the modelView. Error:" + e.toString());
        } 
    }

    
    public static boolean has_field_upload(Collection<Part> parts, Field field){
        for (Part part : parts) {
            if (part.getName().equals(field.getName())) {
               return true;
            }
        }
        return false;
    }


    /**
     * Treat a file uploaded 
     * @param object : THe object that have the field FILEUPLOAD
     * @param field : The field
     * @param request 
     * @throws Exception
     */
    public static void treat_file_upload(Object object, Field field, HttpServletRequest request) throws Exception{
        try {
            Part mf = request.getPart("badge");       // Get the value from input file type
            System.out.println(mf.getSubmittedFileName());
            //System.out.println("byte is "+ new String(mf.getInputStream().readAllBytes()));
            FileUpload fileUpload = new FileUpload();
            fileUpload.setName(replace_characters(mf.getSubmittedFileName()));           // Get the file name 
            fileUpload.setFile(mf.getInputStream().readAllBytes());
            FieldUtil.getSetter(object, field).invoke(object, fileUpload);
            //System.out.println("After "+new String(fileUpload.getFile()));

        } catch (Exception e) {
            throw new Exception("Error on treating upload file with: "+e.getMessage());
        }
    }
    
    /**
     * Replace the non usual name_file characters 
     * @param name_file
     * @return
     */
    public static String replace_characters(String name_file){
        // Define the pattern for characters to be replaced
        String pattern = "[*/\\-+.;']";

        // Replace the characters using the pattern with underscores
        String replaced = name_file.replaceAll(pattern, "_");

        return replaced;
    }

    /**
     * Get all classes annoted with a specific annotation in a package
     * @param packageName 
     * @param annotationClass
     * @return
     * @throws Exception
     */    

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
    


