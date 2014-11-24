<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Group,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	String user = (String) session.getAttribute("username");
	List<Group> groups = OracleHandler.getInstance().getInvolvedGroups(user);
%>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Gallery</h1>
			<p id="titleLeft"></p>
		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>
</body>
</html>