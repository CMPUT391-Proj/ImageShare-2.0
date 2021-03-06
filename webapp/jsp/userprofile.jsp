<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.User,imageshare.model.Person,imageshare.oraclehandler.OracleHandler"%>
<%
	String username = (String) session.getAttribute("user");

	if (username == null) { response.sendRedirect("index"); return; }

	User user = OracleHandler.getInstance().getUser(username);
	Person person = null;

	if (!username.equals("admin")) {
		person = OracleHandler.getInstance().getPerson(username);
	}
%>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>User Profile</h1>
			<p id="titleLeft">Your account information</p>
		</div>
	</div>
	
	<%@include file="error.jsp"%>
	<%@include file="success.jsp"%>

	<div class="row">
		<div class="col-lg-6 col-lg-offset-3">
			<!-- Registration Form -->
			<form class="form-horizontal" action="userprofileservlet" method="post" role="form">
				<div class="panel panel-default">
					<div class="panel-heading">Your personal information</div>
					<div class="panel-body">
						<div class="form-group">
							<label for="username" class="col-sm-3 control-label">Username</label>
							<div class="col-sm-7">
								<input type="text" name="username" class="form-control" id="username1" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="password" class="col-sm-3 control-label">Password</label>
							<div class="col-sm-7">
								<input type="password" name="password" class="form-control" id="password" readonly>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-block" id="edit-password" value="password">Edit</button>
							</div>
						</div>
						<div class="form-group">
							<label for="firstname" class="col-sm-3 control-label">First Name</label>
							<div class="col-sm-7">
								<input type="text" name="firstname" class="form-control" id="firstname" readonly>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-block" id="edit-firstname" value="firstname">Edit</button>
							</div>
						</div>
						<div class="form-group">
							<label for="lastname" class="col-sm-3 control-label">Last Name</label>
							<div class="col-sm-7">
								<input type="text" name="lastname" class="form-control" id="lastname" readonly>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-block" id="edit-lastname" value="lastname">Edit</button>
							</div>
						</div>
						<div class="form-group">
							<label for="address" class="col-sm-3 control-label">Address</label>
							<div class="col-sm-7">
								<input type="text" name="address" class="form-control" id="address" readonly>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-block" id="edit-address" value="address">Edit</button>
							</div>
						</div>
						<div class="form-group">
							<label for="email" class="col-sm-3 control-label">Email</label>
							<div class="col-sm-7">
								<input type="text" name="email" class="form-control" id="email" readonly>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-block" id="edit-email" value="email">Edit</button>
							</div>
						</div>
						<div class="form-group">
							<label for="phone" class="col-sm-3 control-label">Phone</label>
							<div class="col-sm-7">
								<input type="text" name="phone" class="form-control" id="phone" readonly>
							</div>
							<div class="col-sm-2">
								<button type="button" class="btn btn-block" id="edit-phone" value="phone">Edit</button>
							</div>
						</div>
					</div>
					<div class="panel-footer">
						<!-- Should probably pulled-right, but ruins formatting -->
						<button type="submit" class="btn btn-primary">Save changes</button>
					</div>
				</div>
			</form>

		</div>
	</div>

<div class="modal fade" id="edit-modal">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 id="modal-title" class="modal-title" value="Edit"></h4>
			</div>
			<div class="modal-body">
				<input type="text" id="modal-text" value="">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" id="modal-button1" value="false">Close</button>
				<button type="button" class="btn btn-primary" id="modal-button2" value="true">Save changes</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>

	<script>
		var id, edit_text;

		$('#modal-text').on('input', function() {
			edit_text = $(this).val();
		});

		$('#modal-button1,#modal-button2').on('click', function() {
			if ($(this).val() == 'true') {
				$(id).val(edit_text);
			}

			id='';
			edit_text = '';

			$('#modal-text').val('');
			$('#edit-modal').modal('hide');
		});

		$(document).ready(function() {
			var username = <% out.print("'"+user.getUsername()+"'"); %>;
			var password = <% out.print("'"+user.getPassword()+"'"); %>;
			var firstname = <% if (person != null) { out.print("'"+person.getFirstname()+"'"); } else { out.print("''"); } %>;
			var lastname = <% if (person != null) { out.print("'"+person.getLastname()+"'"); } else { out.print("''"); } %>;
			var address = <% if (person != null) { out.print("'"+person.getAddress()+"'"); } else { out.print("''"); } %>;
			var email = <% if (person != null) { out.print("'"+person.getEmail()+"'"); } else { out.print("''"); } %>;
			var phone = <% if (person != null) { out.print("'"+person.getPhone()+"'"); } else { out.print("''"); } %>;

			$('#username1').val(username);
			$('#password').val(password);
			$('#firstname').val(firstname);
			$('#lastname').val(lastname);
			$('#address').val(address);
			$('#email').val(email);
			$('#phone').val(phone);
		});

		$('#edit-password,#edit-firstname,#edit-lastname,#edit-address,#edit-email,#edit-phone').on('click', function() {
			id = '#'+$(this).val();
			$('#edit-modal').modal('show');
		});
	</script>
</body>
</html>
