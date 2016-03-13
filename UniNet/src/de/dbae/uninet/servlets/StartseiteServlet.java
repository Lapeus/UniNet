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
		StartseiteSql sqlSt = new StartseiteSql();
		try {
			/*String sql = sqlSt.getBeitraegeSql(session.getAttribute("UserID").toString());
			PreparedStatement pStmt = con.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();*/
			List<Beitrag> beitragList = new ArrayList<Beitrag>();
			/*ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				String name = rs.getString(1) + " " + rs.getString(2);
				String timeStamp = rs.getString(3) + " " + rs.getBoolean(4);
				String nachricht = rs.getString(5);
				int anzahlLikes = rs.getInt(6);
				int anzahlKommentare = rs.getInt(7);
				Beitrag beitrag = new Beitrag(name, timeStamp, nachricht, anzahlLikes, anzahlKommentare);
				beitragList.add(beitrag);
			}*/
			Beitrag beitrag1 = new Beitrag("Christian Ackermann", "13. M‰rz 18:27 P", "Ich hab nen groﬂes Problem. <br>Irgendwie steht da null an der Seite und ich weiﬂ nich wieso.<br>Bitte helft mir!", 1234, 69);
			Beitrag beitrag2 = new Beitrag("Marvin Wolf", "13. M‰rz 18:27 P", "Du bist leider viel schlauer als wir, da kann ich dir nicht helfen", 15, 3);
			Beitrag beitrag3 = new Beitrag("Leon Schaffert", "13. M‰rz 18:27 P", "H‰h?", -3, 0);
			beitragList.addAll(Arrays.asList(beitrag1, beitrag2, beitrag3));
			request.setAttribute("beitragList", beitragList);
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
		request.getRequestDispatcher("Startseite.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
