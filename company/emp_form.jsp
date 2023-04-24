<%@page import="model.*"%>
<%@page import="java.util.*"%>

<%  
    Employe e = new Employe("e1", "jane", null); 
    List<Departement> departements = (List<Departement>) request.getAttribute("departments");
    String etat = (String) request.getAttribute("etat");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Insert | Employe </title>
        <style> 
            /* <%@include file="dist/css/bootstrap.min.css"%> */
        </style>
    </head>
    <body>
        <section class="vh-100 gradient-custom">
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-lg-12 col-xl-11">
                    <div class="card bg-light text-secondary" style="border-radius: 25px;">
                        <div class="card-body p-md-5">
                            <div class="row justify-content-center">
                                <div class="col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1"> 
                                    <p class="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">
                                        Register an employe
                                    </p>
                                    <% if(etat != null){
                                        if(etat.equals("0") == true){ %>
                                            <h6> Inserted </h6>
                                        <% } else if(etat.equals("1") == true){ %>
                                            <h5> Error </h5>
                                        <% }
                                    } %>
                                        
                                    <form action="/company/save_emp" class="mx-1 mx-md-4">

                                        <div class="d-flex flex-row align-items-center mb-2">
                                            <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                                            <div class="form-outline flex-fill mb-0">
                                                <input type="text" class="form-control" name = "nameEmploye" required/>
                                                <label class="form-label" >Name</label>
                                            </div>
                                        </div>

                                        <div class="d-flex flex-row align-items-center mb-2">
                                            <i class="fas fa-lock fa-lg me-3 fa-fw"></i>
                                            <div class="form-outline flex-fill mb-0">
                                                
                                                <select name="idDepartement" class="form-select" >
                                                    <% for (int i = 0; i < departements.size(); i++) { %>
                                                    <option value= <%= departements.get(i).getIdDepartement() %> >
                                                        <%= departements.get(i).getNameDepartement() %> 
                                                    </option>
                                                    <% } %>
                                                 
                                                </select>
                                                    <p> <label class="form-label" > Departement </label> </p>
                                            </div>
                                        </div>
                                                                  


                                        <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
                                            <button class="btn btn-primary btn-lg" style = "width:100%" type = "submit">
                                                Save employe
                                            </button>
                                        </div>
                                    </form>
                                    <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
                                        <a href="/company/emp_formulaire"">
                                            <button class="btn btn-primary btn-lg" style = "width:100%">Previous</button>
                                        </a>
                                    </div>

                                </div>

                                <div class="col-md-10 col-lg-6 col-xl-7 d-flex align-items-center order-1 order-lg-2">
                                    <img src="/company/img/emp.jpg"
                                    class="img-fluid" style="width:600px; height:400px; border-radius: 30px; margin-right:auto; margin-left:auto" alt="Sample image">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    </body>
</html>
