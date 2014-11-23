package imageshare.servlets;
import imageshare.model.Group;
import imageshare.model.GroupList;
import imageshare.model.GroupTuple;
import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GroupsServlet extends HttpServlet {

	private static final String GROUPS_JSP = "groups";
	
	OracleHandler database;
	String user = "";
	List<Group> group_list;

	public void init(ServletConfig config) throws ServletException {
		super.init(config); 
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session;

		database = OracleHandler.getInstance();
		response.setContentType("text/html;charset=UTF-8");
		session = request.getSession(true);
//		user = (String) session.getAttribute("username");
		
		/* if no user logged in, redirect to login page */
//		if (user == null) {
//			response.sendRedirect("login.jsp");
//		};
		
		try {
			group_list = database.getGroups(user);
			//database.closeConnection();
		    /**
		     * Gets users in a group
		     * @param rs
		     * @return
		     */
			
			List<GroupTuple> pairList = new ArrayList<GroupTuple>();
			List<GroupList> groupLists = new ArrayList<GroupList>();
			for(Group group : group_list)
			{
				groupLists = database.getGroupsLists(group.getGroupId());
				
				pairList.add(new GroupTuple(group, groupLists));
			}
		    
			handle_write(session, pairList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//response.sendRedirect(GROUPS_JSP);
	}	
	
	/**
	 * Writes the groups as jsp out to the browser
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param ArrayList<Group>
	 * @param String
	 */
	public void handle_write(HttpSession session, List<GroupTuple> pairList) {

//		PrintWriter out = null;
//
//		try {
//			out = response.getWriter();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			request.getRequestDispatcher("includes/header.jsp").
//			include(request, response);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}

		String groupsHTML = "";
    	for (int i = 0; i < pairList.size(); i++) {

            groupsHTML =  groupsHTML + "<article class='search-result row'>" +
    			"<div class='col-xs-12 col-sm-12 col-md-3'>" +
    				"<a href='#' title='Lorem ipsum' class='thumbnail'><img src='https://fbstatic-a.akamaihd.net/rsrc.php/v2/ym/r/idA1SgH-tQQ.png' alt='Lorem ipsum' /></a>" +
    			"</div>" +
    			"<div class='col-xs-12 col-sm-12 col-md-2'>" +
    				"<ul class='meta-search'>" +
    					"<li><i class='glyphicon glyphicon-calendar'></i> <span>"+ pairList.get(i).getGroup().getDateCreated().toString() + "</span></li>" +
    				"</ul>" +
    			"</div>" +
    			"<div class='col-xs-12 col-sm-12 col-md-7'>" +
    				"<h3><a href='#' title=''>" + pairList.get(i).getGroup().getGroupname() + "</a></h3>" +
    				"<p>Group description here.</p>" +
    			"</div>" +
    			"<span class='clearfix borda'></span>" +
    		"</article>" +
    		"<ul class='list-unstyled'><li><hr></li></ul>";
		}
    	session.setAttribute("groupcount", Integer.toString(pairList.size()));
		session.setAttribute("groupsHTML", groupsHTML);

	}
}

