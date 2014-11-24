<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.oraclehandler.OracleHandler"%>
<%
	// check user is admin
	String username = (String) session.getAttribute("user");

	String error = (String) session.getAttribute("error");
	session.setAttribute("error", null);
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
		<div class="col-lg-2 col-lg-offset-4">
			<div class="btn-group-vertical" role="group">
				<button type="button" class="btn btn-default" id="button-imagesperuser">Images Per User</button>
				<button type="button" class="btn btn-default" id="button-imagespersubject">Images Per Subject</button>
				<button type="button" class="btn btn-default" id="button-customanalysis">Custom Analysis</button>
			</div>
		</div>
		<div class="col-lg-3">
			<div class="form-horizontal">
				<div class="form-group">
					<label for="date" class="col-sm-4 control-label">From Date:</label>
					<div class="col-sm-8">
						<input type="date" name="date-from" class="form-control" placeholder="YYYY-MM-DD" id="date" disabled>
					</div>
				</div>
				<div class="form-group">
					<label for="date" class="col-sm-4 control-label">To Date:</label>
					<div class="col-sm-8">
						<input type="date" name="date-to" class="form-control" placeholder="YYYY-MM-DD" id="date" disabled>
					</div>
				</div>
				<input type="hidden" name="user">
				<input type="hidden" name="page" value="imagespersubject">
				<button type="submit" class="btn btn-primary pull-right" disabled>Update Page</button>
			</div>
			<button id="reset" class="btn btn-default pull-right" disabled>Reset Page</button>
		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>

	<script>
		$('#button-imagespersubject').click(function() {
			document.location.href = './dataanalysis/imagespersubject';
		});
		$('#button-imagesperuser').click(function() {
			document.location.href = './dataanalysis/imagesperuser';
		});
		$('#button-customanalysis').click(function() {
			document.location.href = './customanalysis';
		});

		$(document).ready(function() {
			$('#user').val(<% out.print("\'"+username+"\'"); %>);
		});
	</script>
</body>