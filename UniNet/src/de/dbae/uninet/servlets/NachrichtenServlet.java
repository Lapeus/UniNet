package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Nachricht;
import de.dbae.uninet.sqlClasses.NachrichtenSql;

/**
 * Servlet implementation class NachrichtenServlet
 */
@WebServlet("/NachrichtenServlet")
public class NachrichtenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
	private int userIDFreund;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NachrichtenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		// Attribute setzen
		String userId = request.getParameter("userID");
		if (userId == null) {
			userId = userIDFreund+"";
		}
		userIDFreund = Integer.parseInt(userId);
		// Name des Freundes herausfinden
		NachrichtenSql nSql = new NachrichtenSql();
		Connection con = new DBConnection().getCon();
		String sNameFreund = "";
		try {
			PreparedStatement pStmtName = con.prepareStatement(nSql.getName());
			pStmtName.setInt(1, userIDFreund);
			ResultSet result = pStmtName.executeQuery();
			while (result.next()) {
				sNameFreund = result.getString(1) + " " + result.getString(2);
				System.out.println(sNameFreund);
			}
		} catch (SQLException e) {
			System.out.println("SQL Fehler NameFreund - NachrichtenServletGET");
		} finally {
			if	(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println("Verbindung konnte nicht beendet werden - NachrichtenServeletGET");
				}
			}
		}
	
		request.setAttribute("nameFreund", sNameFreund);
		request.setAttribute("UserIDFreund", userId);
		request.setAttribute("nachrichten", getNachrichten(Integer.parseInt(userId)));
		request.getRequestDispatcher("Nachrichten.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		Connection con = null;
		NachrichtenSql nSql = new NachrichtenSql();
		if(request.getParameter("senden") != null){
			if(!request.getParameter("nachricht").equals("")) {
				try {
					con = new DBConnection().getCon();
					PreparedStatement pStmtSenden = con.prepareStatement(nSql.nachrichtSenden());
					// Prepared Stmt mit Argumenten fuellen
					pStmtSenden.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
					pStmtSenden.setInt(2, userIDFreund);
					pStmtSenden.setString(3, request.getParameter("nachricht"));
					// Datestamp erstellen
					java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
					pStmtSenden.setDate(4, sqlDate);
					// Timestamp erstellen
					java.sql.Time sqlTime = new java.sql.Time(System.currentTimeMillis());
					pStmtSenden.setTime(5, sqlTime);
					pStmtSenden.execute();
					request.setAttribute("userID", userIDFreund);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (con != null) {
							con.close();
							System.out.println("Die Verbindung wurde erfolgreich beendet! (NachrichtenServletPOST)");
						}
					} catch (Exception ignored) {
						ignored.printStackTrace();
					}
				}
			} 
		}
		doGet(request, response);
	}
	
	private List<Nachricht> getNachrichten(int userIDFreund) {
		Connection con = null;
		NachrichtenSql nSql = new NachrichtenSql();
		List<Nachricht> nachrichten = new ArrayList<>();
		
		try {
			con = new DBConnection().getCon();
			int userId = Integer.parseInt(session.getAttribute("UserID").toString());
			System.out.println("Verbindung wurde geöffnet (NachrichtenServletGET)");
			PreparedStatement pStmtNachrichten = con.prepareStatement(nSql.getNachrichtenListe());
			pStmtNachrichten.setInt(1, userId);
			pStmtNachrichten.setInt(2, userIDFreund);
			pStmtNachrichten.setInt(3, userIDFreund);
			pStmtNachrichten.setInt(4, userId);
			ResultSet result = pStmtNachrichten.executeQuery();
			while (result.next()) {
				int senderId = result.getInt(1);
				String name = result.getString(2) +" "+ result.getString(3);
				String nachricht = result.getString(4);
				java.sql.Date date = result.getDate(5);
				java.sql.Time time = result.getTime(6);
				Date finalTime = null;
				if (time != null) {
					finalTime = new Date(date.getTime() + time.getTime());
				}
				nachrichten.add(new Nachricht(senderId, name, nachricht, finalTime));
			}
			// TESTING
			for (Nachricht nachricht : nachrichten) {
				nachricht.getName();
				nachricht.getNachrichtenText();
			}
		} catch (Exception e) {
			System.out.println("Fehler - getNachrichten");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet! (NachrichtenServletGET)");
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		return nachrichten;
	}

}
