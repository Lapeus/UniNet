package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.javaClasses.ChatFreund;
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
				String name = rs.getString(1) + " " + rs.getString(2);
				String timeStamp = "Zeitstempel P";//rs.getString(3) + " " + rs.getBoolean(4);
				String nachricht = rs.getString(3);
				int anzahlLikes = rs.getInt(4);
				int anzahlKommentare = rs.getInt(5);
				Beitrag beitrag = new Beitrag(name, timeStamp, nachricht, anzahlLikes, anzahlKommentare);
				beitragList.add(beitrag);
			}
			request.setAttribute("beitragList", beitragList);
			
			// Freunde (Online)
			sql = sqlSt.getFreundeOnlineSql();
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			rs = pStmt.executeQuery();
			List<ChatFreund> chatfreunde = new ArrayList<ChatFreund>();
			while (rs.next()) {
				String vorname = rs.getString(1);
				String nachname = rs.getString(2);
				int userID = rs.getInt(3);
				ChatFreund freund = new ChatFreund(vorname, nachname, userID);
				chatfreunde.add(freund);
			}
			request.setAttribute("chatfreunde", chatfreunde);
			
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
