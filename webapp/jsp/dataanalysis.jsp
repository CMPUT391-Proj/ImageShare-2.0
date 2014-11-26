<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.oraclehandler.OracleHandler"%>
<%
	// check user is admin
	String username = (String) session.getAttribute("user");

%>

<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Data Analysis</h1>
			<p id="titleLeft">ImageShare Data Analytics</p>
		</div>
	</div>

	<%@include file="error.jsp" %>

	<div class="row"> <!-- start -->
		<div class="col-lg-6 col-lg-offset-3">

			<form class="form-horizontal" action="dataanalysisservlet" method="post" role="form">
				<div class="panel panel-default">
					<div class="panel-heading">Images Per User</div>
						<div class="panel-body">

						<div class="form-horizontal">
						<div class="form-group">
							<label for="date" class="col-sm-4 control-label">From Date:</label>
							<div class="col-sm-8">
								<input type="date" name="datefrom" class="form-control" placeholder="YYYY-MM-DD" id="date">
							</div>
						</div>
						<div class="form-group">
							<label for="date" class="col-sm-4 control-label">To Date:</label>
							<div class="col-sm-8">
								<input type="date" name="dateto" class="form-control" placeholder="YYYY-MM-DD" id="date">
							</div>
						</div>
						<input type="hidden" name="username" value=<% out.print(username); %>>
						<input type="hidden" name="searchtype" value="imagesperuser">
						<button type="submit" class="btn btn-primary pull-right">Update Page</button>
						</div>
						<button type="reset" class="btn btn-default pull-right">Reset Page</button>

					</div>
				</div>
			</form>

		</div>
	</div>


	<div class="row"> <!-- start -->
		<div class="col-lg-6 col-lg-offset-3">

			<form class="form-horizontal" action="dataanalysisservlet" method="post" role="form">
				<div class="panel panel-default">
					<div class="panel-heading">Images Per Subject</div>
						<div class="panel-body">

						<div class="form-horizontal">
						<div class="form-group">
							<label for="date" class="col-sm-4 control-label">From Date:</label>
							<div class="col-sm-8">
								<input type="date" name="datefrom" class="form-control" placeholder="YYYY-MM-DD" id="date">
							</div>
						</div>
						<div class="form-group">
							<label for="date" class="col-sm-4 control-label">To Date:</label>
							<div class="col-sm-8">
								<input type="date" name="dateto" class="form-control" placeholder="YYYY-MM-DD" id="date">
							</div>
						</div>
						<input type="hidden" name="username" value=<% out.print(username); %>>
						<input type="hidden" name="searchtype" value="imagespersubject">
						<button type="submit" class="btn btn-primary pull-right">Update Page</button>
						</div>
						<button type="reset" class="btn btn-default pull-right">Reset Page</button>

					</div>
				</div>
			</form>

		</div>
	</div>


	<div class="row"> <!-- start -->
		<div class="col-lg-6 col-lg-offset-3">

			<form class="form-horizontal" action="dataanalysisservlet" method="post" role="form">
				<div class="panel panel-default">
					<div class="panel-heading">Custom Parameter Search</div>
						<div class="panel-body">

						<div class="form-horizontal">
						<div class="form-group">
							<label for="date" class="col-sm-4 control-label">From Date:</label>
							<div class="col-sm-8">
								<input type="date" name="datefrom" class="form-control" placeholder="YYYY-MM-DD" id="date">
							</div>
						</div>
						<div class="form-group">
							<label for="date" class="col-sm-4 control-label">To Date:</label>
							<div class="col-sm-8">
								<input type="date" name="dateto" class="form-control" placeholder="YYYY-MM-DD" id="date">
							</div>
						</div>
						<div class="form-group">
							<label for="subjectlist" class="col-sm-4 control-label">Subject List (comma separated)</label>
							<div class="col-sm-8">
								<input type="text" name="subjectlist" class="form-control" placeholder="Subject List" id="subject">
							</div>
						</div>
						<div class="form-group">
							<label for="usernamelist" class="col-sm-4 control-label">Username List (comma separated)</label>
							<div class="col-sm-8">
								<input type="text" name="usernamelist" class="form-control" placeholder="Username List" id="subject">
							</div>
						</div>

						<input type="hidden" name="username" value=<% out.print(username); %>>
						<input type="hidden" name="searchtype" value="customsearch">
						<button type="submit" class="btn btn-primary pull-right">Update Page</button>
						</div>
						<button type="reset" class="btn btn-default pull-right">Reset Page</button>

					</div>
				</div>
			</form>

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