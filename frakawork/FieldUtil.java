/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1757.framework;

import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 *
 * @author rango
 */
public class FieldUtil { 
    /**
     * INSERTING DATA FROM TABLE INTO AN OBJECT
     * @param <T>
     * @param object
     * @param fields all fields that have to be set
     * @param data HAshmaping a String -> Name Colonne and its string value from table / oldschool
     * @return
     * @throws Exception 
     */
    public static <T> T insertDataInObject(Object object, HashMap<String, String> data, Field[] fields) throws Exception{
        Class<T> c = (Class<T>) object.getClass();
        Constructor<T> constru = c.getConstructor();
        T result = constru.newInstance();
        
        try {
            for(Field field : fields){
                String getColumn = field.getName();           // Get the column name
                if(data.containsKey(getColumn) == true){

                    String valueField = data.get(getColumn);        // Get the value from hashmap
                
                    Class clazz = field.getType();                  // Get the type of the field
                    if(clazz == String.class){
                        getSetter(result, field).invoke(result, valueField);
                    } else if(clazz == Integer.class){
                        getSetter(result, field).invoke(result, Integer.valueOf(valueField));
                    } else if(clazz == Double.class){
                        getSetter(result, field).invoke(result, Double.valueOf(valueField));
                    } else if(clazz == Float.class){
                        getSetter(result, field).invoke(result, Float.valueOf(valueField));
                    } else if(clazz == Boolean.class){
                        getSetter(result, field).invoke(result, Boolean.valueOf(valueField));
                    } else if(clazz == Date.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        getSetter(result, field).invoke(result, date);
                    } else if(clazz == Calendar.class){
                        Calendar calendar = DateUtil.stringToCalendar(valueField, DatePattern.YYYY_MM_DD_hh_min_ss);
                        getSetter(result, field).invoke(result, calendar);
                    } else if(clazz == java.sql.Date.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        java.sql.Date theDate = DateUtil.utilDateToSqlDate(java.sql.Date.class, date);
                        getSetter(result, field).invoke(result, theDate);
                    } else if(clazz == java.sql.Timestamp.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        java.sql.Timestamp theDate = DateUtil.utilDateToSqlDate(java.sql.Timestamp.class, date);
                        getSetter(result, field).invoke(result, theDate);
                    } else if(clazz == java.sql.Time.class){
                        java.sql.Time time = DateUtil.stringToSqlTime(valueField);
                        getSetter(result, field).invoke(result, time);
                    }        
                }
            }
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error on inserting some data to "+object.getClass().getSimpleName());
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
