<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp"%>
<body>
	<%@include file="navbar.jsp"%>

	<div class="jumbotron">
		<div class="container">
			<h1>Search</h1>
			<p id="titleLeft">Find Images.</p>

			<form class='well' action='search' method='post' role='form'>
				<div class="row">
					<div class="col-md-6 col-md-offset-3">

						<div class="form-group">
							<p>Keywords:</p>
							<input class="form-control" name="query" type="text"
								placeholder="Enter search query...">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-7 col-md-offset-3">
						<div class="form-group">
							<p>Date:</p>
							<div class="col-md-4">
								<input name="fromdate" class="form-control span-6" type="text"
									placeholder="YYYY-MM-DD">
							</div>
							<div class="col-md-2 text-center">
								<p>To</p>
							</div>
							<div class="col-md-4">
								<input name="todate" class="form-control" type="text"
									placeholder="YYYY-MM-DD">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-md-offset-4">
							<p>Sort By:</p>
							<div class="form-group">
								<div class="btn-group" data-toggle="buttons">
									<div class="btn-group" data-toggle="buttons-radio">
										<label class="btn btn-primary"> <input type="radio"
											name="sortby" value="1" class="sr-only" required>Descending
											Time
										</label> <label class="btn btn-primary"> <input type="radio"
											name="sortby" value="2" class="sr-only" required>Ascending
											Time
										</label> <label class="btn btn-primary"> <input type="radio"
											name="sortby" value="3" class="sr-only" required>Rank
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					<label for="submitsearch" class="col-sm-3 control-label"></label>
					<div class="col-sm-9">
						<input type="submit" name="submitsearch"
							class="btn btn-primary btn-lg pull-right"
							placeholder='Submit Search' id="submitsearch">
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="container">
		<%= (String) request.getSession(false).getAttribute("galHTML") %>
	</div>
	</div>
</body>
</footer>
<div class="container">
	<hr>
	<%@include file="footer.jsp"%>
</div>
</footer>
</html>

