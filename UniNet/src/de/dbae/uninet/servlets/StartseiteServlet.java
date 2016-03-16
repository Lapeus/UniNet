package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.sqlClasses.StartseiteSql;

/**
 * Servlet implementation class StartseiteServlet
 */
@WebServlet("/StartseiteServlet")
public class StartseiteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartseiteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (Startseite)");
		StartseiteSql sqlSt = new StartseiteSql();
		try {
			// Beitraege
			String sql = sqlSt.getBeitraegeSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			ResultSet rs = pStmt.executeQuery();
			List<Beitrag> beitragList = new ArrayList<Beitrag>();
			while (rs.next()) {
				int userID = rs.getInt(1);
				String name = rs.getString(2) + " " + rs.getString(3);
				String timeStamp = "Zeitstempel P";//rs.getString(3) + " " + rs.getBoolean(4);
				String nachricht = rs.getString(4);
				int anzahlLikes = rs.getInt(5);
				int anzahlKommentare = rs.getInt(6);
				int beitragsID = rs.getInt(7);
				sql = sqlSt.getLikeAufBeitragSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.setInt(2, Integer.parseInt(session.getAttribute("UserID").toString()));
				ResultSet rs2 = pStmt.executeQuery();
				if (rs2.next()) {
					boolean like = rs2.getInt(1) == 0 ? false : true;
					Beitrag beitrag = new Beitrag(userID, name, timeStamp, nachricht, anzahlLikes, anzahlKommentare, beitragsID, like);
					beitragList.add(beitrag);
				}
			}
			request.setAttribute("beitragList", beitragList);
			
			// Weiterleitung
			request.getRequestDispatcher("Startseite.jsp").forward(request, response);
		} catch (Exception e) {
			response.getWriter().append("SQL-Fehler");
			e.printStackTrace();
		} finally {
			try {
				if (con!=null) {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				}
			} catch (SQLException ignored) {}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
