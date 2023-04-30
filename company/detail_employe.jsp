<%@page import="model.*"%>
<%@page import="java.util.*"%>
<%@page import="etu1757.framework.*"%>

<%  
    Employe employe = (Employe) request.getAttribute("emp");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Detail | Employe </title>
        <style> 
            /* <%@include file="dist/css/bootstrap.min.css"%> */
            /* <%@include file="dist/css/my.css"%> */
            .height-100{
                height:100vh;
            }

            .card{
                width:380px;
                border:none;
            }

            .dots {
                height: 20px;
                width: 20px;
                margin-top:4px;
                margin-left:4px;
                background-color:#dc3545;
                border-radius: 50%;
                border: 2px solid #fff;
                display:flex;
                align-items:center;
                justify-content:center;
                color:#fff;
                font-size:10px;
            }

            .list li{
                display:flex;
                align-items:center;
                justify-content:space-between;
                padding:13px;
                border-top:1px solid #eee;
                cursor:pointer;
            }

            .list li:hover{
                background-color:#dc3545;
                color:#fff;
            }
        </style>
    </head>
    <body>
        <div class="container height-100 d-flex justify-content-center align-items-center">  
            <div class="card text-center">
                <div class="py-4 p-2">           
                    <div>
                        <img src="" alt="NOt found" class="rounded" width="100">
                    </div>
                <div class="mt-3 d-flex flex-row justify-content-center">
                    <h5> <%= employe.getNameEmploye() %> </h5>
                </div>              
                <span> Departement  <%= employe.getIdDepartement() %> </span>                  
                </div>
                
                <div>
                    <ul class="list-unstyled list">
                        <li>
                            <span class="font-weight-bold"> Id </span>
                            <div>
                                <span class="mr-1"> <%= employe.getIdEmploye() %> </span>
                            </div>
                        </li>
                        
                        <li>
                            <span class="font-weight-bold"> Numero </span>
                            <div>
                                <span class="mr-1"><%= employe.getNumero() %></span>
                            </div>
                        </li>
                        
                        <li>
                            <span class="font-weight-bold"> Date d'embauche </span>
                            <div>
                                <span class="mr-1"><%= DateUtil.dateToString(employe.getDateEmbauche(), DatePattern.DD_MM_YYYY) %></span>
                            </div>
                        </li>
                    </ul>
                </div>   
            </div>
        </div>
    </body>
</html>
