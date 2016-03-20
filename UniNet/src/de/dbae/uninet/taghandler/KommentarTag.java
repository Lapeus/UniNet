package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Kommentar;
import de.dbae.uninet.javaClasses.KommentarZuUnterkommentar;
import de.dbae.uninet.javaClasses.Unterkommentar;

public class KommentarTag extends TagSupport {

	private static final long serialVersionUID = 8294338857824124003L;
	private Kommentar kommentar;

	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public Kommentar getKommentar() {
		return kommentar;
	}

	public void setKommentar(Kommentar kommentar) {
		this.kommentar = kommentar;
	}
	
	private String getHtmlCode() {
		// Kommentar
		String erg = "";
		erg += "<p><div class='row'><div class='col-md-1'>";
		erg += "<a href='#'><img class='media-object kommentarbild' src='Testbild.jpg' alt='Testbild'></a></div>";
		erg += "<div class='col-md-11'><h5 class='media-heading kommentarVerfasser'><a class='kommentarVerfasser' href='ProfilServlet?beitragsID=" + kommentar.getUserID() + "'>" + kommentar.getName() + "</a></h5>";
		erg += kommentar.getKommentar();
		erg += "<div class='row'><div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=AntwortAufKommentar&kommID=" + kommentar.getKommID() + "'>Antworten</a></div>";
		int userID = kommentar.getUserIDsession();
		if (userID == kommentar.getUserID()) {
			erg += "<div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=KommentarLoeschen&id=" + kommentar.getKommID() + "'>Löschen</a></div>";
		}
		erg += "<div class='col-md-8'><a class='zeitstempel'>" + kommentar.getTimeStamp() + "</a></div></div>"; 
		// Hier muessen die Unterkommentare eingefuegt werden
		for (Unterkommentar komm : kommentar.getKommentarList()) {
			erg += "<div class='row border-left'><div class='col-md-1'>";
			erg += "<a href='#'><img class='media-object kommentarbild' src='Testbild.jpg' alt='Testbild'></a></div>";
			erg += "<div class='col-md-11'><h5 class='media-heading kommentarVerfasser'><a class='kommentarVerfasser' href='ProfilServlet?beitragsID=" + komm.getUserID() + "'>" + komm.getName() + "</a></h5>";
			erg += komm.getKommentar();
			// Antwort auf Unterkommentar
			erg += "<div class='row'><div class='col-md-2'><a class='blau'href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=AntwortAufKomm&kommID=" + komm.getKommID() + "&userName=" + komm.getName() + "&userID=" + komm.getUserID() + "'>Antworten</a></div>";
			if (userID == komm.getUserID()) {
				erg += "<div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=KommLoeschen&id=" + komm.getKommID() + "'>Löschen</a></div>"; 
			}
			erg += "<div class='col-md-8'><a class='zeitstempel'>" + komm.getTimeStamp() + "</a></div></div>"; 
			erg += "</div></div>";
			// Hier muessen die KommentareZuUnterkommentare eingefuegt werden
			for (KommentarZuUnterkommentar kzukomm : komm.getKommentarList()) {
				erg += "<div class='row border-left'><p><div class='col-md-1'>";
				erg += "<a href='#'><img class='media-object kommentarbild' src='Testbild.jpg' alt='Testbild'></a></div>";
				erg += "<div class='col-md-11'><h5 class='media-heading kommentarVerfasser'><a class='kommentarVerfasser' href='ProfilServlet?beitragsID=" + kzukomm.getUserID() + "'>" + kzukomm.getName() + "</a></h5>";
				erg += "<a href='ProfilServlet?userID=" + kzukomm.getAntwortAufKommID() + "'><u>" + kzukomm.getAntwortAufKommName() + "</u></a> " + kzukomm.getKommentar();
				// Antwort auf Unterkommentar
				erg += "<div class='row'>";
				erg += "<div class='col-md-2'></div>";
				if (userID == kzukomm.getUserID()) {
					erg += "<div class='col-md-2'><a class='blau' href='BeitragServlet?beitragsID=" + kommentar.getZielID() + "&name=KzukommLoeschen&id=" + kzukomm.getKommID() + "'>Löschen</a></div>"; 	
				}
				erg += "<div class='col-md-8'><a class='zeitstempel'>" + kzukomm.getTimeStamp() + "</a></div></div>"; 
				erg += "</div></div>";
			}
		}
		erg += "</div></div><br>";
		return erg;
	}
}
