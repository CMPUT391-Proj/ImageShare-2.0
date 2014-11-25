<!DOCTYPE html>
<html lang="en">

<%  
   String user = (String) session.getAttribute("user");
%>  

<body>
    <% if (user == null) 
          response.sendRedirect("index"); 
       else 
          response.sendRedirect("gallery");
    %>
</body>

</html>
