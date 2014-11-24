<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.oraclehandler.OracleHandler"%>
<%
	// check user is admin
	String username = (String) session.getAttribute("user");

	String error = (String) session.getAttribute("error");
	session.setAttribute("error", null);

	String imagesPerUser = OracleHandler.getInstance().getImagesPerUser();
%>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Data Analysis</h1>
			<p id="titleLeft">ImageShare Data Analytics</p>
		</div>
	</div>

	<% if (error != null) out.println("<tr>" + error + "</tr>"); %>


	<div class="row"> <!-- start -->
		<div class="col-lg-6 col-lg-offset-3">
			<div class="panel panel-default"> 
				<div class="panel-heading">Number of images per user</div>
					<table id="image-per-user" class="table table-hover table-bordered">
					</table>
				</div>
			</div>
		</div>
	</div><!-- end -->

	<div class="row"> <!-- start -->
		<div class="col-lg-6 col-lg-offset-3">
			<div class="panel panel-default">
				<div class="panel-heading">Number of images per subject</div>
					<table id="image-per-subject" class="table table-hover table-bordered">
					</table>
				</div>
			</div>
		</div>
	</div> <!-- end -->

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>

	<script>
		$(document).ready(function() {
			var imagesPerUser = jQuery.parseJSON(<% out.print("\'"+imagesPerUser+"\'"); %>);
			var imagesPerUserTableData = parseJsonCount(imagesPerUser.result);

			//var imagesPerSubject = jQuery.parseJSON();
			//var imagesPerSubjectTableData = parseJsonCount(imagesPerSubject.result);

			$('#image-per-user').append(imagesPerUserTableData.toString());
			$('#image-per-subject').append('<tr><th class="col-md-3">DATA 1</th><td>2</td></tr>');
		})

		function parseJsonCount(jsonList) {
			var tableData = '';
			
			for (var i=0; i<jsonList.length; i++) {
				tableData += '<tr><th class="col-md-3">'+jsonList[i].username+'</th><td>'+jsonList[i].count+'</td></tr>\n';
			}

			return tableData;
		}

	</script>
</body>