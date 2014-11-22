<!DOCTYPE html>
<%@ page import="model.Group,oraclehandler.OracleHandler,java.util.List"%>
<%
	String user = (String) session.getAttribute("user");
	List<Group> groups = OracleHandler.getInstance().getGroups(user);
%>
<html>
<head>
	<title>Image Upload</title>
</head>
<body>
	<form name="imageUpload" action="imageUpload" enctype="multipart/form-data" method="POST">
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
				<input name="date" type="date" placeholder="MM/DD/YYYY"></input>
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
