package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Semesterrechner;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.ProfilSql;

/**
 * Dieses Servlet sorgt f&uuml;r die Bereitstellung aller Daten f&uuml;r die korrekte Darstellung eines Benutzerprofils.
 * @author Christian Ackermann
 */
@WebServlet("/ProfilServlet")
public class ProfilServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Die aktuelle Session.
	 */
	private HttpSession session;
	
	/**
	 * Die DB-Verbindung.
	 */
	private Connection con;
	
	/**
	 * Stellt die Sql-Statements zur Verf&uuml;gung.
	 */
	private ProfilSql sqlSt = new ProfilSql();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
				
		// Setze die aktuelle Session
		session = request.getSession();
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		// Die UserID unter Vorbehalt
		String user = request.getParameter("userID");
		boolean eigenesProfil = false;
		// Wenn keine UserID gesetzt wurde
		if (user == null) {
			// Ist es das Profil des aktuellen Nutzers
			user = session.getAttribute("UserID").toString();
		}
		if (user.equals(session.getAttribute("UserID").toString())) {
			eigenesProfil = true;
		}
		int userID = Integer.parseInt(user);
		// Setze die UserID als request-Attribut
		request.setAttribute("userID", user);
		// Setze den Wahrheitswert, ob auf dem Profil Beitraege gepostet werden duerfen (eigenes Profil)
		request.setAttribute("beitragPosten", eigenesProfil);
		try {
			// Lade und setze alle Beitraege
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Profilseite", userID));
			// Lade Sql-Statement um die Infos zu laden
			String sql = sqlSt.getSqlStatement("Infos");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				// Setze den Namen
				request.setAttribute("name", rs.getString(1) + " " + rs.getString(2));
				// Setze den Studiengang
				request.setAttribute("studiengang", rs.getString(3));
				// Formatiere den Studienbeginn
				SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
				// Setze das aktuelle Semester
				int semester = new Semesterrechner().getSemester(rs.getDate(4).getTime());
				request.setAttribute("semester", semester);
				// Wenn ein Geburtstag eingetragen wurde
				if (rs.getString(5) != null) {
					// Setze den Geburstag als Teil einer Aufzaehlung (vgl. getInfoString)
					request.setAttribute("geburtstag", getInfoString("Geburtstag:<br>" + sdf.format(new Date(rs.getDate(5).getTime()))));
				}
				// Wenn ein Wohnort eingetragen wurde
				if (rs.getString(6) != null && !rs.getString(6).equals("")) {
					request.setAttribute("wohnort", getInfoString("Wohnort:<br>" + rs.getString(6)));
				}
				// Wenn Hobbys eingetragen wurden
				if (rs.getString(7) != null && !rs.getString(7).equals("")) {
					request.setAttribute("hobbys", getInfoString("Hobbys:<br>" + rs.getString(7)));
				}
				// Wenn Interessen eingetragen wurden
				if (rs.getString(8) != null && !rs.getString(8).equals("")) {
					request.setAttribute("interessen", getInfoString("Interessen:<br>" + rs.getString(8)));
				}
				// Wenn etwas ueber die eigene Persoenlichkeit eingetragen wurde
				if (rs.getString(9) != null && !rs.getString(9).equals("")) {
					request.setAttribute("ueberMich", getInfoString("Über mich:<br>" + rs.getString(9)));
				}
				// Setze die Email-Adresse
				request.setAttribute("email", rs.getString(10));
			}
			// Lade Sql-Statement um die Anzahl der Freunde zu bekommen
			sql = sqlSt.getSqlStatement("AnzahlFreunde");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			rs = pStmt.executeQuery();
			if (rs.next()) {
				// Setze die Anzahl der Freunde
				request.setAttribute("anzFreunde", rs.getInt(1));
			}
			// Weiterleitung
			request.getRequestDispatcher("Profil.jsp").forward(request, response);
		} catch (Exception e) {
			System.err.println("SQL Fehler im ProfilServlet");
			e.printStackTrace();
			// TODO Fehler
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Die Aktion, die das Servlet durchfuehren soll
		String name = request.getParameter("name");
		// Neue DB-Connection oeffnen
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			// Je nach Aktion
			switch (name) {
			case "BeitragPosten":
				posteBeitrag(request, response);
				break;
			default:
				doGet(request, response);
				break;
			}
		} catch (Exception e) {
			System.err.println("SQL Fehler im ProfilServlet");
			// TODO Fehler
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}
	
	/**
	 * Erstellt einen Beitrag und speichert ihn in der DB.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 */
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// Die aktuelle Session
		session = request.getSession();
		// Stellt die Sql-Statements zur Verfuegung
		BeitragSql sqlSt = new BeitragSql();
		String beitrag = request.getAttribute("beitrag").toString();
		// Der Verfasser ist automatisch der aktuelle User
		int verfasserID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sichtbarkeit = request.getParameter("sichtbarkeit");
		boolean sichtbar = true;
		if (sichtbarkeit.startsWith("Privat")) {
			sichtbar = false;
		}
		// Lade Sql-Statement um den Beitrag anzulegen
		String sql = sqlSt.getSqlStatement("BeitragAnlegen1");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, beitrag);
		pStmt.setInt(2, verfasserID);
		pStmt.setBoolean(3, sichtbar);
		// Setze den Zeitstempel (jetzt)
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		
		// Lade Sql-Statement um die neue BeitragsID zu bekommen
		sql = sqlSt.getSqlStatement("BeitragAnlegen2");
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int beitragsID;
		if (rs.next()) {
			beitragsID = rs.getInt(1);
			// Lade Sql-Statement um den Beitrag als Chronikbeitrag einzutragen
			sql = sqlSt.getSqlStatement("BeitragAnlegenChronik");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.executeUpdate();
			// Weiterleitung
			response.sendRedirect("ProfilServlet");
		} else {
			System.err.println("Problem beim Anlegen des Beitrags");
			// TODO Fehler
		}
	}
	
	/**
	 * Gibt die Information als Teil einer Html-Auflistung zur&uuml;ck.
	 * @param info Die Information
	 * @return Die Html-Information
	 */
	private String getInfoString(String info) {
		return "<li class='list-group-item'>" + info + "</li>";
	}

}
