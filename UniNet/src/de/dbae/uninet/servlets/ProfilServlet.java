package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.HashtagVerarbeitung;
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
		// Setze die aktuelle Session
		session = request.getSession();
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			// Chatfreunde
			new LadeChatFreundeServlet().setChatfreunde(request, response, con);
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
			// Ist der aktuelle User mit dem Besitzer des Profils befreundet?
			boolean privatSichtbar = false;
			// Lade Sql-Statement um zu ueberpruefen ob der Nutzer mit dem Besitzer der Seite befreundet ist
			String sql = sqlSt.getSqlStatement("Befreundet");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			pStmt.setInt(2, Integer.parseInt(session.getAttribute("UserID").toString()));
			ResultSet rs = pStmt.executeQuery();
			// Wenn er befreundet ist
			if (rs.next()) {
				// Kann er private Infos sehen
				privatSichtbar = true;
			}
			// Setze die UserID als request-Attribut
			request.setAttribute("userID", user);
			// Setze den Wahrheitswert, ob auf dem Profil Beitraege gepostet werden duerfen (eigenes Profil)
			request.setAttribute("beitragPosten", eigenesProfil);
			// Lade und setze alle Beitraege
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Profilseite", userID));
			// Lade Sql-Statement um die Sichtbarkeiten zu laden
			sql = sqlSt.getSqlStatement("Sichtbarkeiten");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			rs = pStmt.executeQuery();
			// Die Angaben, ob die Info oeffentlich sichtbar ist
			boolean geburtSichtbar = false;
			boolean wohnortSichtbar = false;
			boolean hobbysSichtbar = false;
			boolean interessenSichtbar = false;
			boolean ueberMichSichtbar = false;
			if (rs.next()) {
				geburtSichtbar = rs.getBoolean(1);
				wohnortSichtbar = rs.getBoolean(2);
				hobbysSichtbar = rs.getBoolean(3);
				interessenSichtbar = rs.getBoolean(4);
				ueberMichSichtbar = rs.getBoolean(5);
			}
			// Lade Sql-Statement um die Infos zu laden
			sql = sqlSt.getSqlStatement("Infos");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			rs = pStmt.executeQuery();
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
				if (rs.getString(5) != null && (privatSichtbar || geburtSichtbar)) {
					// Setze den Geburstag als Teil einer Aufzaehlung (vgl. getInfoString)
					request.setAttribute("geburtstag", getInfoString("Geburtstag:<br>" + sdf.format(new Date(rs.getDate(5).getTime()))));
				}
				// Wenn ein Wohnort eingetragen wurde
				if (rs.getString(6) != null && !rs.getString(6).equals("") && (privatSichtbar || wohnortSichtbar)) {
					request.setAttribute("wohnort", getInfoString("Wohnort:<br>" + rs.getString(6)));
				}
				// Wenn Hobbys eingetragen wurden
				if (rs.getString(7) != null && !rs.getString(7).equals("") && (privatSichtbar || hobbysSichtbar)) {
					request.setAttribute("hobbys", getInfoString("Hobbys:<br>" + rs.getString(7)));
				}
				// Wenn Interessen eingetragen wurden
				if (rs.getString(8) != null && !rs.getString(8).equals("") && (privatSichtbar || interessenSichtbar)) {
					request.setAttribute("interessen", getInfoString("Interessen:<br>" + rs.getString(8)));
				}
				// Wenn etwas ueber die eigene Persoenlichkeit eingetragen wurde
				if (rs.getString(9) != null && !rs.getString(9).equals("") && (privatSichtbar || ueberMichSichtbar)) {
					request.setAttribute("ueberMich", getInfoString("&Uuml;ber mich:<br>" + rs.getString(9)));
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
		} catch (NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
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
			// Chatfreunde
			new LadeChatFreundeServlet().setChatfreunde(request, response, con);
			// Je nach Aktion
			switch (name) {
			case "BeitragPosten":
				posteBeitrag(request, response);
				break;
			default:
				doGet(request, response);
				break;
			}
		} catch (NullPointerException npe) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
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
	 * @throws NullPointerException
	 */
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NullPointerException {
		// Die aktuelle Session
		session = request.getSession();
		// Stellt die Sql-Statements zur Verfuegung
		BeitragSql sqlSt = new BeitragSql();
		String beitrag = request.getAttribute("beitrag").toString();
		// Neues Verarbeitungsobjekt
		HashtagVerarbeitung hv = new HashtagVerarbeitung(beitrag);
		// Hole die Liste der Hashtags die gesetzt wurden
		List<String> hashtags = hv.getAndSetHashTags();
		// Hole die geaenderte Eingabe und ersetze entsprechend den Beitrag
		beitrag = hv.getEingabe();
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
			// Mit der BeitragsID koennen nun die Tags gesetzt werden
			hv.setHashTags(con, hashtags, beitragsID);
			// Weiterleitung
			response.sendRedirect("ProfilServlet");
		} else {
			System.err.println("Problem beim Anlegen des Beitrags");
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
