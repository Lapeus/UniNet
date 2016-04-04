package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;

/**
 * Servlet implementation class HashtagServlet
 */
@WebServlet("/HashtagServlet")
public class HashtagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HashtagServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lade Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
		
		String hashtag = request.getParameter("tag");
		request.setAttribute("hashtag", hashtag);
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		try {
			int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Hashtag", userID, "#" + hashtag));
			request.getRequestDispatcher("Hashtags.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Fehler
		} finally {
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
