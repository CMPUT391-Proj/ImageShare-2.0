<div class="container">
	    <div class="row">
	    	<div class="col-md-6">
		        <h1>Header1</h1>
				<p>This is a template for a simple marketing or informational
					website. It includes a large callout called a jumbotron and three
					supporting pieces of content. Use it as a starting point to create
					something more unique.
				</p>
				<p>
					<a class="btn btn-primary btn-lg" role="button">Learn more
						&raquo;</a>
				</p>
			</div>

	        <div class="col-md-6">
	            <div class="panel panel-default">
	                <div class="panel-heading">
	                    <span class="glyphicon glyphicon-lock"></span> Login</div>
	                <div class="panel-body">
	                    <form class="form-horizontal" role="form" id="login" action="/login" method="post">
		                    <div class="form-group">
		                        <label for="username" class="col-sm-3 control-label">
		                            Username</label>
		                        <div class="col-sm-9">
		                            <input type="username" class="form-control" id="inputUsername" name="username" placeholder="Username" required>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label for="password" class="col-sm-3 control-label">
		                            Password</label>
		                        <div class="col-sm-9">
		                            <input type="password" class="form-control" id="inputPassword" name="password" placeholder="Password" required>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <div class="col-sm-offset-3 col-sm-9">
		                            <div class="checkbox">
		                                <label> 
		                                    <input type="checkbox"/>
		                                    Remember me
		                                </label>
		                            </div>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                    	<div class="col-sm-offset-3 col-sm-9" id = "invalidDesc">
		                    	</div>
		                    </div>
		                    <div class="form-group last">
		                        <div class="col-sm-offset-3 col-sm-9">
		                            <button type="submit" class="btn btn-success btn-sm" id = "loginButton">
		                                Sign in</button>
		                                 <button type="reset" class="btn btn-default btn-sm">
		                                Reset</button>
		                        </div>
		                    </div>
	                    </form>
	                </div>
	                <div class="panel-footer">
	                    Not Registered? <a data-toggle="modal" href="/ImageShare-2.0/registration">Register here</a></div>
	            </div>
	        </div>
	    </div>
	</div>
