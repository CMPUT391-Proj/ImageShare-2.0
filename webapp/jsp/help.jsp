<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Help Manual</h1>
		</div>
	</div>	

	<div class="container">
	    <h1>Table of Contents</h1>
	    <a href="#install">Installation</a><br>
	    <a href="#user">User Management</a><br>
	    <a href="#security">Security</a><br>
	    <a href="#upload">Photo Upload</a><br>
	    <a href="#display">Gallery</a><br>
	    <a href="#search">Search</a><br>
	    <a href="#data">Data Analysis</a><br>

	    <h1 id="install">Installation</h1>
	    <ol>
	    <li>Download the associated tar file.</li>
	    <li>Untar the file into your tomcat's catalina webapp directory.</li>
		<li>cd into the project's directory.</li>
		<li>execute: make.</li>
		<li>start tomcat.</li>
		<li>Open a recent version of firefox and navigate to the respective 
			URL for this project, as specified in your tomcat setup.</li>
		<li>You should now be at the login page.</li>
	    </ol>

	    <h1 id="user">User Management</h1>
	    <h3>Register</h3>
		  <p>To successfully register, you must have a unique username 
		  	and email. You will be notified of the error if you enter 
		  	invalid information. You must provide a username, email, and  
		  	a password. You are also required to fill in additional 
		  	details inluding first name, last name, address, and 
		  	phone number.</p>
		<h3 id="login">Login</h3>
		  <p>You must have a registered account in order to login.
		  	The username and password combination must be correct.
		  	You will be notified of the error if an incorrect 
		  	combination is provided.</p>
		<h3>Logout</h3>
		  <p>You are able to log out of the system by pressing the 
		  	right-hand icon on the navigation bar and choosing
		  	"Logout" once logged in.</p>

	    <h1 id="security">Security</h1>
	    <h3>Login</h3>
	      <p>See <a href="#login">User Management's section on Login</a></p>
	   	<h3>Manage Groups</h3> 
		  <p>This module can be accessed <a href="groupsview">here</a>
		  	once logged in or via the navigation bar > Groups.</p>
		  <p>To create a new group, enter a unique group name and click
		  	"Create a New Group". You should now see the new group you 
		  	created on the same page.</p>
		  <p>To delete a group, click "Delete Group" under the group 
		  	you are interested in deleting.</p>
		  <p>To add a member to the group, choose the member you wish to add
		  	in the drop down list and click "Add"</p>
		  <p>To delete a member in the group, choose the member you wish
		  	to delete in the drop down list and click "Delete"</p>
		<h3>Setting a Photo's Security</h3>
		  <p>See <a href="#imagepermissions">Photo Upload's section on 
		  Image Permissions</a> and <a href="#permissionupdate">
		  Gallery's section on Image Updating</a></p>

	    <h1 id="upload">Photo Upload</h1>
		  <p>This module can be accessed <a href="imageupload">here</a> 
		  	once logged in or via the navigation bar > Upload Image.</p>
		  <p>Select the images to be uploaded and then fill in the 
		  	information in the remainder of the form. At least one image file
		  	with the extension .jpg or .gif must be provided. All 
		  	other information is optional. If a date is not provided
		  	the date which the images are uploaded will be used. If not at
		  	least one image file is provided with the correct
		  	extension, you will be notified of the error.</p>
		<h3 id="imagepermissions">Image Permissions</h3>
		  <p>In <a href="#singleUpload">Photo Upload</a>, you will
		  	notice a field called Permissions. Here you are able to
		  	set which groups are able to view an image. If you don't
		  	change the selection, private is selected by default. This
		  	implies that only you are able to view the image. You are 
		  	also able to select public which is viewable by everyone.
		  	The remaining selections are the groups that you own.</p>
	    
	    <h1 id="display">Gallery</h1>
	    <h3>Viewing</h3>
		  <p>This module can be accessed <a href="gallery">here</a>  
		  	once logged in or via the navigation bar > Gallery.</p>
		  <p>By clicking on any of the thumbnails, you are able to
		  	see more information about that image, as well as see
		  	the full size of that image.</p>
		  <h4>Most Popular Images</h4>
		  <p>The most popular images display the thumbnails of the
		  	top five images, ranked in the order of decreasing hits. 
		  	If there are images with the same hit count, you will 
		  	notice there are more than five images being displayed; 
		  	all ties are displayed. There is also visible information here
		  	regarding the hit count of each image, as well as the 
		  	ability to <a href="#permissionupdate">modify image descriptors</a> using the "Edit" button above each thumbnail if you
		  	are the owner.</p>
		  <h4>All Images</h4>
		  <p>All Images displays all the images that the user can view.
		  	These images include public images, the users private images,
		  	and images belonging to groups which the user either owns or
		  	is a part of. You have the ability to <a href="#permissionupdate">modify the image descriptors</a> using the "Edit" button above each thumbnail if you are the owner.</p>
		<h3 href="permissionupdate">Update Image</h3>
		  <p>By clicking on the "Edit" button above each image thumbnail,
		  	you are able to access a page to modify the specific image
		  	details. Here you can update the subject, description,
		  	location, date, and permission of the image. Click "Update"
		  	to save these changes.</p>

	    <h1 id="search">Search</h1>
	    <p>TODO</p>

	    <h1 id="data">Data Analysis</h1>
	    <p>TODO</p>

	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>
</body>
</html>
