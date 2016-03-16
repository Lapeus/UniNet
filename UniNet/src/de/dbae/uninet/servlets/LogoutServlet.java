package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (Logout)");
		AnmeldeSql sqlSt = new AnmeldeSql();
		try {
			String sql = sqlSt.getOfflineUpdate();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Fehler beim Ausloggen");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Verbindung wurde geschlossen!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		response.sendRedirect("Anmeldung.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
