package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.javaClasses.ErstelleBenachrichtigung;
import de.dbae.uninet.javaClasses.Gruppe;
import de.dbae.uninet.javaClasses.HashtagVerarbeitung;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.GruppenSql;

/**
 * Dieses Servlet verarbeitet alle Anfragen, die mit Gruppen zu tun haben.
 * @author Christian Ackermann
 */
@WebServlet("/GruppenServlet")
public class GruppenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Das Session-Objekt.
	 */
	private HttpSession session;
	
	/**
	 * Die DB-Verbindung.
	 */
	private Connection con = null;
	
	/**
	 * Liefert die in diesem Servlet wichtigen Sql-Statements.
	 */
	private GruppenSql sqlSt = new GruppenSql();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GruppenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lade die Aktion, die vom Servlet durchgefuehrt werden soll
		String name = request.getParameter("name");
		// Wenn keine Aktion angegeben wurde
		if (name == null) {
			// Wird standardmaessig die Gruppe geladen
			name = "Gruppe";
		}
		// Neue DB-Verbindung oeffnen
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		// Setze die Session
		session = request.getSession();
		try { 
			// Chatfreunde
			new LadeChatFreundeServlet().setChatfreunde(request, response, con);
			// ID des aktuellen Users
			int userID = Integer.parseInt(session.getAttribute("UserID").toString());
			// Sql-Statement fuer die eigenen Gruppen (linke Spalte)
			String sql = sqlSt.getSqlStatement("EigeneGruppen");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			List<Gruppe> gruppen = new ArrayList<Gruppe>();
			// Fuer jede Gruppe
			while (rs.next()) {
				// Name der Gruppe
				String bezeichnung = rs.getString(1);
				// GruppenID
				int id = rs.getInt(2);
				// Fuege der Liste eine neue Gruppe hinzu
				gruppen.add(new Gruppe(id, bezeichnung));
			}
			// Setze die Gruppen-Liste als Attribut
			request.setAttribute("gruppenList", gruppen);
			// Je nach Aktion
			switch (name) {
			case "Gruppe":
				gruppe(request, response);
				break;
			case "Uebersicht":
				request.getRequestDispatcher("Gruppenuebersicht.jsp").forward(request, response);
				break;
			case "Verlassen":
				verlassen(request, response);
				break;
			default:
				break;
			}
		} catch (NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Beende die Verbindung
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		// Neue DB-Verbindung oeffnen
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		// Setze die Session
		session = request.getSession();
		// Lade die Aktion, die das Servlet durchfuehren soll
		String name = request.getAttribute("name").toString();
		if (name == null) {
			name = "";
		}
		try {
			// Chatfreunde
			new LadeChatFreundeServlet().setChatfreunde(request, response, con);
			
			// Je nach Aktion
			switch (name) {
			case "BeitragPosten":
				posteBeitrag(request, response);
				break;
			case "Gruenden":
				gruenden(request, response);
				break;
			case "BeschreibungBearbeiten":
				beschreibungAendern(request, response);
				break;
			case "MitgliedZufuegen":
				mitgliederBearbeiten(request, response, true);
				break;
			case "MitgliedEntfernen":
				mitgliederBearbeiten(request, response, false);
				break;
			case "GruppeLoeschen":
				loeschen(request, response);
				break;
			default:
				doGet(request, response);
				break;
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im GruppenServlet");
			// TODO Fehler
			e.printStackTrace();
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
	}
	
	/**
	 * L&auml;dt alle Informationen einer Gruppe.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void gruppe(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, NullPointerException {
		// ID des aktuellen Users
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// GruppenID lesen und setzen
		int id = Integer.parseInt(request.getParameter("gruppenID"));
		request.setAttribute("gruppenID", id);
		// Aktuellen Tab lesen und setzen
		String tab = request.getParameter("tab");
		request.setAttribute("tab", tab);
		// Lade das Sql-Statement
		String sql = sqlSt.getSqlStatement("Infos");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next()) {
			// Gruppenname
			String name = rs.getString(1);
			// Gruppenbeschreibung
			String beschreibung = rs.getString(2);
			// Formatierung des Zeitstempels
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String gruendung = sdf.format(new Date(rs.getDate(3).getTime()));
			// Name des Admins
			String admin = rs.getString(4) + " " + rs.getString(5);
			// userID des Gruppenadmins
			int adminID = rs.getInt(6);
			// Neues Gruppenobjekt anlegen
			Gruppe gruppe = new Gruppe(id, name, beschreibung, gruendung, admin, adminID);
			// Gruppenobjekt als Attribut setzen
			request.setAttribute("gruppe", gruppe);
		}
		// UserID setzen
		request.setAttribute("userID", userID);
		// Je nach Tab werden verschiedene Dinge geladen
		switch (tab) {
		case "beitraege":
			// Setze den Tab auf Active
			request.setAttribute("beitraegeActive", "active");
			// Setze eine Liste von Beitraegen als Attribut
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Gruppen", userID, id + ""));
			break;
		case "infos":
			// Setze den Tab auf Active
			request.setAttribute("infosActive", "active");
			break;
		case "mitglieder":
			// Setze den Tab auf Active
			request.setAttribute("mitgliederActive", "active");
			// Setze eine Liste von Mitgliedern als Attribut
			request.setAttribute("mitglieder", getMitglieder(request));
			break;
		case "bearbeiten":
			// Setze den Tab auf Active
			request.setAttribute("bearbeitenActive", "active");
			// Setze eine Liste von Mitgliedern als Attribut
			request.setAttribute("mitglieder", getMitglieder(request));
			// Setze eine Liste von Freunden als Attribut
			request.setAttribute("freunde", getFreunde(request));
		default:
			break;
		}
		// Weiterleitung
		request.getRequestDispatcher("Gruppen.jsp").forward(request, response);
	}
	
	/**
	 * Speichert einen Beitrag in der Datenbank und postet ihn in der Gruppe.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 */
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// Klasse um die Sql-Statements zu bekommen
		BeitragSql sqlSt = new BeitragSql();
		// Der Beitrag der gepostet werden soll
		String beitrag = request.getAttribute("beitrag").toString();
		// Neues Verarbeitungsobjekt
		HashtagVerarbeitung hv = new HashtagVerarbeitung(beitrag);
		// Hole die Liste der Hashtags die gesetzt wurden
		List<String> hashtags = hv.getAndSetHashTags();
		// Hole die geaenderte Eingabe und ersetze entsprechend den Beitrag
		beitrag = hv.getEingabe();
		// Die ID des Verfassers
		int verfasserID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Die Sichtbarkeit des Beitrags
		String sichtbarkeit = request.getParameter("sichtbarkeit");
		// boolean Sichtbarkeit (true: oeffentlich, false: privat)
		boolean sichtbar = true;
		if (sichtbarkeit.startsWith("Privat")) {
			sichtbar = false;
		}
		// Lade Sql-Statement
		String sql = sqlSt.getSqlStatement("BeitragAnlegen1");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, beitrag);
		pStmt.setInt(2, verfasserID);
		pStmt.setBoolean(3, sichtbar);
		// Zeitstempel setzen
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		// Sql-Statement um die BeitragsID des neu angelegten Beitrags zu bekommen
		sql = sqlSt.getSqlStatement("BeitragAnlegen2");
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int beitragsID;
		if (rs.next()) {
			beitragsID = rs.getInt(1);
			// Sql-Statement um einen Gruppenbeitrag einzutragen
			sql = sqlSt.getSqlStatement("BeitragAnlegenGruppe");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			int gruppenID = Integer.parseInt(request.getParameter("gruppenID"));
			pStmt.setInt(2, gruppenID);
			pStmt.executeUpdate();
			// Mit der BeitragsID koennen jetzt die Hashtags gesetzt werden
			hv.setHashTags(con, hashtags, beitragsID);
			// Benachrichtigungen erstellen
			new ErstelleBenachrichtigung(con).gruppenveranstaltungenpost(verfasserID, beitragsID, gruppenID, true);
			// Weiterleitung
			response.sendRedirect("GruppenServlet?tab=beitraege&gruppenID=" + gruppenID);
		} else {
			System.err.println("Problem beim Anlegen des Beitrags");
			// TODO Fehler
		}
	}
	
	/**
	 * Entfernt ein Gruppenmitglied aus der Gruppe.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void verlassen(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NullPointerException {
		// Die ID des aktuellen Users
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Die ID der Gruppe, aus der der Student austreten will
		int id = Integer.parseInt(request.getParameter("gruppenID"));
		// Lade Sql-Statement um ein Mitglied zu entfernen
		String sql = sqlSt.getSqlStatement("MitgliederEntfernen");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		// War der User Admin der Gruppe?
		sql = sqlSt.getSqlStatement("IstAdmin");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next()) {
			// Wenn der User Admin war
			if (rs.getInt(1) > 0) {
				// Muss der engste Freund innerhalb der Gruppe ermittelt werden
				sql = sqlSt.getSqlStatement("EngsterFreundInGruppe");
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, userID);
				pStmt.setInt(2, id);
				pStmt.setInt(3, userID);
				pStmt.setInt(4, id);
				ResultSet rs2 = pStmt.executeQuery();
				int neueAdminID = -1;
				// Wenn es einen Freund gibt
				if (rs2.next()) {
					// Setze ihn als neuen Admin
					neueAdminID = rs2.getInt(1);
				// Wenn es keinen Freund mehr innerhalb der Gruppe gibt
				} else {
					sql = sqlSt.getSqlStatement("Mitglieder") + "studentID";
					pStmt = con.prepareStatement(sql);
					pStmt.setInt(1, id);
					rs2 = pStmt.executeQuery();
					// Wenn es noch ein Mitglied gibt
					if (rs2.next()) {
						// Setze das erste Mitglied als neuen Admin
						neueAdminID = rs2.getInt(3);
					// Wenn es keinen Admin mehr gibt
					} else {
						// Loesche die ganze Gruppe
						sql = sqlSt.getSqlStatement("GruppeLoeschen");
						pStmt = con.prepareStatement(sql);
						pStmt.setInt(1, id);
						pStmt.executeUpdate();
					}
				}
				// Wenn es einen neuen Admin gibt
				if (neueAdminID != -1) {
					// Setze den neuen Admin
					sql = sqlSt.getSqlStatement("AdminIDSetzen");
					pStmt = con.prepareStatement(sql);
					pStmt.setInt(1, neueAdminID);
					pStmt.setInt(2, id);
					pStmt.executeUpdate();
					// Und informiere ihn darueber
					new ErstelleBenachrichtigung(con).gruppenAdmin(neueAdminID, id);
				}
			}
		}
		// Weiterleitung
		response.sendRedirect("GruppenServlet?name=Uebersicht");
	}
	
	/**
	 * Gibt eine Liste von Mitgliedern dieser Gruppe zur&uuml;ck.
	 * @param request Das Request-Objekt
	 * @return Die Liste von Mitgliedern
	 * @throws SQLException
	 */
	private List<Student> getMitglieder(HttpServletRequest request) throws SQLException {
		// Sortierung nach Vorname
		boolean sortByV = true;
		// Sortierung nach Nachname
		if (request.getParameter("sortByV") != null && request.getParameter("sortByV").equals("false")) {
			sortByV = false;
		}
		// Liste fuer die Mitglieder
		List<Student> mitglieder = new ArrayList<Student>();
		// Lade Sql-Statement
		String sql = sqlSt.getSqlStatement("Mitglieder");
		// Angegebene Sortierung
		if (sortByV) {
			sql += "Vorname, Nachname";
			request.setAttribute("vornameLink", "text-decoration: underline");
		} else {
			sql += "Nachname, Vorname";
			request.setAttribute("nachnameLink", "text-decoration: underline");
		}
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, Integer.parseInt(request.getParameter("gruppenID")));
		ResultSet rs = pStmt.executeQuery();
		// Fuer jedes Mitglied
		while(rs.next()) {
			String vorname = rs.getString(1);
			String nachname = rs.getString(2);
			int userID = rs.getInt(3);
			// Fuege ein neues Mitglied der Liste hinzu
			mitglieder.add(new Student(vorname, nachname, userID));
		}
		// Setze die Anzahl der Mitglieder als Attribut
		request.setAttribute("anzahlMitglieder", mitglieder.size());
		return mitglieder;
	}
	
	/**
	 * Tr&auml;gt eine neue Gruppe in der Datenbank ein.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void gruenden(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NullPointerException {
		// Die ID des aktuellen Benutzers
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Lade das Sql-Statement um eine Gruppe zu gruenden
		String sql = sqlSt.getSqlStatement("GruppeGruenden");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, request.getParameter("gruppenname"));
		pStmt.setString(2, "");
		// Gruendungstag
		pStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		// Der Student, der die Gruppe anlegt, ist automatisch Admin
		pStmt.setInt(4, userID);
		pStmt.executeUpdate();
		// Lade Sql-Statement um an die neue GruppenID zu kommen
		sql = sqlSt.getSqlStatement("NeueGruppenID");
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int id = 1;
		if (rs.next()) {
			id = rs.getInt(1);
		}
		// Lade Sql-Statement um den Admin der Gruppe hinzuzufuegen
		sql = sqlSt.getSqlStatement("MitgliederZufuegen");
		pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		// Weiterleitung
		response.sendRedirect("GruppenServlet?tab=bearbeiten&gruppenID=" + id);
	}
	
	/**
	 * L&ouml;scht eine Gruppe.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 */
	private void loeschen(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// Lade Sql-Statement um die Gruppe zu loeschen
		String sql = sqlSt.getSqlStatement("GruppeLoeschen");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, Integer.parseInt(request.getParameter("gruppenID")));
		pStmt.executeUpdate();
		// Weiterleitung zurueck zur Gruppenuebersicht
		response.sendRedirect("GruppenServlet?name=Uebersicht");
	}
	
	/**
	 * &Auml;ndert die Beschreibung der Gruppe.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 */
	private void beschreibungAendern(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// Lade Sql-Statement um die Beschreibung zu aendern
		String sql = sqlSt.getSqlStatement("BeschreibungAendern");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, request.getParameter("beschreibung"));
		pStmt.setInt(2, Integer.parseInt(request.getParameter("gruppenID")));
		pStmt.executeUpdate();
		// Weiterleitung
		response.sendRedirect("GruppenServlet?tab=infos&gruppenID=" + request.getParameter("gruppenID"));
	}
	
	/**
	 * Bearbeitet die Mitglieder der Gruppe.<br>
	 * In Abh&auml;ngigkeit des Attributs <b>zufuegen</b> wird der angegebene Student entweder zugef&uuml;gt oder entfernt.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @param zufuegen Wahrheitswert, ob der Student zgef&uuml;gt oder entfernt werden soll
	 * @throws SQLException
	 * @throws IOException
	 */
	private void mitgliederBearbeiten(HttpServletRequest request, HttpServletResponse response, boolean zufuegen) throws SQLException, IOException {
		// Lade Sql-Statement um einen Studenten hinzuzufuegen
		String sql = sqlSt.getSqlStatement("MitgliederZufuegen");
		// Wenn der Student entfernt werden soll
		if (!zufuegen) {
			// Lade Sql-Statement um ein Mitglied zu entfernen
			sql = sqlSt.getSqlStatement("MitgliederEntfernen");
		}
		int gruppenID = Integer.parseInt(request.getParameter("gruppenID"));
		// Die ID des Studenten, der zugefuegt oder entfernt werden soll
		int userID = Integer.parseInt(request.getParameter("mitgliedID"));
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, gruppenID);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		if (zufuegen) {
			// Benachrichtige den Benutzer
			new ErstelleBenachrichtigung(con).gruppeneinladung(Integer.parseInt(request.getSession().getAttribute("UserID").toString()), gruppenID, userID);
		}
		// Weiterleitung
		response.sendRedirect("GruppenServlet?gruppenID=" + gruppenID + "&tab=bearbeiten");
	}
	
	/**
	 * Gibt eine Liste von Studenten zur&uuml;ck, die der Gruppe hinzugef&uuml;gt werden k&ouml;nnen.<br>
	 * Um zugef&uuml;gt werden zu k&ouml;nnen, muss man mit mindestens einem Gruppenmitglied befreundet sein.<br>
	 * Ist ein Student bereits Mitglied der Gruppe, wird er in der Liste natr&uuml;rlich nicht aufgef&uuml;hrt.
	 * @param request Das Request-Objekt
	 * @return Die Liste der potentiellen Mitglieder
	 * @throws SQLException
	 */
	private List<Student> getFreunde(HttpServletRequest request) throws SQLException {
		// Liste der Freunde der Gruppenmitglieder
		List<Student> freunde = new ArrayList<Student>();
		// Lade Sql-Statement um alle Freunde der Gruppenmitglieder zu bekommen
		String sql = sqlSt.getSqlStatement("MoeglicheMitglieder");
		int gruppenID = Integer.parseInt(request.getParameter("gruppenID"));
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, gruppenID);
		ResultSet rs = pStmt.executeQuery();
		// Fuer alle Freunde
		while (rs.next()) {
			// StudentID
			int id = rs.getInt(1);
			String vorname = rs.getString(2);
			String nachname = rs.getString(3);
			// Wahrheitswert, ob der Student bereits Mitglied ist
			boolean mitglied = false;
			// Iteriere ueber alle Gruppenmitglieder
			for (Student student : getMitglieder(request)) {
				// Wenn die MitgliedsID mit der StudentID uebereinstimmt
				if (student.getUserID() == id)
					// Ist der Student bereits Mitglied der Gruppe
					mitglied = true;
			}
			// Wenn er kein Mitglied ist
			if (!mitglied)
				// Kann er der Liste der potentiellen neuen Mitglieder zugefuegt werden
				freunde.add(new Student(vorname, nachname, id));
		}
		return freunde;
	}

}
