<!DOCTYPE html>
<html lang="en">
<%@include file="header.jsp" %>
<%@ page import="imageshare.oraclehandler.OracleHandler"%>
<%
	// check user is admin
	String username = (String) session.getAttribute("user");

	if (username == null || !username.equals("admin")) { response.sendRedirect("gallery"); return; }

	String testtitle = (String) session.getAttribute("testtitle");
	String customjson = (String) session.getAttribute("customjson");
	String search = (String) session.getAttribute("search");

	session.setAttribute("customjson", null);
	session.setAttribute("testtitle", null);
	session.setAttribute("search", null);
%>
<style>
ul ul li { background: #faa; }
</style>
<body>
	<%@include file="navbar.jsp" %>

	<div class="jumbotron">
		<div class="container">
			<h1>Data Analysis</h1>
			<p id="titleLeft">ImageShare Data Analytics</p>
		</div>
	</div>

	<%@include file="error.jsp" %>

	<div class="row">
		<div class="col-lg-6 col-lg-offset-3">
			<h4 id="testtitle"><% out.print(testtitle);%></h4>
			<div id="customjsondata">
			</div>
		</div>
	</div>

	<div class="container">
		<hr>
			<%@include file="footer.jsp"%>
	</div>

	<script>
		$('#button-imagespersubject').click(function() {
			document.location.href = './dataanalysis/imagespersubject';
		});


		$(document).ready(function() {
			var search = <% out.print("\'"+search+"\'"); %>;
			var customdatajson = jQuery.parseJSON(<% out.print("\'"+customjson+"\'"); %>);
			//var customdatajson =jQuery.parseJSON('{"result":[{"COUNT":"1","MONTH_LIST":[{"WEEK_LIST":[{"WEEK":50,"COUNT":1}],"COUNT":"1","MONTH":"11"}],"YEAR":"1991"},{"COUNT":"1","MONTH_LIST":[{"WEEK_LIST":[{"WEEK":25,"COUNT":1}],"COUNT":"1","MONTH":"05"}],"YEAR":"2007"},{"COUNT":"9","MONTH_LIST":[{"WEEK_LIST":[{"WEEK":41,"COUNT":1}],"COUNT":"1","MONTH":"09"},{"WEEK_LIST":[{"WEEK":46,"COUNT":1}],"COUNT":"1","MONTH":"10"},{"WEEK_LIST":[{"WEEK":51,"COUNT":1},{"WEEK":52,"COUNT":2}],"COUNT":"7","MONTH":"11"}],"YEAR":"2014"}]}');
			var unorderedlist = '';


			if (search == 'customsearch') {
				unorderedlist = parseCustomJson(customdatajson.result);
			}
			else if(search == 'imagespersubject') {
				var subjectList = customdatajson.result;

				unorderedlist = buildJsonList(subjectList, 'SUBJECT');
				/*
				unorderedlist += '<ul>\n';
				for (var i=0; i<subjectList.length; i++) {

					
					unorderedlist += '<div>\n';
						unorderedlist += '<li>'+subjectList[i].SUBJECT+'</li>';
						unorderedlist += '<ul>';
							unorderedlist += '<li">'+parseCustomJson(subjectList[i].data)+'</li>\n';
						unorderedlist += '</ul>\n';
					unorderedlist += '</div>\n';
					
				}
				unorderedlist += '</ul>\n';
				*/
			}
			else if(search == 'imagesperuser') {
				var userList = customdatajson.result;
				
				unorderedlist = buildJsonList(userList, "OWNER_NAME");
/*
				unorderedlist += '<ul>\n';
				for (var i=0; i<subjectList.length; i++) {

					
					unorderedlist += '<div>\n';
						unorderedlist += '<li>'+subjectList[i].SUBJECT+'</li>';
						unorderedlist += '<ul>';
							unorderedlist += '<li">'+parseCustomJson(subjectList[i].data)+'</li>\n';
						unorderedlist += '</ul>\n';
					unorderedlist += '</div>\n';
					
				}
				unorderedlist += '</ul>\n';
*/
			}



			$('#customjsondata').append(unorderedlist);
			$('#testtitle').val('text');
		});

		function buildJsonList(jsonList, title) {
			var dataList = '<ul>\n';

			for (var i=0; i<jsonList.length; i++) {
				dataList += '<div>\n';
					dataList += '<li>'+jsonList[i][title]+'</li>';
					dataList += '<ul>';
						dataList += '<li">'+parseCustomJson(jsonList[i].data)+'</li>\n';
					dataList += '</ul>\n';
				dataList += '</div>\n';
				
			}
			dataList += '</ul>\n';

			return dataList;
		}

		function parseCustomJson(jsonList) {
			var data = '';
			
			data += '<ul class="list-group">\n';
			for (var i=0; i<jsonList.length; i++) {
				data += '<li class="list-group-item" > YEAR: '+jsonList[i].YEAR+' - COUNT: '+jsonList[i].COUNT+'</li>\n';

				data += '<li class="list-group-item"  data-toggle="collapse" ><ul class="list-group">\n'
				for (var j=0; j<jsonList[i].MONTH_LIST.length; j++) {
					data += '<li class="list-group-item" data-toggle="collapse" > MONTH: '+jsonList[i].MONTH_LIST[j].MONTH+' - COUNT: '+jsonList[i].MONTH_LIST[j].COUNT+'</li>\n';
					

					data += '<li class="list-group-item" data-toggle="collapse" ><ul class="list-group">\n'
					for (var k=0; k<jsonList[i].MONTH_LIST[j].WEEK_LIST.length; k++) {
						data += '<li class="list-group-item"> WEEK: '+jsonList[i].MONTH_LIST[j].WEEK_LIST[k].WEEK+' - COUNT: '+jsonList[i].MONTH_LIST[j].WEEK_LIST[k].COUNT+'</li>\n';
					}
					data += '</ul></li>\n';
				}
				data += '</ul></li>\n';
			}
			data += '</ul>\n';
			return data;
		}

		$("li").click(function() {
			$(this).parent().find('ul').toggle();
		});
	</script>
</body>