package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Freundschaftsanfrage;


public class FreundesanfrageTag extends TagSupport {
	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private Freundschaftsanfrage anfrage = null;

	// GRUPPE
	public boolean isAnfrage() {
		return anfrage != null;
	}
	
	public Freundschaftsanfrage getRequest() {
		return anfrage;
	}

	public void setRequest(Freundschaftsanfrage anfrage) {
		this.anfrage = anfrage;
	}

	@Override
	public int doStartTag() throws JspException {
		Writer page = pageContext.getOut();
		try {
			if (isAnfrage()) {
				page.append(getGruppeJSPCode());
			}
		} catch (IOException e) {
			System.out.println("FreundesanfrageTag - doStartTag");
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String getGruppeJSPCode() {
		// Datum formatieren
		String sDatum = "Keine Daten";
		if (anfrage.getDatum() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date datum = anfrage.getDatum();
			sDatum = dateFormat.format(datum);
		}
		System.out.println("FREUND ID: " + anfrage.getFreundID());
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ "<a href='ProfilServlet?userID=" + anfrage.getFreundID() + "' title='Lorem ipsum' class='thumbnail'><img class='img-responsive' alt='Profilbild' src='LadeProfilbildServlet?userID="+ anfrage.getFreundID() +"'/></a>"
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'>"
				+ "<ul class='meta-search'>"
				+ "<li><i class='glyphicon glyphicon-calendar'></i><span>"+ sDatum +"</span></li>"				
				+ "<li><i class='glyphicon glyphicon-tags'></i> <span>Freundschaftsanfrage</span></li>"
				+ "</ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<a class='verfasser' href='ProfilServlet?userID=" + anfrage.getFreundID() + "'>"+ anfrage.getFreundName() +"</a> m&ouml;chte mit dir befreundet sein!<br>"
				+ "<span>"
				+ "<button class='button btn-success' type='submit' name='accept' value='"+ anfrage.getFreundID() +"'>Freunschaftsanfrage bestätigen</button>"
				+ "</span>"
				+ "<span>"
				+ "<button class='button btn-danger' type='submit' name='denie' value='"+ anfrage.getFreundID() +"'>Freunschaftsanfrage ablehnen</button>"
				+ "</span>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";
		System.out.println(jsp);
		return jsp;
	}
}
