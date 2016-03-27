package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Kommentar;
import de.dbae.uninet.javaClasses.KommentarZuUnterkommentar;
import de.dbae.uninet.javaClasses.Unterkommentar;

/**
 * Dieser Tag zeigt einen Kommentar mit all seinen Unterkommentaren zu einem Beitrag an.<br>
 * Er wird innerhalb eines forEachs in der Beitrag.jsp verwendet.
 * @author Christian Ackermann
 */
public class KommentarTag extends TagSupport {

	private static final long serialVersionUID = 8294338857824124003L;
	
	/**
	 * Das Kommentarobjekt mit allen Unterkommentaren und weiteren Eigenschaften.
	 * @see Kommentar
	 */
	private Kommentar kommentar;

	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r den Kommentar.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		// Kommentar
		String erg = "";
		erg += "<p><div class='row'><div class='col-md-1'>";
		erg += "<a href='#'><img class='media-object kommentarbild' src='Testbild.jpg' alt='Testbild'></a></div>";
		erg += "<div class='col-md-11'><h5 class='media-heading kommentarVerfasser'><a class='kommentarVerfasser' href='ProfilServlet?userID=" + kommentar.getUserID() + "'>" + kommentar.getName() + "</a></h5>";
		erg += kommentar.getKommentar();
		erg += "<div class='row'><div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=AntwortAufKommentar&kommID=" + kommentar.getKommID() + "'>Antworten</a></div>";
		int userID = kommentar.getUserIDsession();
		// Wenn es der eigene Kommentar ist, kann man ihn loeschen
		if (userID == kommentar.getUserID()) {
			erg += "<div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=KommentarLoeschen&id=" + kommentar.getKommID() + "'>Löschen</a></div>";
		}
		erg += "<div class='col-md-8'><a class='zeitstempel'>" + kommentar.getTimeStamp() + "</a></div></div>"; 
		// Anzeige saemtlicher Unterkommentare
		for (Unterkommentar komm : kommentar.getKommentarList()) {
			erg += "<div class='row border-left'><div class='col-md-1'>";
			erg += "<a href='#'><img class='media-object kommentarbild' src='Testbild.jpg' alt='Testbild'></a></div>";
			erg += "<div class='col-md-11'><h5 class='media-heading kommentarVerfasser'><a class='kommentarVerfasser' href='ProfilServlet?userID=" + komm.getUserID() + "'>" + komm.getName() + "</a></h5>";
			erg += komm.getKommentar();
			erg += "<div class='row'><div class='col-md-2'><a class='blau'href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=AntwortAufKomm&kommID=" + komm.getKommID() + "&userName=" + komm.getName() + "&userID=" + komm.getUserID() + "'>Antworten</a></div>";
			// Wenn es der eigene Unterkommentar ist, kann man ihn loeschen
			if (userID == komm.getUserID()) {
				erg += "<div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=KommLoeschen&id=" + komm.getKommID() + "'>Löschen</a></div>"; 
			}
			erg += "<div class='col-md-8'><a class='zeitstempel'>" + komm.getTimeStamp() + "</a></div></div>"; 
			erg += "</div></div>";
			// Anzeige saemtlicher Kommentare zu dem entsprechenden Unterkommentar
			for (KommentarZuUnterkommentar kzukomm : komm.getKommentarList()) {
				erg += "<div class='row border-left'><div class='col-md-1'>";
				erg += "<a href='#'><img class='media-object kommentarbild' src='Testbild.jpg' alt='Testbild'></a></div>";
				erg += "<div class='col-md-11'><h5 class='media-heading kommentarVerfasser'><a class='kommentarVerfasser' href='ProfilServlet?userID=" + kzukomm.getUserID() + "'>" + kzukomm.getName() + "</a></h5>";
				erg += "<a href='ProfilServlet?userID=" + kzukomm.getAntwortAufKommID() + "'><u>" + kzukomm.getAntwortAufKommName() + "</u></a> " + kzukomm.getKommentar();
				erg += "<div class='row'>";
				erg += "<div class='col-md-2'></div>";
				// Wenn es der eigene Kommentar ist, kann man ihn loeschen
				if (userID == kzukomm.getUserID()) {
					erg += "<div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=KzukommLoeschen&id=" + kzukomm.getKommID() + "'>Löschen</a></div>"; 	
				}
				erg += "<div class='col-md-8'><a class='zeitstempel'>" + kzukomm.getTimeStamp() + "</a></div></div>"; 
				erg += "</div></div>";
			}
		}
		erg += "</div></div>";
		return erg;
	}
	
	/**
	 * Getter f&uuml;r den Kommentar.
	 * @return Der Kommentar
	 */
	public Kommentar getKommentar() {
		return kommentar;
	}

	/**
	 * Setter f&uuml;r den Kommentar.
	 * @param kommentar Der Kommentar
	 */
	public void setKommentar(Kommentar kommentar) {
		this.kommentar = kommentar;
	}
}
