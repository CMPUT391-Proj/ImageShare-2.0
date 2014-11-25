<style>
	#successBox {
		margin-left: auto;
    	margin-right: auto;
    	display: table;
	}
</style>

<div class="container">
<%
	String success = (String) session.getAttribute("success");
	session.setAttribute("success", null);
%>

<% if (success != null) 
	out.println("<div id=\"successBox\" class=\"row\">" 
		+ "<h3><span class=\"label label-success\">Success! "  
		+ success + "</span></h3></div><br>"); 
%>
</div>