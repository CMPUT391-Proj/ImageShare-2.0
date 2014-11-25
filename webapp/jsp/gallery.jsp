<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Group,imageshare.model.Image,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	String user = (String) session.getAttribute("user");
	if (user == null) response.sendRedirect("index");
	List<Image> popularImages = OracleHandler.getInstance().getImagesByPopularity(user);
	int numPopularImages = OracleHandler.getInstance().getNumberOfPopularImages(user);
	List<Image> allImages = OracleHandler.getInstance().getAllImages(user);
%>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Gallery</h1>
			<p id="titleLeft">View all your photos here ... </p>
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

		// Generate and append the popular thumbnails to the page
		var popular_thumbs = "";
		<% for (int i = 0; i < popularImages.size() && i < numPopularImages; ++i) { %>
			<% String getURL = "thumbnail?" + popularImages.get(i).getPhotoId(); %>
			<% String editURL = "updateimage?" + popularImages.get(i).getPhotoId(); %>
			<% String displayURL = "display?" + popularImages.get(i).getPhotoId(); %>

			popular_thumbs = popular_thumbs + '<div class=\'col-sm-3 col-xs-5 col-md-2 col-lg-2\'><small class=\'text-muted\'><%=i+1%><br>hits: <%=popularImages.get(i).getHits()%></small>'

			<% if (popularImages.get(i).getOwnerName().equals(user)) { %> 
				popular_thumbs = popular_thumbs + '<br><a href="<%=editURL%>"><strong class=\'text-muted\'>Edit</strong></a>';
			<% } %>

			popular_thumbs = popular_thumbs + '<div id="p<%=popularImages.get(i).getPhotoId()%>"><a class="thumbnail fancybox" href="<%=displayURL%>"><img class="img-responsive" alt="" src="<%=getURL%>"/></a></div>';

			popular_thumbs = popular_thumbs + '</div>';
		<% } %>

		$('#popular').append(popular_thumbs);

		// Generate and append all user visible thumbnails to the page.
		var	thumbs = "";
		<% for (int i = 0; i < allImages.size(); ++i) { %>
			<% String getURL = "thumbnail?" + allImages.get(i).getPhotoId(); %>
			<% String editURL = "updateimage?" + allImages.get(i).getPhotoId(); %>
			<% String displayURL = "display?" + allImages.get(i).getPhotoId(); %>
			
			thumbs = thumbs + '<div class=\'col-sm-3 col-xs-5 col-md-2 col-lg-2\'>'

			<% if (allImages.get(i).getOwnerName().equals(user)) { %> 
				thumbs = thumbs + '<a href="<%=editURL%>"><strong class=\'text-muted\'>Edit</strong></a>';
			<% } %>

			thumbs = thumbs + '<div id="<%=allImages.get(i).getPhotoId()%>"><a class="thumbnail fancybox" href="<%=displayURL%>"><img class="img-responsive" alt="" src="<%=getURL%>"/></a></div>';

			thumbs = thumbs + '</div>';
		<% } %>
			
		$('#gal').append(thumbs);
	</script>	
</body>
</html>