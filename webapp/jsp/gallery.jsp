<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Group,imageshare.model.Image,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	String user = (String) session.getAttribute("user");
	String thumbnailURL = "thumbnail?";
	String imageURL = "image?";
  	String updateURL = "updateimage?";
	List<Image> popularImages = OracleHandler.getInstance().getTopFivePopularImages();
	List<Image> allImages = OracleHandler.getInstance().getAllImages(user);
%>
<style>
	#imageInfoLeft {
		text-align: left;
		margin: 0px;
		padding: 0px;
		float: left;
		vertical-align:top;
	}
	#imageInfoRight {
		text-align: right;
		margin: 0px;
		padding: 0px;
		float: right;
		vertical-align:top;
	}	
	#imageOwner {
		text-align: right;
		margin: 1px;
		padding: 1px;
	}
	#imagePlace {
		text-align: right;
		margin: 1px;
		padding: 1px;	
	}
	#imageInfoBottom {
		clear: both;
		text-align: left;
		padding-top: 5px;
	}
	#imageDate {
		text-align: right;
		margin: 1px;
		padding: 1px;
	}				
	#titleLeft {
		float: left;
	}
	#titleRight {
		float: right;
	}
	.text-muted {
	    margin-left: auto;
	    margin-right: auto;
	    display: table;
	}
</style>
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
		<!-- Image Modal -->
		<div class="modal fade" id="imageModal" tabindex="-1" role="dialog" aria-labelledby="imageModalTitle" aria-hidden="true">
			<div class="modal-dialog modal-dialog-center">
				<div class="modal-content">
			    	<div class="modal-body">
			    		<div id="imageView"></div>
			    	</div>
			    	<div class="modal-footer">
				    	<div id="imageInfoLeft">
							<h3 id="imageSubject"></h3>
							<h4 id="imagePermissions"></h4>
						</div>
						<div id="imageInfoRight">
							<h5 id="imageOwner"></h5>
							<h5 id="imagePlace"></h5>
							<h6 id="imageDate"></h6>
						</div>
						<div id="imageInfoBottom">
							<p id="imageDesc"></p>
						</div>
			    	</div>
			    </div>
			</div>
		</div>
	</div>	

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>

	<script>

		// Generate and append the popular thumbnails to the page
		var popular_thumbs = "";
		<% for (int i = 0; i < popularImages.size() && i < 5; ++i) { %>
			<% String getURL = thumbnailURL + popularImages.get(i).getPhotoId(); %>
			<% String editURL = updateURL + popularImages.get(i).getPhotoId(); %>

			popular_thumbs = popular_thumbs + '<div class=\'col-sm-3 col-xs-5 col-md-2 col-lg-2\'><div id="p<%=popularImages.get(i).getPhotoId()%>"><a class="thumbnail fancybox"><img class="img-responsive" alt="" src="<%=getURL%>"/></a></div>';

			<% if (popularImages.get(i).getOwnerName().equals(user)) { %> 
				popular_thumbs = popular_thumbs + '<a href="<%=editURL%>"><small class=\'text-muted\'>Edit</small></a>';
			<% } %>

			popular_thumbs = popular_thumbs + '</div>';
		<% } %>

		$('#popular').append(popular_thumbs);

		// Generate and append all user visible thumbnails to the page.
		var	thumbs = "";
		<% for (int i = 0; i < allImages.size(); ++i) { %>
			<% String getURL = thumbnailURL + allImages.get(i).getPhotoId(); %>
			<% String editURL = updateURL + allImages.get(i).getPhotoId(); %>
			
			thumbs = thumbs + '<div class=\'col-sm-3 col-xs-5 col-md-2 col-lg-2\'><div id="<%=allImages.get(i).getPhotoId()%>"><a class="thumbnail fancybox"><img class="img-responsive" alt="" src="<%=getURL%>"/></a></div>';

			<% if (allImages.get(i).getOwnerName().equals(user)) { %> 
				thumbs = thumbs + '<a href="<%=editURL%>"><small class=\'text-muted\'>Edit</small></a>';
			<% } %>

			thumbs = thumbs + '</div>';
		<% } %>
			
		$('#gal').append(thumbs);

		// Generate the modal for each of the popular thumbnails
		<% for (int i = 0; i < popularImages.size() && i < 5; ++i) { %>
			<%Image image = popularImages.get(i);%>

			var thumbnail = document.getElementById("p<%=image.getPhotoId()%>");
    		thumbnail.addEventListener("click", function (e) {

        		e.preventDefault();

        		<%String groupName = OracleHandler.getInstance().getGroupName(image.getPermitted());%>
				
				// Set image details
				document.getElementById("imageSubject").innerHTML = "Subject: <%=image.getSubject()%>";
				document.getElementById("imagePlace").innerHTML = "Location: <%=image.getPlace()%>";
				document.getElementById("imageOwner").innerHTML = "Owner: <%=image.getOwnerName()%>";
				document.getElementById("imageDate").innerHTML = "Date: <%=image.getDate().toString()%>";
				document.getElementById("imageDesc").innerHTML = "Description: <%=image.getDescription()%>";
				document.getElementById("imagePermissions").innerHTML = "Group: <%=groupName%>";

				<% String getURL = thumbnailURL + image.getPhotoId(); %>

				// Set Image
				$('#imageView').html('<img id="modalImage" src="<%=getURL%>">');

				// Resize modal based on image
				var imgWidth = document.getElementById("modalImage").naturalWidth;
    			$('#imageModal').find('.modal-dialog').css({width:imgWidth+45});

				// Show the modal
				$('#imageModal').modal('show');
    		});			
		<%} %>

		// Generate the modal for the remainder thumbnails
		<% for (int i = 0; i < allImages.size(); ++i) { %>
			<%Image image = allImages.get(i);%>

			var thumbnail = document.getElementById("<%=image.getPhotoId()%>");
    		thumbnail.addEventListener("click", function (e) {

        		e.preventDefault();

        		<%String groupName = OracleHandler.getInstance().getGroupName(image.getPermitted());%>
				
				// Set image details
				document.getElementById("imageSubject").innerHTML = "Subject: <%=image.getSubject()%>";
				document.getElementById("imagePlace").innerHTML = "Location: <%=image.getPlace()%>";
				document.getElementById("imageOwner").innerHTML = "Owner: <%=image.getOwnerName()%>";
				document.getElementById("imageDate").innerHTML = "Date: <%=image.getDate().toString()%>";
				document.getElementById("imageDesc").innerHTML = "Description: <%=image.getDescription()%>";
				document.getElementById("imagePermissions").innerHTML = "Group: <%=groupName%>";

				<% String getURL = thumbnailURL + image.getPhotoId(); %>

				// Set Image
				$('#imageView').html('<img id="modalImage" src="<%=getURL%>">');

				// Resize modal based on image
				var imgWidth = document.getElementById("modalImage").naturalWidth;
    			$('#imageModal').find('.modal-dialog').css({width:imgWidth+45});

				// Show the modal
				$('#imageModal').modal('show');
    		});			
		<%} %>
	</script>	
</body>
</html>