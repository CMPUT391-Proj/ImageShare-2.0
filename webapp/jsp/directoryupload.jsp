<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.model.Group,imageshare.oraclehandler.OracleHandler,java.util.List,java.util.ArrayList"%>
<%
  String user = (String) session.getAttribute("user");
  if (user == null) response.sendRedirect("index");
  List<Group> groups = OracleHandler.getInstance().getInvolvedGroups(user);
  String uploadURL = request.getRequestURL().toString();
  uploadURL = uploadURL.substring(0, uploadURL.lastIndexOf("/")) + "/directoryUpload";
%>
<style>
  #upload {
    float: right;
  }
  .panel-footer {
    min-height: 65px;
  }
  #uploadApplet {
    margin-left: auto;
    margin-right: auto;
    display: table;
  }
</style>
<body>
  <%@include file="navbar.jsp" %>

  <div class="jumbotron">
    <div class="container">
      <h1>Directory Upload</h1>
      <p id="titleLeft">Choose a directory to upload multiple files here.</p>
    </div>
  </div>  

  <%@include file="error.jsp"%>

  <div class="row">
    <div class="col-lg-6 col-lg-offset-3">

    <!-- Image Upload Form -->
    <form class="form-horizontal" action="directoryUploadDetails" enctype="multipart/form-data" method="post" role="form">

      <div class="panel panel-default">
        <div class="panel-heading">Please fill in the directory upload details.</div>
        <div class="panel-body">

          <div class="form-group" id="uploadApplet">
            <applet code="applet-basic_files/wjhk.JUploadApplet" name="JUpload" archive="applet-basic_files/wjhk.jar" mayscript="" height="300" width="640">
            <param name="CODE" value="wjhk.jupload2.JUploadApplet">
            <param name="ARCHIVE" value="wjhk.jupload.jar">
            <param name="type" value="application/x-java-applet;version=1.4">
            <param name="scriptable" value="false">    
            <param name="postURL" value=<%=uploadURL%>>
            <param name="nbFilesPerRequest" value="2">    
              Java 1.4 or higher plugin required.
            </applet>
          </div>

          <div class="form-group">
            <label for="date" class="col-sm-3 control-label">Date</label>
            <div class="col-sm-9">
              <input type="date" name="date" class="form-control" placeholder="YYYY-MM-DD" id="date">
            </div>
          </div>
          <div class="form-group">
            <label for="subject" class="col-sm-3 control-label">Subject</label>
            <div class="col-sm-9">
              <input type="text" name="subject" class="form-control" placeholder="Who / What are in these photos?" id="subject">
            </div>
          </div>
          <div class="form-group">
            <label for="location" class="col-sm-3 control-label">Location</label>
            <div class="col-sm-9">
              <input type="text" name="location" class="form-control" placeholder="Where were these photos taken?" id="location">
            </div>
          </div>
          <div class="form-group">
            <label for="description" class="col-sm-3 control-label">Description</label>
            <div class="col-sm-9">
              <input type="text" name="description" class="form-control" placeholder="Give some details about these images." id="description">
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">Permissions</label>
            <div class="col-xs-9">
              <div class="radio">
                <label>
                  <input type="radio" name="permissions" value="1">public</input>
                </label>
              </div>
              <div class="radio">
                <label>
                  <input type="radio" name="permissions" value="2" checked="checked">private</input>
                </label>
              </div>
              <% for (Group group : groups) {
                out.println("<div class='radio'><label><input type='radio' name='permissions' value='" + group.getGroupId() + "'>" + group.getGroupname() + "<small class='text-muted'> " + group.getUsername() + "</small></input></label></div>");
              } %>
            </div>
          </div>
        </div>
        <div class="panel-footer">
          <button id="upload" type="submit" class="btn btn-primary">Submit</button>
        </div>
      </div>
    </form>

    </div>
  </div>

  <div class="container">
    <hr>
      <%@include file="footer.jsp"%>
      <!--% include footer %-->
  </div>
</body>
</html>