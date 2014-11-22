<!DOCTYPE html>
<%@ page import="imageshare.model.Group,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
	//String user = (String) session.getAttribute("user");
	String user = "admin";
	List<Group> groups = OracleHandler.getInstance().getGroups(user);
	String error = (String) session.getAttribute("error");
	session.setAttribute("error", null);
%>
<html>
<head>
	<title>Image Upload</title>
</head>
<body>
	<% if (error != null) out.println("<tr>" + error + "</tr>"); %>
	<form name="singleImageUpload" action="singleImageUpload" enctype="multipart/form-data" method="POST">
		<table>
		<tr>
			<th>File path: </th>
			<td>
				<input name="filepath" type="file" size="30" ></input>
			</td>
		</tr>
		<tr>
			<th>Date: </th>
			<td>
				<input name="date" type="date" placeholder="YYYY-MM-DD"></input>
			</td>
		</tr>
		<tr>
			<th>Subject: </th>
			<td>
				<input name="subject" type="text" placeholder="subject"></input>
			</td>
		</tr>		
		<tr>
			<th>Location: </th>
			<td>
				<input name="location" type="text" placeholder="location"></input>
			</td>
		</tr>
		<tr>
			<th>Description: </th>
			<td>
				<textarea name="description" cols="25" rows="5"
				placeholder="Description"></textarea>
			</td>
		</tr>
		<tr>
			<th>Permissions: </th>
			<td>
				<p><input type="radio" name="permissions" value="1">public</input></p>
				<p><input type="radio" name="permissions" value="2" checked="checked">private</input></p>
				<% for (Group group : groups) {
					out.println("<p><input type='radio' name='permissions' value='" + group.getGroupId() + "'>" + group.getGroupname() + "</input></p>");
				} %>
			</td>
		</tr>
		<tr>
			<td ALIGN=CENTER COLSPAN="2"><input type="submit" name=".submit"
				value="Upload">
			</td>
		</tr>
		</table>
	</form>
</body>
</html>
