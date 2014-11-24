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
	String imagesPerSubject = OracleHandler.getInstance().getImagesPerSubject();
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

	<div class="row">
		<div class="col-lg-5 col-lg-offset-1">
			<h4>ImageShare Overview</h4>
		</div>
	</div>

	<div class="row"> <!-- start -->
		<div class="col-lg-5 col-lg-offset-1">
			<div class="panel panel-default"> 
				<div class="panel-heading">Number of images per user</div>
				<table class="table table-hover table-bordered">
					<tbody id="image-per-user">
					</tbody>
				</table>
			</div>
		</div>

		<div class="col-lg-5">
			<div class="panel panel-default">
				<div class="panel-heading">Number of images per subject</div>
					<table class="table table-hover table-bordered">
						<tbody id="image-per-subject">
						</tbody>
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

			var imagesPerSubject = jQuery.parseJSON(<% out.print("\'"+imagesPerSubject+"\'"); %>);
			var imagesPerSubjectTableData = parseJsonCount(imagesPerSubject.result);

			$('#image-per-user').append(imagesPerUserTableData.toString());
			$('#image-per-subject').append(imagesPerSubjectTableData.toString());
		})

		function parseJsonCount(jsonList) {
			var tableData = '';
			
			for (var i=0; i<jsonList.length; i++) {
				tableData += '<tr>';
				
				for (var j=0; j< jsonList[i].length; j++) {
					if (jsonList[i][j].heading == 0) {
						tableData += '<td>'+jsonList[i][j].data+'</td>';
					}
					else {
						tableData += '<th>'+jsonList[i][j].data+'</th>';
					}
				}
				
				tableData += '</tr>\n';
			}

			return tableData;
		}

	</script>
</body>