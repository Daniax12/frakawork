<%@page import="model.*"%>
<%@page import="java.util.*"%>

<% 
    String hell = (String) request.getAttribute("hell");
    List<Employe> all = (List<Employe>) request.getAttribute("liste");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>TODO supply a title</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>Hello, there are list of emp</div>
        <table>
            <thead>
                <td>Id</td>
                <td>name</td>
            </thead>
            <% for(int i = 0; i < all.size(); i++){ %>
                <tbody>
                    <td> <%= all.get(i).getIdEmploye() %> </td>
                    <td> <%= all.get(i).getNameEmploye() %> </td>
                </tbody>
            <% } %>
        </table>
    </body>
</html>
