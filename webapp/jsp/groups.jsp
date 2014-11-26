<!DOCTYPE html>
<style>
@import "http://fonts.googleapis.com/css?family=Roboto:300,400,500,700";

.mb20 {
	margin-bottom: 20px;
}

hgroup {
	padding-left: 15px;
	border-bottom: 1px solid #ccc;
}

hgroup h1 {
	font: 500 normal 1.625em "Roboto", Arial, Verdana, sans-serif;
	color: #2a3644;
	margin-top: 0;
	line-height: 1.15;
}

hgroup h2.lead {
	font: normal normal 1.125em "Roboto", Arial, Verdana, sans-serif;
	color: #2a3644;
	margin: 0;
	padding-bottom: 10px;
}

.search-result .thumbnail {
	border-radius: 0 !important;
}

.search-result:first-child {
	margin-top: 0 !important;
}

.search-result {
	margin-top: 20px;
}

.search-result .col-md-2 {
	border-right: 1px dotted #ccc;
	min-height: 140px;
}

.search-result ul {
	padding-left: 0 !important;
	list-style: none;
}

.search-result ul li {
	font: 400 normal .85em "Roboto", Arial, Verdana, sans-serif;
	line-height: 30px;
}

.search-result ul li i {
	padding-right: 5px;
}

.search-result .col-md-7 {
	position: relative;
}

.search-result h3 {
	font: 500 normal 1.375em "Roboto", Arial, Verdana, sans-serif;
	margin-top: 0 !important;
	margin-bottom: 10px !important;
}

.search-result h3>a,.search-result i {
	color: #248dc1 !important;
}

.search-result p {
	font: normal normal 1.125em "Roboto", Arial, Verdana, sans-serif;
}

.search-result span.plus {
	position: absolute;
	right: 0;
	top: 126px;
}

.search-result span.plus a {
	background-color: #248dc1;
	padding: 5px 5px 3px 5px;
}

.search-result span.plus a:hover {
	background-color: #414141;
}

.search-result span.plus a i {
	color: #fff !important;
}

.search-result span.border {
	display: block;
	width: 97%;
	margin: 0 15px;
	border-bottom: 1px dotted #ccc;
}
</style>

<html lang="en">
<%@include file="header.jsp"%>
<%@include file="redirect.jsp" %>
<body>
	<%@include file="navbar.jsp"%>

	<div class="jumbotron">
		<div class="container">
			<h1>Groups</h1>
			<p id="titleLeft">Share images with groups.</p>
		</div>
	</div>

	<%@include file="error.jsp" %>

	<div class="container">

		<form class='well' action='groups' method='post' role='form'>
			<div class='row'>
				<div class='col-md-7 col-md-offset-3'>
					<div class='form-group'>
						<div class="form-group">
							<label for="groupname" class="col-sm-3 control-label">Enter
								a name for your new group: </label>
							<div class="col-sm-9">
								<input type="text" name="groupname" class="form-control"
									placeholder="Group Name" id="groupname">
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class='span7 text-center'>
				<div class='btn-group'>
					<button id='submitGrp' name='submitGrp' type='submit' class='btn btn-primary'>Create
						a New Group</button>
				</div>
			</div>
		</form>

		<hgroup class="mb20">
			<h2 class="lead">
				You are an owner of <strong class="text-danger"><%= (String)request.getSession(false).getAttribute("groupcount") %></strong>
				group(s)
			</h2>
		</hgroup>

		<section class="col-xs-12 col-sm-6 col-md-12">


			<%= (String) request.getSession(false).getAttribute("groupBodyHTML") %>

		</section>
	</div>

	<div class="container">
		<hr>
		<%@include file="footer.jsp"%>
	</div>
</body>
</html>


