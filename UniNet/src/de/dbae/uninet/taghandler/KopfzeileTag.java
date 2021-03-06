package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.dbConnections.DBConnection;

/**
 * Dieser Tag erstellt die Kopfzeile.<br>
 * Er wird direkt oder indirekt auf jeder jsp verwendet.
 * @author Christian Ackermann
 */
public class KopfzeileTag extends TagSupport {

	/**
	 * Auto-generated serialVersionUID.
	 */
	private static final long serialVersionUID = -5650080610199722432L;
	
	/**
	 * Die Anzahl der ungelesenen Nachrichten / Chats.
	 */
	private int anzahlUngeleseneNachrichten = 0;
	
	/**
	 * Die Anzahl der ungelesenen Benachrichtigungen.
	 */
	private int anzahlUngeleseneBenachrichtigungen = 0;
	
	/**
	 * Die Datenbankverbindung.
	 */
	private Connection con;
	
	/**
	 * Die UserID des aktuellen Nutzers.
	 */
	private int userID;
	
	/**
	 * Der Vorname des Nutzers.
	 */
	private String vorname = "Profilseite";
	
	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode(userID));
		} catch (IOException e) {
			System.out.println("Fehler beim Anh&auml;ngen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die Kopfzeile.<br>
	 * @return Den entsprechenden Html-Code
	 */
	public String getHtmlCode(int userID) {
		// UserID setzen
		this.userID = userID;
		// Eine neue DBConnection erzeugen
		DBConnection dbcon = new DBConnection();
		// Die Verbindung setzen
		con = dbcon.getCon();
		// Anzahl Benachrichtigungen und Nachrichten setzen
		setAnzahlBenachrichtigungen();
		setAnzahlNachrichten();
		// Vornamen des aktuellen Benutzers setzen
		setVorname();
		// Die DB-Verbindung schliessen, da sie nicht mehr gebraucht wird
		dbcon.close();
		// Den Html-Code erzeugen
		String kopfzeile = "";
		kopfzeile += "<nav class='navbar navbar-default navbar-fixed-top'>";
	    kopfzeile += "<div class='container-fluid kopfzeile'>";
		kopfzeile += "<div class='navbar-header nav-justified'>";
		kopfzeile += "<ul class='nav navbar-nav navbar-left'>";
		kopfzeile += "<li><label class='platzhalter'></label></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile logo' href='StartseiteServlet'><b>UNINET</b></a></li>";
		kopfzeile += "<li><form class='navbar-form navbar-left' role='search' action='SuchergebnisseServlet' method='get'>";
		kopfzeile += "<span class='form-group containerColor'>";
		kopfzeile += "<input type='text' class='form-control' name='suchanfrage' size=50 placeholder='Suchbegriff eingeben'>";
		kopfzeile += "<button type='submit' class='btn btn-default'>Suchen</button>";
		kopfzeile += "</span>";
		kopfzeile += "</form></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='ProfilServlet'><img class='media-object kopfzeile' alt='' src='LadeProfilbildServlet'</img>&nbsp;"
				 + vorname + "</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='StartseiteServlet'>Startseite</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='NachrichtenServlet'>";
		// Wenn es ungelesene Nachrichten gibt
		if (anzahlUngeleseneNachrichten > 0) {
			// Wird die Anzahl fett angezeigt
			kopfzeile += "<b>Chat (" + anzahlUngeleseneNachrichten + ")</b>";
		} else {
			kopfzeile += "Chat";
		}
		kopfzeile += "</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='BenachrichtigungenServlet'>";
		// Wenn es ungelesene Benachrichtigungen gibt
		if (anzahlUngeleseneBenachrichtigungen > 0) {
			// Wird die Anzahl fett angezeigt
			kopfzeile += "<b>Benachrichtigungen (" + anzahlUngeleseneBenachrichtigungen + ")</b>";
		} else {
			kopfzeile += "Benachrichtigungen";
		}
		kopfzeile += "</a></li>";
		kopfzeile += "</ul>";
		kopfzeile += "<ul class='nav navbar-nav navbar-right'>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='EmoticonServlet'>Hilfe</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='LogoutServlet'>Logout</a></li>";
		kopfzeile += "</ul>";
		kopfzeile += "</div></div></nav>";
		return kopfzeile;
	}
	
	/**
	 * Setzt die Anzahl der ungelesenen Nachrichten / Chats.
	 */
	private void setAnzahlNachrichten() {
		try {
			// Lies die Anzahl aus der DB
			PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(empfaengerID) FROM "
					+ "(SELECT DISTINCT senderID, empfaengerID FROM nachrichten WHERE empfaengerID = ? AND gelesen = false) AS temp;");
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				anzahlUngeleseneNachrichten = rs.getInt(1);
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	/**
	 * Setzt die Anzahl der ungelesenen Benachrichtigungen.
	 */
	private void setAnzahlBenachrichtigungen() {
		try {
			// Lies die Anzahl aus der DB
			PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(benachrichtigungID) FROM benachrichtigungen WHERE userID = ? AND gelesen = FALSE");
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				anzahlUngeleseneBenachrichtigungen = rs.getInt(1);
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	/**
	 * Setzt den Vornamen des aktuellen Nutzers, damit dieser neben dem Profilbild angezeigt werden kann.
	 */
	private void setVorname() {
		try {
			PreparedStatement pStmt = con.prepareStatement("SELECT vorname FROM Nutzer WHERE userID = ?");
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				vorname = rs.getString(1);
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
	/**
	 * Getter f&uuml;r die UserID.
	 * @return Die UserID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Setter f&uuml;r die UserID.
	 * @param userID Die UserID
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

}
