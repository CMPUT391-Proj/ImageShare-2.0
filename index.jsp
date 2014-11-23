<!DOCTYPE html>
<html lang="en">

<%  
   String error = null;  
   try{  
      error = (String) session.getAttribute("err");  
   } catch(NullPointerException e) {
      e.printStackTrace();
   }
%>  

<jsp:include page="webapp/jsp/header.jsp"/>

<body>
    <% 
       if (error != null) {
       out.println(error);
       session.removeAttribute("err");
       }
       %>

    <jsp:include page="webapp/jsp/navbar.jsp"/>

    <div class="jumbotron">
	    <div class="container">
		    <h1>Welcome to ImageShare!</h1>
		    <p></p>
	    </div>
    </div>

    <jsp:include page="webapp/jsp/login.jsp"/>

    <div class="container">
	    <hr>
		    <jsp:include page="webapp/jsp/footer.jsp"/>
    </div>
</body>

</body>
</html>
