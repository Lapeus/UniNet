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
import de.dbae.uninet.sqlClasses.BeitragSql;

/**
 * Servlet implementation class BeitragServlet
 */
@WebServlet("/BeitragServlet")
public class BeitragServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BeitragServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		switch (name) {
		case "Like":
			likeBeitrag(request, response);
			break;

		default:
			break;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private void likeBeitrag(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		String page = request.getParameter("page");
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = sqlSt.getLike();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setInt(2, userID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			try {
				String sql = sqlSt.getEntferneLike();
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.setInt(2, userID);
				pStmt.executeUpdate();
			} catch (Exception e2) {
				System.out.println("SQL Fehler in BeitragServlet");
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				request.getRequestDispatcher(page).forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
