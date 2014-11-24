<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Group,imageshare.model.Image,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	String user = (String) session.getAttribute("username");
	String thumbnailURL = request.getRequestURL().toString();
  	thumbnailURL =  "thumbnail?";
	List<Group> groups = OracleHandler.getInstance().getInvolvedGroups(user);
	List<Image> popularImages = OracleHandler.getInstance().getTopFivePopularImages();
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
		<div class="row">
			<h1>Most Popular Images</h1>
		</div>
		<div class="row">
			<div id="popular" class='list-group gallery'></div>
		</div>
		<div class="row">
			<h1>All Images</h1>
		</div>
		<div class="row">
			<div id="gal" class='list-group gallery'></div> 
		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>

	<script>
		var thumbnail_holders = "";
		<% int count = 0; %>
		for (i = 0; i < <%=popularImages.size() %> && i < 5; ++i) {
			<% String getURL = thumbnailURL + popularImages.get(count).getPhotoId(); %>

			thumbnail_holders = thumbnail_holders + '<div id="thumbnail_' + i +  '" class=\'col-sm-3 col-xs-5 col-md-2 col-lg-2\'><a class="thumbnail fancybox"><img class="img-responsive" alt="" src="<%=getURL%>"/></a></div>'
			<% count++; %>
		}

		$('#popular').append(thumbnail_holders);
	</script>	
</body>
</html>