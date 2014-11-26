 <%
 String user = (String) session.getAttribute("user");
 if (user == null) { 
 	response.sendRedirect("index"); 
 	return; 
 }
 %>