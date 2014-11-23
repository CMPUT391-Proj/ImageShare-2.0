<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target=".navbar-collapse">
                <span class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/ImageShare-2.0/">ImageShare</a>
        </div>

        <ul class="nav navbar-nav">
            <li class="divider-vertical"></li>
            <li><a href="/ImageShare-2.0/"><i class="glyphicon glyphicon-home white"></i> Home</a></li>
        </ul>

        <div class="navbar-collapse collapse pull-right">
        <ul class="nav navbar-nav pull-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="dropdown">
              <span class="glyphicon glyphicon-list pull-right"></span></a>
              <ul class="dropdown-menu">
                    <li><a href="imageupload"><span class="glyphicon glyphicon-picture pull-right"></span>Upload Image</a></li>
                    <li class="divider"></li>
                    <li><a href="directoryupload"><span class="glyphicon glyphicon-folder-open pull-right"></span>Upload Folder</a></li>
                    <li class="divider"></li>
                    <li><a href="groups"><span class="glyphicon glyphicon-user pull-right"></span>Groups</a></li>
                    <li class="divider"></li>
                    <li><a href="#"><span class="glyphicon glyphicon-stats pull-right"></span>User Stats</a></li>
                    <li class="divider"></li>
                    <li><a href="logout"><span class="glyphicon glyphicon-log-out pull-right"></span>Sign Out</a></li>
              </ul>
            </li>
        </ul>
        </div>

        <ul class="nav navbar-nav pull-right">
            <li class="divider-vertical"></li>
            <li><p class="navbar-text" id='username'><!--% out.println(username); %--></p></li>
        </ul>

        <div class="nav navbar-nav pull-right">
            <form class="navbar-form" role="search">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="Search" name="q">
                <div class="input-group-btn">
                    <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                </div>
            </div>
            </form>
        </div>

    </div>
</div>
