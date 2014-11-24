<html lang="en">

<%@include file="header.jsp"%>
<body>
	<%@include file="navbar.jsp"%>

	<div class="jumbotron">
		<div class="container">
			<h1>Search</h1>
			<p id="titleLeft">Find Images.</p>

			<form id="landing-page-search-form" class="well" method="get"
				action="/search" role="form" onsubmit="return getcoord(this);">
				<div class="row">
					<div class="col-md-6 col-md-offset-3">

						<div class="form-group">
							<p>Keywords:</p>
							<input class="form-control" name="query" type="text"
								placeholder="Enter search query..." required>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-7 col-md-offset-3">
						<div class="form-group">
							<p>Date:</p>
							<div class="col-md-4">
								<input name="fromDate" class="form-control span-6" type="text"
									placeholder="YYYY-MM-DD" required>
							</div>
							<div class="col-md-2 text-center">
								<p>To</p>
							</div>
							<div class="col-md-4">
								<input name="toDate" class="form-control" type="text"
									placeholder="YYYY-MM-DD" required>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-md-offset-4">
							<p>Sort By:</p>
							<div class="form-group">
								<div class="btn-group" data-toggle="buttons">
									<div class="btn-group" data-toggle="buttons-radio">
										<label class="btn btn-primary"
											<input type="radio" name="sortby" value="1" class="sr-only" required>Descending Time
					                    </label>
					                    <label class="btn btn-primary">
					                        <input type="radio" name="sortby" value="2" class="sr-only" required>Ascending Time
					                    </label>
										<label class="btn btn-primary">
					                        <input type="radio" name="sortby" value="3" class="sr-only" required>Rank
					                    </label>
									</div>
								</div>
							</div>
						</div>
					</div>
			</form>
		</div>
	<button type="submit" class="btn btn-primary btn-lg pull-right">Search</button>
	</div>
	<div>
		<section class="col-xs-12 col-sm-6 col-md-12">
			<%= (String) request.getSession(false).getAttribute("searchBodyHTML") %>
		</section>
	</div>
</body>
</html>

