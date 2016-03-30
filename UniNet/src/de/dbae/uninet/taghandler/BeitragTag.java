package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Beitrag;

/**
 * Dieser Tag zeigt die Informationen aus dem Beitrags-Objekt auf der jsp an.
 * Er wird auf den meisten Seiten des Projekts verwendet.
 * @author Christian Ackermann
 */
public class BeitragTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	/**
	 * Der Beitrag mit allen wichtigen Eigenschaften.
	 * @see Beitrag
	 */
	private Beitrag beitrag;
	
	/**
	 * Die Seite, auf die anschlie&szlig;end zur&uuml;ck verwiesen werden soll.<br>
	 * Sie wird bei den Links als Parameter gesetzt und sp&auml;ter vom BeitragServlet verarbeitet.
	 * @see BeitragServlet
	 */
	private String page;

	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (Exception e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r den Beitrag.<br>
	 * Dynamisch wird &uuml;berpr&uuml;ft, ob der Beitrag bereits mit 'Interessiert mich nicht besonders' markiert und
	 * wo er gepostet wurde (Chronik, Gruppe, Veranstaltung), sowie eine grammatikalische Anpassung beim Sonderfall Anzahl = 1
	 * f&uuml;r Likes und Kommentare.
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String geliket = "";
		if (beitrag.isLike()) {
			geliket = "geliket";
		} 
		String erg = "<div class='row'><div class='col-md-1'></div><div class='col-md-10 beitrag'><div class='row kopf'><br>";
		erg += "<div class='col-md-1'><a href='#'><img class='media-object kommentarbild profil' alt='Profilbild' src='LadeProfilbildServlet?userID=" + beitrag.getUserID() + "'></a></div>";
		erg += "<div class='col-md-10'>";
		// Ueberpruefung, ob der Beitrag in der Chronik oder wo anders (Gruppe, Veranstaltung) gepostet wurde
		if (beitrag.isNichtChronik()) {
			// Wenn nicht in der Chronik, fuege einen Pfeil und den entsprechenden Namen des Ortes hinzu (zB. Autor -> Gruppe1)
			erg += "<a class='verfasser' href='ProfilServlet?userID=" + beitrag.getUserID() + "'>" + beitrag.getName() + 
					"</a> <span class='glyphicon glyphicon-arrow-right' style='color: #3b5998;'></span> ";
			erg += "<a class='verfasser' href='#'>" + beitrag.getOrtName() + "</a><br>";
		} else {
			// Sonst nur den Namen anzeigen
			erg += "<a class='verfasser' href='ProfilServlet?userID=" + beitrag.getUserID() + "'>" + beitrag.getName() + "</a><br>";
		}
		erg += "<label class='zeitstempel'>" + beitrag.getTimeStamp() + "</label></div>";
		if (beitrag.getLoeschenErlaubt()) {
			erg += "<div class='col-md-1'><a href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "&name=BeitragLoeschen&page=" + page + "' title='Beitrag löschen'><span class='glyphicon glyphicon-remove-sign' style='color:#3b5998;'></span></a></div>";
		}
		erg += "</div><label class='beitrag'><br>" + beitrag.getNachricht() + "</label><br><br>";
		erg += "<div><ul class='nav nav-pills border'>";
		erg += "<li role='presentation' class='like " + geliket + "'><a href='BeitragServlet?page=" + page + "&beitragsID=" + beitrag.getBeitragsID() 
		+ "&name=Like'>Interessiert mich nicht besonders</a></li>";
		erg += "<li role='presentation'><a href='BeitragServlet?page=" + page + "&beitragsID=" + beitrag.getBeitragsID() + "&name=Melden'>Melden</a></li>";
		erg += "<li role='presentation' class='kommentareAnzeigen'><a href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "'>Alle Kommentare anzeigen</a></li></ul>";
		erg += "<label class='anzahlLikes'><p><p>";
		// Wenn es nur eine Person 'nicht besonders interessiert', schreibe 'Eine Person' sonst '... Personen'
		String likePersonen = beitrag.getLikes() == 1 ? "Eine Person" : beitrag.getLikes() + " Personen";
		erg += "<a class='blau' href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "&name=LikesAnzeigen'>" + likePersonen + " interessiert das nicht besonders</a></label>";
		// Aehnliches mit Kommentaren
		String kommentare = beitrag.getKommentare() == 1 ? "1 Kommentar" : beitrag.getKommentare() + " Kommentare";
		erg += "<label class='anzahlKommentare'><a class='anzahlKommentare' href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "'>" + kommentare + "</a></label></div>";
		erg += "<div class='row'><div class='col-md-1'><a href='#'><img class='media-object kommentarbild' alt='Profilbild' src='LadeProfilbildServlet?'></a></div>";
		erg += "<div class='col-md-11'><form action='BeitragServlet?page=" + page + "&beitragsID=" + beitrag.getBeitragsID() + "&name=Kommentar' method='post'>";
		erg += "<input type='text' class='form-control' name='kommentar' placeholder='Schreibe einen Kommentar...'></form></div></div><p></div></div><br>";
		return erg;
	}
	
	/**
	 * Getter f&uuml;r den Beitrag.
	 * @return Das Beitragsobjekt
	 * @see Beitrag
	 */
	public Beitrag getBeitrag() {
		return beitrag;
	}

	/**
	 * Setter f&uuml;r den Beitrag.
	 * @param beitrag Der Beitrag
	 * @see Beitrag
	 */
	public void setBeitrag(Beitrag beitrag) {
		this.beitrag = beitrag;
	}
	
	/**
	 * Getter f&uuml;r die Seite.
	 * @return Die Seite
	 */
	public String getPage() {
		return page;
	}

	/**
	 * Setter f&uuml;r die Seite.
	 * @param page Die Seite
	 */
	public void setPage(String page) {
		this.page = page;
	}
}
