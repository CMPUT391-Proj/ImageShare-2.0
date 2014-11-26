<% String navbarUser = (String) session.getAttribute("user"); %>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target=".navbar-collapse">
                <span class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="gallery"><i class="glyphicon glyphicon-home white"></i> ImageShare</a>
        </div>

        <% if (navbarUser != null) { %>

        <ul class="nav navbar-nav">
            <li><a href="imageupload">Upload</a></li>
            <li><a href="groups">Groups</a></li>
            <li><a href="search">Search</a></li>
            <li><a href="dataanalysis">Statistics</a></li>
        </ul>

        <div class="navbar-collapse collapse pull-right">
        <ul class="nav navbar-nav pull-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="dropdown">
              <span class="glyphicon glyphicon-list pull-right"></span></a>
              <ul class="dropdown-menu">
                    <li><a href="gallery"><span class="glyphicon glyphicon-camera pull-right"></span>Gallery</a></li>
                    <li class="divider"></li>
                    <li><a href="imageupload"><span class="glyphicon glyphicon-picture pull-right"></span>Upload Image</a></li>
                    <li class="divider"></li>
                    <li><a href="directoryupload"><span class="glyphicon glyphicon-folder-open pull-right"></span>Upload Folder</a></li>
                    <li class="divider"></li>
                    <li><a href="search"><span class="glyphicon glyphicon-search pull-right"></span>Search</a></li>
                    <li class="divider"></li>
                    <li><a href="groups"><span class="glyphicon glyphicon-user pull-right"></span>Groups</a></li>
                    <li class="divider"></li>
                    <li><a href="dataanalysis"><span class="glyphicon glyphicon-stats pull-right"></span>User Stats</a></li>
                    <li class="divider"></li>
                    <li><a href="help"><span class="glyphicon glyphicon-wrench pull-right"></span>Help</a></li>
                    <li class="divider"></li>
                    <li><a href="logout"><span class="glyphicon glyphicon-log-out pull-right"></span>Sign Out</a></li>
              </ul>
            </li>
        </ul>
        </div>

        <ul class="nav navbar-nav pull-right">
            <li><p class="navbar-text" id='username'><a href="userprofile"><% out.println(navbarUser); %></a></p></li>
        </ul>

        <% } %>

    </div>
</div>
