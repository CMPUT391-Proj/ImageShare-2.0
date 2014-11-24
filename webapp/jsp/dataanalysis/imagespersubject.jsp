<!DOCTYPE html>
<html lang="en">
<%@include file="../header.jsp" %>
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
	<%@include file="../navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Data Analysis</h1>
			<p id="titleLeft">ImageShare Data Analytics</p>
		</div>
	</div>

	<% if (error != null) out.println("<tr>" + error + "</tr>"); %>

	<div class="row"> <!-- start -->
		<div class="col-lg-2 col-lg-offset-4">
			<div class="btn-group-vertical" role="group">
				<button type="button" class="btn btn-default" id="button-imagesperuser">Images Per User</button>
				<button type="button" class="btn btn-default active" id="button-imagespersubject">Images Per Subject</button>
				<button type="button" class="btn btn-default" id="button-customanalysis">Custom Analysis</button>
			</div>
		</div>
		<div class="col-lg-3">
			<form class="form-horizontal" action="../dataanalysisservlet" method="post" role="form">
				<div class="form-group">
					<label for="date" class="col-sm-4 control-label">From Date:</label>
					<div class="col-sm-8">
						<input type="date" name="date-from" class="form-control" placeholder="YYYY-MM-DD" id="date">
					</div>
				</div>
				<div class="form-group">
					<label for="date" class="col-sm-4 control-label">To Date:</label>
					<div class="col-sm-8">
						<input type="date" name="date-to" class="form-control" placeholder="YYYY-MM-DD" id="date">
					</div>
				</div>
				<input type="hidden" name="username" value=<% out.print("'"+username+"'"); %>>
				<input type="hidden" name="page" value="imagespersubject">
				<button type="submit" class="btn btn-primary pull-right">Update Page</button>
			</form>
			<button id="reset" class="btn btn-default pull-right">Reset Page</button>
		</div>
	</div>

	<hr>

	<div class="row"> <!-- start -->
		<div class="container">
			<div class="col-lg-3">
				<div class="panel panel-default"> 
					<div class="panel-heading">Images Per User (All)</div>
					<table class="table table-hover table-bordered">
						<tbody id="image-per-subject">
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-lg-3">
				<div class="panel panel-default"> 
					<div class="panel-heading">Images Per User (Yearly)</div>
					<table class="table table-hover table-bordered">
						<tbody id="image-per-subject">
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-lg-3">
				<div class="panel panel-default"> 
					<div class="panel-heading">Images Per User (Monthly)</div>
					<table class="table table-hover table-bordered">
						<tbody id="image-per-subject">
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-lg-3">
				<div class="panel panel-default"> 
					<div class="panel-heading">Images Per User (Weekly)</div>
					<table class="table table-hover table-bordered">
						<tbody id="image-per-subject">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="../footer.jsp"%>
	</div>

	<script>
		$('#button-imagespersubject').click(function() {
			document.location.href = './imagespersubject';
		});
		$('#button-imagesperuser').click(function() {
			document.location.href = './imagesperuser';
		});
		$('#button-customanalysis').click(function() {
			document.location.href = './customanalysis';
		});

		$('#reset').click(function(){
			location.reload();
		});

		$(document).ready(function() {
			$('#user').val(<% out.print("\'"+username+"\'"); %>);

			var imagesPerUser = jQuery.parseJSON(<% out.print("\'"+imagesPerUser+"\'"); %>);
			var imagesPerUserTableData = parseJsonCount(imagesPerUser.result);

			var imagesPerSubject = jQuery.parseJSON(<% out.print("\'"+imagesPerSubject+"\'"); %>);
			var imagesPerSubjectTableData = parseJsonCount(imagesPerSubject.result);

			$('#image-per-user').append(imagesPerUserTableData.toString());
			$('#image-per-subject').append(imagesPerSubjectTableData.toString());
		});

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