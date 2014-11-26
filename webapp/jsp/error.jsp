<style>
	#errBox {
		margin-left: auto;
    	margin-right: auto;
    	display: table;
	}
</style>

<div class="container">
<%
	String error = (String) session.getAttribute("error");
	session.setAttribute("error", null);
%>

<% if (error != null) 
	out.println("<div id=\"errBox\" class=\"row\">" 
		+ "<h3><span class=\"label label-danger\">"  
		+ error + "</span></h3></div><br>"); 
%>
</div>