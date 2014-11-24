<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Image,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	String user = (String) session.getAttribute("user");
	Integer photoId = Integer.parseInt(request.getQueryString());
	session.setAttribute("photoID", photoId);
	Image image = OracleHandler.getInstance().getImageById(photoId);
	String imageURL = "image?" + photoId;
	String error = (String) session.getAttribute("error");
	session.setAttribute("error", null);
%>
<style>
</style>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1><%=image.getSubject()%></h1>
			<p id="titleLeft"><%=image.getOwnerName()%></p>
		</div>
	</div>

	<div class="container">
		<!-- Image Modal -->
		<div class="row>">
    		<div id="imageView"><img src="<%=imageURL%>"></div>
    	</div>
		<div class="row">
			<h4 id="imagePermissions"><b>Permissions:</b> <%=OracleHandler.getInstance().getGroupName(image.getPermitted())%></h4>
		</div>
		<div class="row">
			<h4 id="imagePlace"><b>Location:</b> <%=image.getPlace()%></h4>
		</div>
		<div class="row">
			<h4 id="imageDate"><b>Date:</b> <%=image.getDate().toString()%></h4>
		</div>
		<div class="row">
			<h4 id="imageDesc"><b>Description:</b> <%=image.getDescription()%></h4>
		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>
</body>
</html>