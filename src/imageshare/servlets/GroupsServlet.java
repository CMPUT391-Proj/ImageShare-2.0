package imageshare.servlets;
import imageshare.model.Group;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GroupsServlet extends HttpServlet {

	private static final String GROUPS_JSP = "groupsview";

	OracleHandler database;
	String user = "";
	List<Group> group_list;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		database = OracleHandler.getInstance();
		response.setContentType("text/html;charset=UTF-8");
		user = (String) request.getSession(true).getAttribute("user");

		/* if no user logged in, redirect to login page */
		if (user == null) {
			response.sendRedirect("index");
			return;
		};

		try {
			group_list = database.getGroups(user);

			writeGroupsJSP(request, response, group_list);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		response.sendRedirect(GROUPS_JSP);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		user = (String) request.getSession(true).getAttribute("user");
		/* if no user logged in, redirect to login page */
		if (user == null) {
			response.sendRedirect("index");
			return;
		};

		HttpSession session = request.getSession();

		if (request.getParameter("submitGrp") != null) {
			String new_group = request.getParameter("groupname");
			if(!new_group.equals("")){
				try {
					database.storeNewGroup(user, new_group);
				} catch (Exception e) {
					if (e instanceof SQLIntegrityConstraintViolationException)
						session.setAttribute("error", "Error: Group name must be unique.");
					else
						session.setAttribute("error", e.toString());
					response.sendRedirect(GROUPS_JSP);
					return;
				}
			}
			else
			{
				session.setAttribute("error", "Group name cannot be empty");
			}
		}
		else if(request.getParameter("addmember") != null) {
			if(request.getParameter("listadd") != null)
			{
				String groupId = request.getQueryString();
				try {
					database.add_friend(Integer.parseInt(groupId), request.getParameter("listadd"));
				} catch (Exception e) {
					session.setAttribute("error", e.toString());
					response.sendRedirect(GROUPS_JSP);
					return;
				}
			}
		}
		else if(request.getParameter("deletemember") != null) {
			if(request.getParameter("listdelete") != null)
			{
				String groupId = request.getQueryString();
				try {
					database.delete_friend(Integer.parseInt(groupId), request.getParameter("listdelete"));
				} catch (Exception e) {
					session.setAttribute("error", e.toString());
					response.sendRedirect(GROUPS_JSP);
					return;
				}
			}

		}
		else if(request.getParameter("deletegrp") != null)
		{
			String delete_group = request.getQueryString();
			try {
				database.delete_group(delete_group);
			} catch (Exception e) {
				session.setAttribute("error", e.toString());
				response.sendRedirect(GROUPS_JSP);
				return;
			}
		}
		response.sendRedirect("groups");
	}

	/**
	 * Writes the groups as jsp out to the browser
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param ArrayList<Group>
	 * @param String
	 */
	public void writeGroupsJSP(HttpServletRequest request, 
			HttpServletResponse response, List<Group> grpList) {

		List<String> members = new ArrayList<String>();
		String bodyHTML = "";
		for (int i = 0; i < grpList.size(); i++) {

			bodyHTML =  bodyHTML +
					"<article id = '" + grpList.get(i).getGroupId() + "' name = '" + grpList.get(i).getGroupId() + "' class='search-result row'>" +
					"<div class='col-xs-12 col-sm-12 col-md-3'>" +
					"<a href='#' title='Lorem ipsum' class='thumbnail'><img src='https://fbstatic-a.akamaihd.net/rsrc.php/v2/ym/r/idA1SgH-tQQ.png' alt='Lorem ipsum' /></a>" +
					"</div>" +
					"<div class='col-xs-12 col-sm-12 col-md-2'>" +
					"<form class='well' action='groups?" + grpList.get(i).getGroupId() + "' method='post' role='form'>" +
					"<ul class='meta-search'>" +
					"<li><i class='glyphicon glyphicon-calendar'></i> <span>Date Created: "+ grpList.get(i).getDateCreated().toString() + "</span></li>" +
					"<li><button id='deletegrp' name='deletegrp' type='submit' class='btn btn-primary' data-backdrop='static'>Delete Group</button></li>" +
					"</ul></form>" +
					"</div>" +
					"<div class='col-xs-12 col-sm-12 col-md-2'>" +
					"<form class='well' action='groups?" + grpList.get(i).getGroupId() + "' method='post' role='form'>" +
					"<select class='form-control' id='listdelete' name='listdelete'>";

			database = OracleHandler.getInstance();
			try {
				members = database.getUsersInGroup((grpList.get(i).getGroupId()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// fill add listbox with existing group members
			for (int j = 0; j < members.size(); j++) {
				if(!members.get(j).equals(user)){
					bodyHTML =  bodyHTML +"<option id='"+members.get(j)+"' name='"+members.get(j)+"' value='" + members.get(j) + "'>" + members.get(j) + "</option>";
				}
			}

			bodyHTML =  bodyHTML + "</select>" +
					"<button id='deletemember' name='deletemember' type='submit' class='btn btn-primary btn-sm' data-backdrop='static'>Delete</button>" +
					"<select class='form-control' id='listadd' name='listadd'>";

			// fill delete listbox with nonexisting users
			try {
				members = database.getUsersNotInGroup((grpList.get(i).getGroupId()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			for (int k = 0; k < members.size(); k++) {
				if(!members.get(k).equals(user)){
					bodyHTML =  bodyHTML +"<option id='"+members.get(k)+"' name='"+members.get(k)+"' value='" + members.get(k) + "'>" + members.get(k) + "</option>";
				}
			}

			try {
				members = OracleHandler.getInstance().getUsersInGroup(grpList.get(i).getGroupId());
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bodyHTML =  bodyHTML + "</select>" +
					"<button id='addmember' name='addmember' type='submit' class='btn btn-primary btn-sm' data-backdrop='static'>Add</button></form>" +


			//insert group members

					"</div>" +
					"<div class='col-xs-12 col-sm-12 col-md-2'>" +
					"<h3>" + grpList.get(i).getGroupname() + "</h3>" +
					"<p>Group Members: </p><ul>";

			for(String member : members)
			{
				bodyHTML =  bodyHTML + "<li>" + member + "</li>";
			}
			bodyHTML =  bodyHTML + "</ul>" +
					"</div>" +
					"<span class='clearfix borda'></span>" +
					"</article>" +
					"<ul class='list-unstyled'><li><hr></li></ul>";
		}
		request.getSession(true).setAttribute("groupcount", Integer.toString(grpList.size()));
		request.getSession(true).setAttribute("groupBodyHTML", bodyHTML);
	}
}

