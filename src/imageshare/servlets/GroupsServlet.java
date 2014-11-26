package imageshare.servlets;
import imageshare.model.Group;
import imageshare.model.GroupList;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class GroupsServlet extends HttpServlet {

	private static final String GROUPS_JSP = "/webapp/jsp/groups.jsp";

	OracleHandler database;
	String user = "";
	List<Group> group_list;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		database = OracleHandler.getInstance();
		response.setContentType("text/html;charset=UTF-8");
		user = (String) request.getSession(true).getAttribute("user");

		/* if no user logged in, redirect to login page */
		if (user == null) {
			response.sendRedirect("login.jsp");
		};

		try {
			group_list = database.getGroups(user);
			//database.closeConnection();

			writeGroupsJSP(request, response, group_list);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GROUPS_JSP);    
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("submitGrp") != null) {
			String new_group = request.getParameter("groupname");
			try {
				database.storeNewGroup(user, new_group);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(request.getParameter("addmember") != null) {
			if(request.getParameter("listadd") != null)
			{
				String groupId = request.getQueryString();
				try {
					database.add_friend(Integer.parseInt(groupId), request.getParameter("listadd"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(request.getParameter("deletemember") != null) {
			if(request.getParameter("listdelete") != null)
			{
				String groupId = request.getQueryString();
				try{
					database.delete_friend(Integer.parseInt(groupId), request.getParameter("listdelete"));

				}

				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		else if(request.getParameter("deletegrp") != null)
		{
			String delete_group = request.getQueryString();
			try {
				database.delete_group(delete_group);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//checkNewGroup(request, response);
		//		if (request.getParameter("submitGrp") == null)
		//		{
		response.sendRedirect("groups");
		//		}
		//		else if(request.getParameter("backGrp") == null)
		//		{
		//			RequestDispatcher requestDispatcher = request.getRequestDispatcher(GROUPS_JSP);    
		//			requestDispatcher.forward(request, response);
		//		}
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
				bodyHTML =  bodyHTML +"<option id='"+members.get(j)+"' name='"+members.get(j)+"' value='" + members.get(j) + "'>" + members.get(j) + "</option>";
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
				bodyHTML =  bodyHTML +"<option id='"+members.get(k)+"' name='"+members.get(k)+"' value='" + members.get(k) + "'>" + members.get(k) + "</option>";
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

	/**
	 * Checks to see if a group name has been entered in the
	 * "new_group" box
	 * @param HttpServletRequest
	 */
	public void checkNewGroup(HttpServletRequest request, HttpServletResponse response) {

		//			bodyHTML = 
		//					"<form id='addGroupForm' class='well' method='post'" +
		//							"action='" +  GROUPS_JSP + "' role='form''>" +
		//							"<div class='row'>" +
		//							"<div class='col-md-6 col-md-offset-3'>" +
		//							"<h3 class='text-danger'>Please enter a valid group name</h3>" +
		//							"<div class='form-group'>" +
		//							"<p>Enter a name for your new group:</p>" +
		//							"<input class='form-control' name='query' type='text'" +
		//							"placeholder='group name...'>" +
		//							"</div>" +
		//							"</div>" +
		//							"</div>" +
		//							"<div class='span7 text-center'>" +
		//							"<div class='btn-group'>" +
		//							"<button id='submitGrp' type='submit'" +
		//							"class='btn btn-primary' data-backdrop='static'>Submit" +
		//							"</button>" +
		//							"<button id='backGrp' type='submit'" +
		//							"class='btn btn-primary' data-backdrop='static'>Back" +
		//							"</button>" +
		//							"</div>" +
		//							"</div>" +
		//							"</form>";

		//		request.getSession(true).setAttribute("bodyHTML", bodyHTML);
		//		RequestDispatcher requestDispatcher = request.getRequestDispatcher(GROUPS_JSP);    

	}
}

