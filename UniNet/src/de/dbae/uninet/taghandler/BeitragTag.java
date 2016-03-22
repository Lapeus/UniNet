package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Beitrag;

public class BeitragTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	private Beitrag beitrag;
	private String page;

	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	private String getHtmlCode() {
		String geliket = "";
		if (beitrag.isLike()) {
			geliket = "geliket";
		} 
		String erg = "<div class='row'><div class='col-md-1'></div><div class='col-md-10 beitrag'><div class='row kopf'><br>";
		erg += "<div class='col-md-1'><a href='#'><img class='media-object kommentarbild profil' alt='Testbild' src='Testbild.jpg'></a></div>";
		erg += "<div class='col-md-10'>";
		if (beitrag.isNichtChronik()) {
			erg += "<a class='verfasser' href='ProfilServlet?userID=" + beitrag.getUserID() + "'>" + beitrag.getName() + "</a> <span class='glyphicon glyphicon-arrow-right' style='color: #3b5998;'></span> ";
			erg += "<a class='verfasser' href='#'>" + beitrag.getOrtName() + "</a><br>";
		} else {
			erg += "<a class='verfasser' href='ProfilServlet?userID=" + beitrag.getUserID() + "'>" + beitrag.getName() + "</a><br>";
		}
		erg += "<label class='zeitstempel'>" + beitrag.getTimeStamp() + "</label></div>";
		erg += "<div class='col-md-1'><a href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "&name=BeitragLoeschen' title='Beitrag löschen'>" + beitrag.getLoeschenErlaubt() + "</a></div>";
		erg += "</div><label class='beitrag'><br>" + beitrag.getNachricht() + "</label><br><br>";
		erg += "<div><ul class='nav nav-pills border'>";
		erg += "<li role='presentation' class='like " + geliket + "'><a href='BeitragServlet?page=" + page + "&beitragsID=" + beitrag.getBeitragsID() + "&name=Like'>Interessiert mich nicht besonders</a></li>";
		erg += "<li role='presentation'><a href='BeitragServlet?page=" + page + "&beitragsID=" + beitrag.getBeitragsID() + "&name=Melden'>Melden</a></li>";
		erg += "<li role='presentation' class='kommentareAnzeigen'><a href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "'>Alle Kommentare anzeigen</a></li></ul>";
		erg += "<label class='anzahlLikes'><p><p>" + beitrag.getLikes() + " Personen interessiert das nicht besonders</label>";
		erg += "<label class='anzahlKommentare'><a class='anzahlKommentare' href='BeitragServlet?beitragsID=" + beitrag.getBeitragsID() + "'>" + beitrag.getKommentare() + " Kommentare</a></label></div>";
		erg += "<div class='row'><div class='col-md-1'><a href='#'><img class='media-object kommentarbild' alt='Testbild' src='Testbild.jpg'></a></div>";
		erg += "<div class='col-md-11'><form action='BeitragServlet?page=" + page + "&beitragsID=" + beitrag.getBeitragsID() + "name=Kommentar' method='post'>";
		erg += "<input type='text' class='form-control' name='kommentar' placeholder='Schreibe einen Kommentar...'></form></div></div><p></div></div><br>";
		return erg;
	}
	
	public Beitrag getBeitrag() {
		return beitrag;
	}

	public void setBeitrag(Beitrag beitrag) {
		this.beitrag = beitrag;
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
