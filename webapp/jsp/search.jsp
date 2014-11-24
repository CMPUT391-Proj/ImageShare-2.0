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
							<p>Keywords:</p> <input class="form-control" name="q" type="text"
								placeholder="Enter search query...">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-7 col-md-offset-3">
						<div class="form-group">
						<p>Date:</p>
							<div class="col-md-4">
								<input name="startDate" class="form-control span-6" type="text"
									placeholder="YYYY-MM-DD">
							</div>
							<div class="col-md-2 text-center">
								<p>To</p>
							</div>
							<div class="col-md-4">
								<input name="endDate" class="form-control" type="text"
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
										<button type="button" class="btn btn-primary">Decending Time</button>
										<button type="button" class="btn btn-primary">Ascending Time</button>
										<button type="button" class="btn btn-primary active">Rank</button>
									</div>
								</div>
							</div>
						</div>
					</div>
			</form>
		</div>
							<button type="submit" class="btn btn-primary btn-lg pull-right">Search</button>
	</div>
</body>
</html>

