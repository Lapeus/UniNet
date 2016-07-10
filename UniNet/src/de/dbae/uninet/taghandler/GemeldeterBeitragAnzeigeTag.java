package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Beitrag;

/**
 * Dieser Tag zeigt die Informationen aus dem Beitrags-Objekt auf der jsp an.
 * Er wird auf den meisten Seiten des Projekts verwendet.
 * @author Leon Schaffert
 */
public class GemeldeterBeitragAnzeigeTag extends TagSupport {

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
			System.out.println("Fehler beim Anh�ngen!");
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
		String erg = "<div class='row'><div class='col-md-1'></div><div class='col-md-10 beitrag'><div class='row kopf'><br>";
		erg += "<div class='col-md-1'><img class='media-object kommentarbild profil' alt='Profilbild' src='LadeProfilbildServlet?userID=" + beitrag.getUserID() + "'></div>";
		erg += "<div class='col-md-9'>";
		// Ueberpruefung, ob der Beitrag in der Chronik oder wo anders (Gruppe, Veranstaltung) gepostet wurde
		if (!beitrag.getOrtLink().equals("")) {
			// Wenn nicht in der Chronik, fuege einen Pfeil und den entsprechenden Namen des Ortes hinzu (zB. Autor -> Gruppe1)
			erg += beitrag.getName() + " <span class='glyphicon glyphicon-arrow-right' style='color: #3b5998;'></span> ";
			erg +=  beitrag.getOrtName() + "<br>";
		} else {
			// Sonst nur den Namen anzeigen
			erg += "<a class='verfasser' src='ProfilServlet?userID=" + beitrag.getUserID() + "'>" + beitrag.getName() + "<br>";
		}
		erg += "<label class='zeitstempel'>" + beitrag.getTimeStamp();
		if (beitrag.isBearbeitet())
			erg += "&nbsp;&nbsp;&nbsp;Bearbeitet";
			//erg += "<span title='Bearbeitet' class='glyphicon glyphicon-pencil'></span>";
		erg += "</label></div>";
		erg += "<div class='col-md-2'><div class='row'>";
		erg += "<div class='col-md-8'>";
		erg += "<a class='pull-right' href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "&name=GemeldetenBeitragBearbeiten' title='Beitrag bearbeiten'><span class='glyphicon glyphicon-pencil' style='color:#3b5998;'></span></a>";
		erg += "</div><div class='col-md-3'>";
		erg += "<a class='pull-right' href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "&name=BeitragLoeschen&page=" + page + "' title='Beitrag l&ouml;schen'><span class='glyphicon glyphicon-remove-sign' style='color:#3b5998;'></span></a>";
		erg += "</div><div class='col-md-1'></div></div></div>";
		erg += "</div><label class='beitrag'><br>" + beitrag.getNachricht() + "</label><br><br>";
		erg += "<div><ul class='nav nav-pills border'>";
		erg += "<li class='blau'><label class='anzahlLikes'><p><p>Interessiert mich nicht besonders</label></li>";
		erg += "<li class='kommentareAnzeigen'><label class='anzahlKommentare'><p><p>Alle Kommentare anzeigen</label></li></ul>";
		erg += "<label class='anzahlLikes'><p><p>";
		// Wenn es nur eine Person 'nicht besonders interessiert', schreibe 'Eine Person' sonst '... Personen'
		String likePersonen = beitrag.getLikes() == 1 ? "Eine Person" : beitrag.getLikes() + " Personen";
		erg += "<class='blau'>" + likePersonen + " interessiert das nicht besonders	</label>";
		// Aehnliches mit Kommentaren
		String kommentare = beitrag.getKommentare() == 1 ? "1 Kommentar" : beitrag.getKommentare() + " Kommentare";
		erg += "<label class='anzahlKommentare'><class='anzahlKommentare'>" + kommentare + "</label></div>";
		erg += "<div class='row'><div class='col-md-1'></div>";
		erg += "<div class='col-md-11'></div></div><p></div></div><br>";
		return erg;
	}
	
	/**
	 * Getter f&uuml;r den Beitrag.
	 * @return Das Beitragsobjekt
	 * @see Beitrag
	 */
	public Beitrag getGemeldeterBeitrag() {
		return beitrag;
	}

	/**
	 * Setter f&uuml;r den Beitrag.
	 * @param beitrag Der Beitrag
	 * @see Beitrag
	 */
	public void setGemeldeterBeitrag(Beitrag beitrag) {
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
