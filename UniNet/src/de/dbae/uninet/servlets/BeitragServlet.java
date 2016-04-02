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
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.javaClasses.Emoticon;
import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.javaClasses.Kommentar;
import de.dbae.uninet.javaClasses.KommentarZuUnterkommentar;
import de.dbae.uninet.javaClasses.Unterkommentar;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.GruppenSql;
import de.dbae.uninet.sqlClasses.ProfilSql;
import de.dbae.uninet.sqlClasses.StartseiteSql;
import de.dbae.uninet.sqlClasses.VeranstaltungenSql;

/**
 * Dieses Servlet verarbeitet alle Anfragen, die sich auf Beitr&auml;ge beziehen.<br>
 * @see Beitrag
 * @author Christian Ackermann
 */
@WebServlet("/BeitragServlet")
public class BeitragServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Datenbankverbindung.
	 */
	private Connection con = null;
	
	/**
	 * Liefert die in diesem Servlet wichtigen Sql-Statements.
	 */
	private BeitragSql sqlSt = new BeitragSql();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BeitragServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lade Chatfreunde
	    new LadeChatFreundeServlet().setChatfreunde(request);
				
		// Lade die Aktion, die vom Servlet durchgefuehrt werden soll
		String name = request.getParameter("name");
		// Standardmaessig soll der Beitrag und nicht die Like-Liste angezeigt werden
		request.setAttribute("beitragAnzeigen", true);
		HttpSession session = request.getSession();
		// Setze das Attribut, ob das Antworttextfeld angezeigt werden soll oder nicht
		if (request.getParameter("anzeigen") == null) {
			request.setAttribute("anzeigen", false);
		} else {
			request.setAttribute("anzeigen", true);
			name = null;
		}
		// Neue DB-Verbindung oeffnen
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			// Wenn keine Aktion angegeben wurde
			if (name == null) {
				// Lade den kompletten Beitrag und zeige ihn an
				kompletterLoad(request, response, session);
			} else {
				// Je nach angegebener Aktion
				switch (name) {
				case "Like":
					// Like den Beitrag und verweise zurueck an die aufrufende Seite
					likeBeitrag(request, response);
					break;
				case "Kommentar":
					// Speichere den Kommentar und verweise zurueck an die aufrufende Seite
					kommentieren(request, response);
					break;
				case "BeitragLoeschen":
					// Loesche den Beitrag und verweise auf die Startseite
					beitragLoeschen(request, response);
					break;
				case "KommentarLoeschen":
					// Loesche den Kommentar
					kommentarLoeschen(request, response, "kommentar");
					break;
				case "KommLoeschen":
					// Loesche den Unterkommentar
					kommentarLoeschen(request, response, "komm");
					break;
				case "KzukommLoeschen":
					// Loesche den Kommentar zum Unterkommentar
					kommentarLoeschen(request, response, "kzukomm");
					break;
				case "BeitragBearbeiten":
					// Lade den Beitrag komplett mit Textfeld statt Nachricht
					kompletterLoad(request, response, session, true);
					break;
				case "AntwortAufKommentar":
					// Zeige das Antwort-Textfeld an (auf Kommentar)
					antwortVorbereiten(request, response, "kommentar");
					break;
				case "AntwortAufKomm":
					// Zeige das Antwort-Textfeld an (auf Unterkommentar)
					antwortVorbereiten(request, response, "komm");
					break;
				case "KommentarAntwort":
					// Antworte auf einen Kommentar
					kommentarAntworten(request, response);
					break;
				case "Melden":
					// Melde den Beitrag 
					melden(request, response);
					break;
				case "LikesAnzeigen":
					// Zeige die Personen an, die den Beitrag mit 'interessiert mich nicht besonders' markiert haben
					likesAnzeigen(request, response);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im BeitragServlet");
			// TODO Fehler
			e.printStackTrace();
		} finally {
			// Schliesse die Verbindung 
			dbcon.close();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lade Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
				
		// Standardmaessig soll der Beitrag und nicht die Like-Liste angezeigt werden
		request.setAttribute("beitragAnzeigen", true);
		// Lade die Aktion, die vom Servlet durchgefuehrt werden soll
		String name = request.getParameter("name");
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			if (name == null) {
				kompletterLoad(request, response, request.getSession());
			} else {
				switch (name) {
				case "BeitragBearbeitet":
					String sql = sqlSt.getSqlStatement("BeitragBearbeiten");
					PreparedStatement pStmt = con.prepareStatement(sql);
					pStmt.setString(1, request.getAttribute("neuerBeitrag").toString());
					pStmt.setInt(2, Integer.parseInt(request.getParameter("beitragsID")));
					pStmt.executeUpdate();
					kompletterLoad(request, response, request.getSession());
					break;
				default:
					doGet(request, response);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Fehler
		} finally {
			dbcon.close();
		}
		
	}
	
	/**
	 * L&auml;dt den Beitrag und zeigt ihn an.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @param session Das aktuelle Session-Objekt
	 * @param optionalParam True, wenn Beitrag bearbeitet werden soll
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void kompletterLoad(HttpServletRequest request, HttpServletResponse response, HttpSession session, boolean... optionalParam) throws SQLException, ServletException, IOException {
		// Die ID des aktuellen Nutzers
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		// Die BeitragsID
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		// Lade den Beitrag
		String sql = sqlSt.getSqlStatement("Beitrag");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		ResultSet rs = pStmt.executeQuery();
		Beitrag beitrag;
		if (rs.next()) {
			// BeitragsID
			int id = rs.getInt(1);
			// Verfasser-Name
			String name = rs.getString(2) + " " + rs.getString(3);
			// Der Beitrag
			String nachricht = rs.getString(4);
			int anzahlLikes = rs.getInt(5);
			int anzahlKommentare = rs.getInt(6);
			// Formatierung des Zeitstempels
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String timeStamp = sdf.format(new Date(rs.getDate(7).getTime())) + " " + rs.getTime(8).toString();
			// Sichtbarkeit
			if (rs.getBoolean(9)) {
				// Oeffentlich
				timeStamp += " <span class='glyphicon glyphicon-globe'></span>";
			} else {
				// Privat
				timeStamp += " <span class='glyphicon glyphicon-user'></span>";
			}
			// Bearbeitet
			boolean bearbeitet = rs.getBoolean(10);
			// Hat man den Beitrag selbst mit 'interessiert mich nicht besonders' markiert?
			sql = sqlSt.getSqlStatement("Like");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setInt(2, userID);
			ResultSet rs2 = pStmt.executeQuery();
			if (rs2.next()) {
				boolean like = rs2.getInt(1) == 0 ? false : true;
				// Man darf den Beitrag loeschen, wenn es der eigene Beitrag ist
				boolean loeschenErlaubt = userID == id;
				// Neues Beitragsobjekt
				beitrag = new Beitrag(id, name, timeStamp, nachricht, anzahlLikes, anzahlKommentare, beitragsID, like, loeschenErlaubt, bearbeitet);
				// Kommentare laden
				beitrag.setKommentarList(getKommentare(beitragsID, userID));
				sql = sqlSt.getSqlStatement("OrtName");
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				ResultSet rs3 = pStmt.executeQuery();
				// Fuer den Fall dass es kein Chronikbeitrag ist
				if (rs3.next()) {
					// Setze den Wahrheitswert und den Ort des Beitrags
					beitrag.setNichtChronik(true);
					beitrag.setOrtName(rs3.getString(1));
				}
				// Grammatikalische Unterscheidung fuer den Sonderfall Anzahl = 1
				request.setAttribute("beitragLikesPersonen", beitrag.getLikes() == 1 ? "Eine Person" : beitrag.getLikes() + " Personen");
				request.setAttribute("beitragKommentare", beitrag.getKommentare() == 1 ? "1 Kommentar" : beitrag.getKommentare() + " Kommentare");
				request.setAttribute("beitrag", beitrag);
				// Wenn der Beitrag bearbeitet werden soll
				if (optionalParam.length > 0) {
					// Setze das entsprechende Attribut
					request.setAttribute("beitragBearbeiten", optionalParam[0]);
					// Aendere die Darstellung der Emoticons
					beitrag.setNachricht(inverseEmoticonFilter(beitrag.getNachricht()));
				}
				// Wenn man den Beitrag selbst markiert hat
				if (like) {
					// Wird das angezeigt
					request.setAttribute("liClass", "geliket");
				} else {
					request.setAttribute("liClass", "");
				}
				// Weiterleitung
				request.getRequestDispatcher("Beitrag.jsp").forward(request, response);
			}
		}
	}
	
	/**
	 * L&auml;dt alle Kommentare zum Beitrag und gibt sie als Liste von Kommentaren zur&uuml;ck.
	 * @param con Die ge&ouml;ffnete DB-Verbindung
	 * @param beitragsID Die entsprechende BeitragsID
	 * @param userIDsession Die ID des aktuellen Users
	 * @return Die Liste der Kommentare zum Beitrag
	 * @throws SQLException
	 */
	private List<Kommentar> getKommentare(int beitragsID, int userIDsession) throws SQLException {
		// Neue Liste anlegen
		List<Kommentar> kommentarList = new ArrayList<Kommentar>();
		// Lade die Kommentare aus der DB
		String sql = sqlSt.getSqlStatement("Kommentare");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		ResultSet rs = pStmt.executeQuery();
		// Fuer jeden Kommentar
		while (rs.next()) {
			// VerfasserID
			int userID = rs.getInt(1);
			// KommentarID
			int kommID = rs.getInt(2);
			// Verfasser-Name
			String name = rs.getString(3) + " " + rs.getString(4);
			// Der eigentliche Kommentar
			String kommentar = rs.getString(5);
			// Formatierung des Zeitstempels
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String timeStamp = sdf.format(new Date(rs.getDate(6).getTime())) + " " + rs.getTime(7).toString();
			// Laden der Unterkommentare
			sql = sqlSt.getSqlStatement("Unterkommentare");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, kommID);
			ResultSet rs2 = pStmt.executeQuery();
			// Neue Liste fuer die Unterkommentare
			List<Unterkommentar> unterkommentarList = new ArrayList<Unterkommentar>();
			// Fuer jeden Unterkommentar
			while (rs2.next()) {
				// VerfasserID
				int userID2 = rs2.getInt(1);
				// KommentarID
				int kommID2 = rs2.getInt(2);
				// Verfassername
				String name2 = rs2.getString(3) + " " + rs2.getString(4);
				// Der eigentliche Unterkommentar
				String kommentar2 = rs2.getString(5);
				// Formatierung des Zeitstempels
				String timeStamp2 = sdf.format(new Date(rs2.getDate(6).getTime())) + " " + rs2.getTime(7).toString();
				// Neuer Unterkommentar
				Unterkommentar ukomm = new Unterkommentar(userID2, kommID2, name2, kommentar2, kommID, timeStamp2);
				// Lade Kommentare zum Unterkommentar
				sql = sqlSt.getSqlStatement("KommentareZuUnterkommentaren");
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, kommID2);
				ResultSet rs3 = pStmt.executeQuery();
				// Neue Liste mit Kommentaren zu dem Unterkommentar
				List<KommentarZuUnterkommentar> kzukommList = new ArrayList<KommentarZuUnterkommentar>();
				// Fuer jeden Kommentar
				while (rs3.next()) {
					// VerfasserID
					int userID3 = rs3.getInt(1);
					// KommentarID
					int kommID3 = rs3.getInt(2);
					// Verfassername
					String name3 = rs3.getString(3) + " " + rs3.getString(4);
					// Der eigentliche Kommmentar zum Unterkommentar
					String kommentar3 = rs3.getString(5);
					// Formatierung des Zeitstempels
					String timeStamp3 = sdf.format(new Date(rs3.getDate(6).getTime())) + " " + rs3.getTime(7).toString();
					// Fuege den Kommentar der Liste zu
					kzukommList.add(new KommentarZuUnterkommentar(userID3, kommID3, name3, kommentar3, name2, userID2, timeStamp3));
				}
				// Setze die Liste aller Kommentare zum Unterkommentar 
				ukomm.setKommentarList(kzukommList);
				// Fuege den Unterkommentar der Liste hinzu
				unterkommentarList.add(ukomm);
			}
			// Neuer Kommentar
			Kommentar komm = new Kommentar(userID, kommID, name, kommentar, unterkommentarList, beitragsID, userIDsession, timeStamp);
			// Fuege den Kommentar der Liste hinzu
			kommentarList.add(komm);
		}
		// Gib die Liste aller Kommentare zurueck
		return kommentarList;
	}
	
	/**
	 * Speichert ein 'Interessiert mich nicht besonders' des Nutzers f&uuml;r den jeweiligen Beitrag ab.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void likeBeitrag(HttpServletRequest request, HttpServletResponse response) throws IOException, NullPointerException {
		// Die ID des aktuellen Users
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// Die BeitragsID
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		// Die Seite, auf die zurueckverwiesen werden soll
		String page = request.getParameter("page");
		// Wenn keine Seite angegeben wurde
		if (page == null) {
			// Wird auf die Beitragsseite verwiesen
			page = "BeitragServlet?beitragsID=" + beitragsID;
		}
		// Wenn der Aufruf vom Profil kam
		if (page.equals("ProfilServlet")) {
			// Muss fuer diese Seite die UserID gesetzt werden
			page += "?userID=" + request.getParameter("userID");
		// Wenn er von einer Veranstaltung kam
		} else if (page.equals("VeranstaltungenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=beitraege";
			page += "&veranstaltungsID=" + request.getParameter("veranstaltungsID");
		// Wenn er von einer Gruppe kam
		} else if (page.equals("GruppenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=beitraege";
			page += "&gruppenID=" + request.getParameter("gruppenID");
		}
		try {
			// Lade das Sql-Statement
			String sql = sqlSt.getSqlStatement("BeitragLiken");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setInt(2, userID);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			// Wenn es einen Fehler gab, wurde der Beitrag bereits geliket
			try {
				// Deswegen soll der Like jetzt entfernt werden
				String sql = sqlSt.getSqlStatement("BeitragEntliken");
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.setInt(2, userID);
				pStmt.executeUpdate();
			} catch (Exception e2) {
				// Wenn das auch nicht geht, ist es ein groeﬂerer Fehler
				// TODO Fehler
				System.out.println("SQL Fehler in BeitragServlet");
			}
		} finally {
			// Weiterleitung an die entsprechende Seite
			response.sendRedirect(page);
		}
	}
	
	/**
	 * Speichert einen Kommentar des aktuellen Users f&uuml;r diesen Beitrag.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void kommentieren(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NullPointerException {
		// ID des aktuellen Users
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// BeitragsID
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		// Die Seite, auf der kommentiert wurde und an die zurueck verwiesen werden soll
		String page = request.getParameter("page");
		// Wenn keine Seite angegeben wurde
		if (page == null) {
			// Wird an die Beitragsseite weitergeleitet
			page = "BeitragServlet?beitragsID=" + beitragsID;
		}
		// Wenn der Aufruf von der Profilseite kam
		if (page.equals("ProfilServlet")) {
			// Muss die UserID gesetzt werden
			page += "?userID=" + request.getParameter("userID");
		// Wenn er von einer Veranstaltung kam
		} else if (page.equals("VeranstaltungenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=" + request.getParameter("tab");
			page += "&veranstaltungsID=" + request.getParameter("veranstaltungsID");
		// Wenn er von einer Gruppe kam
		} else if (page.equals("GruppenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=beitraege";
			page += "&gruppenID=" + request.getParameter("gruppenID");
		}
		// Lade das Sql-Statement
		String sql = sqlSt.getSqlStatement("Kommentieren");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		pStmt.setString(2, request.getAttribute("kommentar").toString());
		pStmt.setInt(3, userID);
		// Setzte den Zeitstempel (jetzt)
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		// Weiterleitung an die angegebene Seite
		response.sendRedirect(page);
	}
	
	/**
	 * L&ouml;scht einen Beitrag.
	 * @param request Request-Objekt
	 * @param response Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 */
	private void beitragLoeschen(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// BeitragsID
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		// Die Seite, von der der Aufruf kam
		String page = request.getParameter("page");
		// Wenn keine Seite angegeben wurde
		if (page == null) {
			// Wird auf die Startseite verwiesen
			page = "StartseiteServlet";
		}
		// Wenn der Aufruf von der Profilseite kam
		if (page.equals("ProfilServlet")) {
			// Muss die UserID gesetzt werden
			page += "?userID=" + request.getParameter("userID");
		// Wenn er von einer Veranstaltung kam
		} else if (page.equals("VeranstaltungenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=" + request.getParameter("tab");
			page += "&veranstaltungsID=" + request.getParameter("veranstaltungsID");
		// Wenn er von einer Gruppe kam
		} else if (page.equals("GruppenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=beitraege";
			page += "&gruppenID=" + request.getParameter("gruppenID");
		}
		// Lade das Sql-Statement
		String sql = sqlSt.getSqlStatement("BeitragLoeschen");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		pStmt.executeUpdate();
		// Weiterleitung an die entsprechende Seite
		response.sendRedirect(page);
			
	}
	
	/**
	 * L&ouml;scht den angegebenen Kommentar.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @param komm Spezifiziert die Art des Kommentars (Kommentar, Unterkommentar, KommentarZuUnterkommentar)
	 * @throws SQLException
	 * @throws IOException
	 */
	private void kommentarLoeschen(HttpServletRequest request, HttpServletResponse response, String komm) throws SQLException, IOException {
		// KommentarID
		int kommID = Integer.parseInt(request.getParameter("id"));
		// Die Seite, von der der Aufruf kam
		String page = request.getParameter("page");
		// Wenn keine Seite angegeben wurde
		if (page == null) {
			// Verweise auf die Beitragsseite
			page = "BeitragServlet?beitragsID=" + request.getParameter("beitragsID");
		}
		// Wenn der Aufruf von der Profilseite kam
		if (page.equals("ProfilServlet")) {
			// Muss die UserID gesetzt werden
			page += "?userID=" + request.getParameter("userID");
		// Wenn er von einer Veranstaltung kam
		} else if (page.equals("VeranstaltungenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=" + request.getParameter("tab");
			page += "&veranstaltungsID=" + request.getParameter("veranstaltungsID");
		// Wenn er von einer Gruppe kam
		} else if (page.equals("GruppenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=beitraege";
			page += "&gruppenID=" + request.getParameter("gruppenID");
		}
		String sql = "";
		// In Abhaengigkeit der Art des Kommentars
		switch (komm) {
		// Lade die verschiedenen Sql-Statements
		case "kommentar":
			sql = sqlSt.getSqlStatement("LoescheKommentar");
			break;
		case "komm":
			sql = sqlSt.getSqlStatement("LoescheUnterkommentar");
			break;
		case "kzukomm":
			sql = sqlSt.getSqlStatement("LoescheKommentarZuUnterkommentar");
			break;
		default:
			break;
		}
		// Und fuehre sie aus
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, kommID);
		pStmt.executeUpdate();
		// Weiterleitung an die entsprechende Seite
		response.sendRedirect(page);
	}
	
	/**
	 * Bereitet eine Antwort auf einen Kommentar vor, indem Attribute gesetzt werden, sodass ein Antwort-Textfeld angezeigt wird.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @param komm Spezifiziert die Art des Kommentars (Kommentar, Unterkommentar, KommentarZuUnterkommentar)
	 * @throws ServletException
	 * @throws IOException
	 */
	private void antwortVorbereiten(HttpServletRequest request, HttpServletResponse response, String komm) throws ServletException, IOException {
		// Das Antwortfeld soll angezeigt werden
		request.setAttribute("anzeigen", true);
		// Auf welche Art von Kommentar wird geantwortet
		request.setAttribute("tiefe", komm);
		// Auf welchen Kommentar soll geantwortet werden
		request.setAttribute("kommID", request.getParameter("kommID"));
		// Die auszufuehrende Aktion muss zurueckgesetzt werden, sonst entsteht eine Endlosschleife
		request.setAttribute("name", null);
		// Weiterleitung 
		request.getRequestDispatcher("BeitragServlet?anzeigen=true&beitragsID=" + request.getParameter("beitragsID")).forward(request, response);
	}
	
	/**
	 * Tr&auml;gt eine Antwort auf einen Kommentar ein.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void kommentarAntworten(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NullPointerException {
		// ID des aktuellen Users
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// Die Art des Kommentars (Kommentar, Unterkommentar, KommentarZuUnterkommentar)
		String tiefe = request.getParameter("tiefe");
		// Die ID des Kommentars, auf den geantwortet wird
		int kommID = Integer.parseInt(request.getParameter("kommID"));
		// Die Seite, auf die verwiesen werden soll
		String page = "BeitragServlet?beitragsID=" + request.getParameter("beitragsID");
		String sql = "";
		// Lade in Abhaengigkeit der Art des Kommentars das entsprechende Sql-Statement
		switch (tiefe) {
		case "kommentar":
			sql = sqlSt.getSqlStatement("Unterkommentieren");
			break;
		case "komm":
			sql = sqlSt.getSqlStatement("UnterkommentarKommentieren");
			break;
		default:
			break;
		}
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, kommID);
		pStmt.setString(2, request.getAttribute("kommentar").toString());
		pStmt.setInt(3, userID);
		// Setze den Zeitstempel (jetzt)
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		// Weiterleitung an die entsprechende Seite
		response.sendRedirect(page);
	}
	
	/**
	 * Speichert einen Beitrag als <b>gemeldet</b> in der DB ab.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private void melden(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NullPointerException {
		// Die ID des aktuellen Users
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// Die BeitragsID
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		// Die Seite, von der der Aufruf kam und an die wieder weitergeleitet werden soll
		String page = request.getParameter("page");
		// Wenn keine Seite angegeben wurde
		if (page == null) {
			// Wird an die Beitragsseite weitergeleitet
			page = "BeitragServlet?beitragsID=" + request.getParameter("beitragsID");
		}
		// Wenn der Aufruf von der Profilseite kam
		if (page.equals("ProfilServlet")) {
			// Muss die UserID gesetzt werden
			page += "?userID=" + request.getParameter("userID");
		// Wenn er von einer Veranstaltung kam
		} else if (page.equals("VeranstaltungenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=" + request.getParameter("tab");
			page += "&veranstaltungsID=" + request.getParameter("veranstaltungsID");
		// Wenn er von einer Gruppe kam
		} else if (page.equals("GruppenServlet")) {
			// Muss der Tab und die ID gesetzt werden
			page += "?tab=beitraege";
			page += "&gruppenID=" + request.getParameter("gruppenID");
		}
		String sql = sqlSt.getSqlStatement("BeitragMelden");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		pStmt.setInt(2, userID);
		pStmt.executeUpdate();
		// Weiterleitung an die entsprechende Seite
		response.sendRedirect(page);
	}
	
	/**
	 * Gibt eine Liste mit allen relevanten Beitr&auml;gen f&uuml;r die jeweilige Seite zur&uuml;ck.
	 * @param request Das Request-Objekt
	 * @param con Die DB-Verbindung
	 * @param seite Die aufrufende Seite
	 * @param userID Die ID des aktuellen Users (Ausnahme: Profilservlet: ID des Profilbesitzers)
	 * @param optionalParams Optionale Parameter um korrekt weiterleiten zu k&ouml;nnen
	 * @return Die Liste der relevanten Beitr&auml;ge
	 * @throws SQLException
	 * @throws NullPointerException
	 */
	public List<Beitrag> getBeitraege(HttpServletRequest request, Connection con, String seite, int userID, String... optionalParams) throws SQLException, NullPointerException {
		String sql = "";
		PreparedStatement pStmt;
		// Je nach Seite wird das entsprechende Sql-Statement geladen
		switch (seite) {
		case "Startseite":
			sql = new StartseiteSql().getSqlStatement("Beitraege");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			break;
		case "Profilseite":
			sql = new ProfilSql().getSqlStatement("Beitraege");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			break;
		case "Veranstaltungen":
			sql = new VeranstaltungenSql().getSqlStatement("Beitraege");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(optionalParams[0]));
			break;
		case "Gruppen":
			sql = new GruppenSql().getSqlStatement("Beitraege");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(optionalParams[0]));
			break;
		default:
			pStmt = con.prepareStatement("");
			break;
		}
		ResultSet rs = pStmt.executeQuery();
		// Liste fuer die Beitraege
		List<Beitrag> beitragList = new ArrayList<Beitrag>();
		// Fuer jeden Beitrag
		while (rs.next()) {
			// VerfasserID
			int id = rs.getInt(1);
			// Verfassername
			String name = rs.getString(2) + " " + rs.getString(3);
			// Beitrag
			String nachricht = rs.getString(4);
			int anzahlLikes = rs.getInt(5);
			int anzahlKommentare = rs.getInt(6);
			int beitragsID = rs.getInt(7);
			// Formatierung des Zeitstempels
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			String timeStamp = sdf.format(new Date(rs.getDate(8).getTime())) + " " + rs.getTime(9).toString();
			// Sichtbarkeit
			if (rs.getBoolean(10)) {
				timeStamp += " <span class='glyphicon glyphicon-globe'></span>";
			} else {
				timeStamp += " <span class='glyphicon glyphicon-user'></span>";
			}
			// Bearbeitet
			boolean bearbeitet = rs.getBoolean(11);
			// Wurde der Beitrag mit 'interessiert mich nicht besonders' markiert
			sql = sqlSt.getSqlStatement("Like");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			// Hier muss die ID des aktuellen Users gesetzt werden, daher das Attribut und nicht 'userID'
			pStmt.setInt(2, Integer.parseInt(request.getSession().getAttribute("UserID").toString()));
			ResultSet rs2 = pStmt.executeQuery();
			if (rs2.next()) {
				boolean like = rs2.getInt(1) == 0 ? false : true;
				// Wenn es der eigene Beitrag ist, darf man ihn loeschen
				boolean loeschenErlaubt = userID == id;
				// Beitrag erstellen
				Beitrag beitrag = new Beitrag(id, name, timeStamp, nachricht, anzahlLikes, anzahlKommentare, beitragsID, like, loeschenErlaubt, bearbeitet);
				// Wurde der Beitrag in der Chronik oder woanders gepostet
				sql = sqlSt.getSqlStatement("OrtName");
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				ResultSet rs3 = pStmt.executeQuery();
				// Wenn er woanders gepostet wurde
				if (rs3.next()) {
					// Wahrheitswert setzen und den Namen des Ortes speichern
					beitrag.setNichtChronik(true);
					beitrag.setOrtName(rs3.getString(1));
				}
				// Beitrag der Liste hinzufuegen
				beitragList.add(0, beitrag);
			}
		}
		return beitragList;
	}
	
	/**
	 * Zeigt eine Liste der Personen an, die diesen Beitrag mit 'interessiert mich nicht besonders' markiert haben.
	 * @param request Das Request-Objekt
	 * @param response Das Response-Objekt
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void likesAnzeigen(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		// Die BeitragsID
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		// Art der Sortierung (alphabetisch Vorname, Nachname; chronologisch)
		String sortBy = request.getParameter("sortBy");
		// Wenn keine Sortierung angegeben wurde, wird chronologisch sortiert
		if (sortBy == null) {
			sortBy = "Zeit";
		}
		// Lade das Sql-Statement
		String sql = sqlSt.getSqlStatement("LikesPersonen");
		// In Abhaengigkeit der Sortierung
		switch (sortBy) {
		case "Vorname":
			sql += "ORDER BY Vorname, Nachname";
			request.setAttribute("vornameLink", "text-decoration: underline;");
			break;
		case "Nachname":
			sql += "ORDER BY Nachname, Vorname";
			request.setAttribute("nachnameLink", "text-decoration: underline;");
			break;
		default:
			request.setAttribute("zeitLink", "text-decoration: underline;");
			break;
		}
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, beitragsID);
		ResultSet rs = pStmt.executeQuery();
		// Liste der Personen
		List<Student> user = new ArrayList<Student>();
		// Fuer jede Person
		while (rs.next()) {
			int userID = rs.getInt(1);
			String vorname = rs.getString(2);
			String nachname = rs.getString(3);
			// Fuege der Liste den neuen Studenten hinzu
			user.add(new Student(vorname, nachname, userID));
		}
		// Setze die Liste der Studenten als Attribut
		request.setAttribute("user", user);
		// Setze die Groesse der Liste
		request.setAttribute("anzahl", user.size());
		// Es sollen die Likes und nicht der Beitrag angezeigt werden
		request.setAttribute("beitragAnzeigen", false);
		request.setAttribute("beitragsID", beitragsID);
		// Weiterleitung an die Beitragsseite
		request.getRequestDispatcher("Beitrag.jsp").forward(request, response);
	}
	
	private String inverseEmoticonFilter(String beitrag) {
		// Lade alle Emoticons
		List<Emoticon> emoticons = new EmoticonServlet().getEmoticons();
		for (Emoticon emo : emoticons) {
			// Ersetze alle Kuerzel durch ihre Unicode-Zeichen
			beitrag = beitrag.replace(emo.getBild(), emo.getCode());
		}
		return beitrag;
	}

}
