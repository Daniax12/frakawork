package etu1757.framework.servlet;

import etu1757.framework.AnnotedClass;
import etu1757.framework.Mapping;
import etu1757.framework.ModelView;
import etu1757.framework.Utilities;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rango
 */
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    String packageName;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            PrintWriter out = response.getWriter();
            String path = (String) request.getServletPath();
            String[] split = path.split("/"); 

            if(this.getMappingUrls().containsKey(split[1]) == true){
                String className = this.getMappingUrls().get(split[1]).getClassName();          // Get the name of the class
                String methode = this.getMappingUrls().get(split[1]).getMethod();               // Get the method
                Class<?> classType = Class.forName(className);                                  // Recast the name => Classe
                Object temp = classType.getDeclaredConstructor().newInstance();                 // INstantiate the object
                ModelView mv = (ModelView) classType.getDeclaredMethod(methode).invoke(temp);   // Get the modelView
                String view = mv.getVue();                                                      // Get the jsp page
                HashMap<String, Object> data = mv.getData();                                    // Get all data of the mv

                if(data != null){
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        out.println(key + " - "+ value);

                        request.setAttribute(key, value);
                    }
                }

                request.setAttribute("hell", "hell");

                RequestDispatcher dispat = request.getRequestDispatcher(view);
                dispat.forward(request, response);
            } else{
                RequestDispatcher dispat = request.getRequestDispatcher("/");
                dispat.forward(request, response);
            }
       
            // for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            //     String key = entry.getKey();
            //     Mapping value = entry.getValue();
            //     out.print("<p>");
            //     out.println("annotation = " + key + " / ");
            //     out.println("class Name = " + value.getClassName() + " / ");
            //     out.println("fonction Name = " + value.getMethod() + " / ");
            //     out.println("</p>");
            // }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void init() throws ServletException 
    {
        this.setPackageName(this.getInitParameter("toScan")); 
        try {
            this.setMappingUrls(Utilities.getAnnotatedMethods(this.getPackageName(), AnnotedClass.class));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }   
}
