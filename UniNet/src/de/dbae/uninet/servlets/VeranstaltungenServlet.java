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
import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.javaClasses.Veranstaltung;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.VeranstaltungenSql;

/**
 * Dieses Servlet verarbeitet alle Anfragen die sich auf Veranstaltungen beziehen.
 * @author Christian Ackermann
 */
@WebServlet("/VeranstaltungenServlet")
public class VeranstaltungenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Die aktuelle Session.
	 */
	private HttpSession session;
	
	/**
	 * Die DB-Verbindung.
	 */
	private Connection con = null;
	
	/**
	 * Stellt die Sql-Statements zur Verf&uuml;gung.
	 */
	private VeranstaltungenSql sqlSt = new VeranstaltungenSql();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VeranstaltungenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
		
		// Die Aktion, die vom Servlet ausgefuhrt werden soll
		String name = request.getParameter("name");
		// Wenn keine Aktion angegeben wurde
		if (name == null) {
			// Lade die Veranstaltung
			name = "Veranstaltung";
		}
		// Die ID des aktuellen Users
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// Oeffne eine neue Verbindung
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			// Lade Sql-Statement um die eigenen Veranstaltungen (linke Spalte) zu laden
			String sql = sqlSt.getSqlStatement("EigeneVeranstaltungen");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			// Liste der eigenen Veranstaltungen
			List<Veranstaltung> veranstaltungList = new ArrayList<Veranstaltung>();
			// Fuer jede Veranstaltung
			while (rs.next()) {
				// VeranstaltungsID
				int id = rs.getInt(1);
				// Name der Veranstaltung
				String bezeichnung = rs.getString(2);
				// Neue Veranstaltung anlegen und der Liste zufuegen
				veranstaltungList.add(new Veranstaltung(id, bezeichnung));
			}
			// Die Liste als Attribut setzen
			request.setAttribute("veranstaltungList", veranstaltungList);
			// Je nach Aktion
			switch (name) {
			case "Veranstaltung":
				veranstaltung(request, response);
				break;
			case "Uebersicht":
			case "Suche":
				request.setAttribute("tab", "infos");
				uebersicht(request, response);
				break;
			case "Einschreiben":
				einAusSchreiben(request, response, true);
				break;
			case "Ausschreiben":
				einAusSchreiben(request, response, false);
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			System.err.println("SQL Fehler im VeranstaltungenSerlet");
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
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
		
		// Oeffne eine neue DB-Verbindung
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		// Die Aktion, die das Servlet durchfuehren soll
		String name = request.getParameter("name");
		if (name == null) {
			name = "";
		}
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
			System.out.println("Fehler in VeranstaltungenServlet");
			// TODO Fehler
		} finally {
			// Verbindung schliessen
			dbcon.close();
		}
		
	}
	
	/**
	 * L&auml;dt alle Veranstaltungen und stellt sie zur Verf&uuml;gung.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void uebersicht(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, NullPointerException {
		// Wahrheitswert ob nach einer speziellen Veranstaltung gesucht wurde oder nicht
		boolean suche = request.getParameter("suche") != null;
		// Setze die Session
		session = request.getSession();
		// Die ID des aktuellen Users
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Lade Sql-Statement um alle Veranstaltungen zu bekommen
		String sql = sqlSt.getSqlStatement("AlleVeranstaltungen");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, userID);
		ResultSet rs = pStmt.executeQuery();
		// Liste aller Veranstaltungen
		List<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
		// Fuer jede Veranstaltung
		while (rs.next()) {
			// VeranstaltungsID
			int id = rs.getInt(1);
			// Veranstaltungsname
			String bezeichnung = rs.getString(2);
			// Wahrheitswert, ob die Veranstaltung der Suche entspricht
			boolean anzeigen = false;
			// Wenn es eine Sucheinschraenkung gibt
			if (suche) {
				// Suchparameter
				String such = request.getParameter("suche");
				// Wenn das Suchwort in dem Veranstaltungsnamen vorkommt
				if (bezeichnung.toLowerCase().contains(such.toLowerCase())) {
					// Soll die Veranstaltung angezeigt werden
					anzeigen = true;
				}
			}
			// Wenn nicht gesucht wurde oder die Veranstaltung angezeigt werden darf
			if (!suche || anzeigen) {
				// Erstelle eine neue Veranstaltung und fuege sie der Liste hinzu
				veranstaltungen.add(new Veranstaltung(id, bezeichnung));
			}
		}
		// Setze die VeranstaltungsListe als Attribut
		request.setAttribute("veranstaltungen", veranstaltungen);
		// Weiterleitung
		request.getRequestDispatcher("Veranstaltungsuebersicht.jsp").forward(request, response);
	}
	
	/**
	 * L&auml;dt alle relevanten Informationen einer Veranstaltung.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	@SuppressWarnings("unchecked")
	private void veranstaltung(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, NullPointerException {
		// Setze die aktuelle Session
		session = request.getSession();
		// Die ID des aktuellen Users
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// VeranstaltungsID lesen und setzen
		int id = Integer.parseInt(request.getParameter("veranstaltungsID"));
		request.setAttribute("veranstaltungsID", id);
		// Aktuellen Tab lesen und setzen
		String tab = request.getParameter("tab");
		request.setAttribute("tab", tab);
		// Kontrolle, ob man bereits in der Veranstaltung eingeschrieben ist
		boolean eingetragen = false;
		for (Veranstaltung veranstaltung : (ArrayList<Veranstaltung>)request.getAttribute("veranstaltungList")) {
			// Wenn die VeranstaltungsID der aktuellen ID entspricht
			if (veranstaltung.getId() == id) {
				// Ist man bereits in dieser Veranstaltung eingeschrieben
				eingetragen = true;
			}
		}
		// Setze das Attribut, ob man bereits eingetragen ist oder nicht
		request.setAttribute("eingetragen", eingetragen);
		// Lade Sql-Statement um alle Infos zu laden
		String sql = sqlSt.getSqlStatement("Infos");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next()) {
			// Veranstaltungsname
			String name = rs.getString(1);
			String semester = rs.getString(2);
			String dozent = rs.getString(3);
			String beschreibung = rs.getString(4);
			String sonstiges = rs.getString(5);
			// Erstelle eine neue Veranstaltung und fuege sie der Liste hinzu
			request.setAttribute("veranstaltung", new Veranstaltung(id, name, dozent, semester, beschreibung, sonstiges));
		}
		// Je nach Tab
		switch (tab) {
		case "beitraege":
			// Wenn man eingetragen ist
			if (eingetragen) {
				// Sieht man die Beitraege, welche als List geladen und gesetzt werden
				request.setAttribute("beitraegeActive", "active");
				request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Veranstaltungen", userID, id + ""));
			} else {
				// Ansonsten sind die Beitraege nicht sichtbar
				request.setAttribute("infosActive", "active");
				request.setAttribute("tab", "infos");
			}
			break;
		case "infos":
			request.setAttribute("infosActive", "active");
			break;
		case "mitglieder":
			// Die Veranstaltungsmitglieder werden geladen und gesetzt
			request.setAttribute("mitglieder", getMitglieder(request, con));
			request.setAttribute("mitgliederActive", "active");
			break;
		default:
			break;
		}
		// Weiterleitung
		request.getRequestDispatcher("Veranstaltungen.jsp").forward(request, response);
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
		// Stellt die Sql-Statements zur Verfuegung
		BeitragSql sqlSt = new BeitragSql();
		String beitrag = request.getAttribute("beitrag").toString();
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
		// Setze Zeitstempel (jetzt)
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		// Lade Sql-Statement um an die neue BeitragsID zu kommen
		sql = sqlSt.getSqlStatement("BeitragAnlegen2");
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int beitragsID;
		if (rs.next()) {
			beitragsID = rs.getInt(1);
			// Lade Sql-Statement um den Beitrag als VeranstaltungsBeitrag einzutragen
			sql = sqlSt.getSqlStatement("BeitragAnlegenVeranstaltung");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			int veranstaltungsID = Integer.parseInt(request.getParameter("veranstaltungsID"));
			pStmt.setInt(2, veranstaltungsID);
			pStmt.executeUpdate();
			// Weiterleitung
			response.sendRedirect("VeranstaltungenServlet?tab=beitraege&veranstaltungsID=" + veranstaltungsID);
		} else {
			System.err.println("Problem beim Anlegen des Beitrags");
			// TODO Fehler
		}
	}
	
	/**
	 * Schreibt einen Studenten in eine Veranstaltung ein oder aus einer aus.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @param einschreiben Wahrheitswert ob der Student ein- oder ausgeschrieben werden soll
	 * @throws SQLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void einAusSchreiben(HttpServletRequest request, HttpServletResponse response, boolean einschreiben) throws SQLException, IOException, NullPointerException {
		// Setze aktuelle Session
		session = request.getSession();
		// ID des aktuellen Users
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sql;
		// Beim Ausschreiben wird auf die Uebersicht weitergeleitet
		String page = "VeranstaltungenServlet?name=Uebersicht";
		int id = Integer.parseInt(request.getParameter("veranstaltungsID"));
		if (einschreiben) {
			sql = sqlSt.getSqlStatement("Einschreiben");
			// Beim Einschreiben wird auf die Beitraege der Veranstaltung weitergeleitet
			page = "VeranstaltungenServlet?name=Veranstaltung&veranstaltungsID=" + id + "&tab=beitraege";
		} else
			sql = sqlSt.getSqlStatement("Ausschreiben");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, id);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();	
		// Weiterleitung
		response.sendRedirect(page);	
	}
	
	/**
	 * Gibt eine Liste der Mitglieder der Veranstaltung zur&uuml;ck.
	 * @param request Das Request-Objekt
	 * @param con Die DB-Verbindung
	 * @return Die Liste der Mitglieder
	 * @throws SQLException
	 */
	private List<Student> getMitglieder(HttpServletRequest request, Connection con) throws SQLException {
		// Nach Vorname sortiert
		boolean sortByV = true;
		// Nach Nachname sortiert
		if (request.getParameter("sortByV") != null && request.getParameter("sortByV").equals("false")) {
			sortByV = false;
		}
		// Liste der Mitglieder
		List<Student> mitglieder = new ArrayList<Student>();
		// Lade Sql-Statement um die Mitglieder zu bekommen
		String sql = sqlSt.getSqlStatement("Mitglieder");
		// Je nach Sortierung
		if (sortByV) {
			sql += "Vorname, Nachname";
			request.setAttribute("vornameLink", "text-decoration: underline");
		} else {
			sql += "Nachname, Vorname";
			request.setAttribute("nachnameLink", "text-decoration: underline");
		}
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, Integer.parseInt(request.getParameter("veranstaltungsID")));
		ResultSet rs = pStmt.executeQuery();
		// Fuer jedes Mitglied
		while(rs.next()) {
			String vorname = rs.getString(1);
			String nachname = rs.getString(2);
			int userID = rs.getInt(3);
			// Erstelle einen neuen Studenten und fuege ihn der Liste hinzu
			mitglieder.add(new Student(vorname, nachname, userID));
		}
		// Setze die Anzahl der Mitglieder
		request.setAttribute("anzahl", mitglieder.size());
		return mitglieder;
	}
}
