<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%
	String error = (String) session.getAttribute("error");
	session.setAttribute("error", null);
%>
<body>
	<div class="jumbotron">
		<div class="container">
			<h1>ImageShare</h1>
		</div>
	</div>

	<% if (error != null) out.println("<tr>" + error + "</tr>"); %>

	<div class="row">
		<div class="col-md-6  col-lg-offset-3">
			<div class="panel panel-default">
				<div class="panel-heading">
					<span class="glyphicon glyphicon-lock"></span> Login</div>
					<div class="panel-body">
						<form class="form-horizontal" role="form" id="login" action="loginservlet" method="post">
							<div class="form-group">
								<label for="username" class="col-sm-3 control-label">Username</label>
								<div class="col-sm-9">
									<input type="username" class="form-control" id="inputUsername" name="username" placeholder="Username" required>
								</div>
							</div>
							<div class="form-group">
								<label for="password" class="col-sm-3 control-label">Password</label>
								<div class="col-sm-9">
									<input type="password" class="form-control" id="inputPassword" name="password" placeholder="Password" required>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-9" id = "invalidDesc">
								</div>
							</div>
							<div class="form-group last">
								<div class="col-sm-offset-3 col-sm-9">
									<button type="submit" class="btn btn-success btn-sm" id = "loginButton">Sign in</button>
								</div>
							</div>
						</form>
					</div>
					<div class="panel-footer" >
						Not Registered? <a href="registration">Register here</a>
					</div>
			</div>
		</div>
	</div>
	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
			<!--% include footer %-->
	</div>
</body>
</html>