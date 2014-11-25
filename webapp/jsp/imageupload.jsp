<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Group,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	String user = (String) session.getAttribute("user");
	if (user == null) response.sendRedirect("index");
	List<Group> groups = OracleHandler.getInstance().getInvolvedGroups(user);
%>
<style>
	#upload {
		float: right;
	}
	.panel-footer {
		min-height: 65px;
	}
</style>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Image Upload</h1>
			<p id="titleLeft">.jpg and .gif files are currently supported.</p>
		</div>
	</div>	

	<%@include file="error.jsp"%>
	<%@include file="success.jsp"%>

	<div class="row">
		<div class="col-lg-6 col-lg-offset-3">

		<!-- Image Upload Form -->
		<form class="form-horizontal" action="singleImageUpload" enctype="multipart/form-data" method="post" role="form">
			<div class="panel panel-default">
				<div class="panel-heading">Please fill in the image upload details.</div>
				<div class="panel-body">
					<div class="form-group">
						<label for="filepath" class="col-sm-3 control-label">File Path</label>
						<div class="col-sm-9">
							<input type="file" name="filepath" class="form-control" placeholder="filepath" id="filepath">
						</div>
					</div>
					<div class="form-group">
						<label for="date" class="col-sm-3 control-label">Date</label>
						<div class="col-sm-9">
							<input type="date" name="date" class="form-control" placeholder="YYYY-MM-DD" id="date">
						</div>
					</div>
					<div class="form-group">
						<label for="subject" class="col-sm-3 control-label">Subject</label>
						<div class="col-sm-9">
							<input type="text" name="subject" class="form-control" placeholder="Who / What is in this photo?" id="subject">
						</div>
					</div>
					<div class="form-group">
						<label for="location" class="col-sm-3 control-label">Location</label>
						<div class="col-sm-9">
							<input type="text" name="location" class="form-control" placeholder="Where was this photo taken?" id="location">
						</div>
					</div>
					<div class="form-group">
						<label for="description" class="col-sm-3 control-label">Description</label>
						<div class="col-sm-9">
							<input type="text" name="description" class="form-control" placeholder="Give some details about this image." id="description">
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-3 control-label">Permissions</label>
						<div class="col-xs-9">
							<div class="radio">
								<label>
									<input type="radio" name="permissions" value="1">public</input>
								</label>
							</div>
							<div class="radio">
								<label>
									<input type="radio" name="permissions" value="2" checked="checked">private</input>
								</label>
							</div>
							<% for (Group group : groups) {
								out.println("<div class='radio'><label><input type='radio' name='permissions' value='" + group.getGroupId() + "'>" + group.getGroupname() + "<small class='text-muted'> " + group.getUsername() + "</small></input></label></div>");
							} %>
						</div>
					</div>
				</div>
				<div class="panel-footer">
					<button id="upload" type="submit" class="btn btn-primary">Upload</button>
				</div>
			</div>
		</form>

		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
			<!--% include footer %-->
	</div>
</body>
</html>
