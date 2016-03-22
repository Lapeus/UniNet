package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;

/**
 * Servlet implementation class VeranstaltungenServlet
 */
@WebServlet("/VeranstaltungenServlet")
public class VeranstaltungenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private HttpSession session;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VeranstaltungenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		String tab = request.getParameter("tab");
		request.setAttribute("tab", tab);
		try {
			switch (tab) {
			case "beitraege":
				request.setAttribute("beitraegeActive", "active");
				request.setAttribute("beitragsList", new BeitragServlet().getBeitraege(request, con, "Veranstaltungen", userID));
				break;
			case "infos":
				request.setAttribute("infosActive", "active");
				break;
			case "mitglieder":
				request.setAttribute("mitgliederActive", "active");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler in VeranstaltungenServlet");
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		request.getRequestDispatcher("Veranstaltungen.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
