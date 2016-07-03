package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
	
	private int anzahlUngeleseneNachrichten = 0;
	
	private int anzahlUngeleseneBenachrichtigungen = 0;
	
	private Connection con;
	
	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			System.out.println("Fehler beim Anh�ngen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die Kopfzeile.<br>
	 * @return Den entsprechenden Html-Code
	 */
	public String getHtmlCode() {
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
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='ProfilServlet'><img class='media-object kopfzeile' alt='' src='LadeProfilbildServlet'</img>&nbsp;Profilseite</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='StartseiteServlet'>Startseite</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='NachrichtenServlet'>";
		if (anzahlUngeleseneNachrichten > 0) {
			kopfzeile += "<b>Chat (" + anzahlUngeleseneNachrichten + ")</b>";
		} else {
			kopfzeile += "Chat";
		}
		kopfzeile += "</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='BenachrichtigungenServlet'>";
		if (anzahlUngeleseneBenachrichtigungen > 0) {
			kopfzeile += "<b>Benachrichtigungen (" + anzahlUngeleseneBenachrichtigungen + ")</b>";
		} else {
			kopfzeile += "Benachrichtigungen";
		}
		kopfzeile += "</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='FreundeFindenServlet'>Freunde finden</a></li>";
		kopfzeile += "</ul>";
		kopfzeile += "<ul class='nav navbar-nav navbar-right'>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='EmoticonServlet'>Hilfe</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='LogoutServlet'>Logout</a></li>";
		kopfzeile += "</ul>";
		kopfzeile += "</div></div></nav>";
		return kopfzeile;
	}
	
	private void setAnzahlNachrichten() {
		try {
			PreparedStatement pStmt = con.prepareStatement("");
			
		} catch (SQLException sqlEx) {
			
		}
	}
	
	private void setAnzahlBenachrichtigungen() {
		try {
			PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(benachrichtigungID) FROM benachrichtigungen WHERE userID = ? AND gelesen = FALSE");
			
		} catch (SQLException sqlEx) {
			
		}
	}

}
