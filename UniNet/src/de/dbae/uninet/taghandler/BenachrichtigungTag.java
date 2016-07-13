package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Benachrichtigung;

/**
 * Dieser Tag dient der Darstellung einer Benachrichtigung.
 * Er wird auf der Benachrichtigung.jsp verwendet.
 * @author Marvin Wolf
 */
public class BenachrichtigungTag extends TagSupport {
	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private Benachrichtigung benachrichtigung = null;

	// GRUPPE
	public boolean isBenachrichtigung() {
		return benachrichtigung != null;
	}
	
	public Benachrichtigung getNot() {
		return benachrichtigung;
	}

	public void setNot(Benachrichtigung benachrichtigung) {
		this.benachrichtigung = benachrichtigung;
	}

	@Override
	public int doStartTag() throws JspException {
		Writer page = pageContext.getOut();
		try {
			if (isBenachrichtigung()) {
				page.append(getGruppeJSPCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String getGruppeJSPCode() {
		// Datum formatieren
		String sDatum = "Keine Daten";
		if (benachrichtigung.getDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date datum = benachrichtigung.getDate();
			sDatum = dateFormat.format(datum);
		}
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ ""
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'>"
				+ "<ul class='meta-search'>"
				+ "<li><i class='glyphicon glyphicon-calendar'></i><span>"+ sDatum +"</span></li>"				
				+ "<li><i class='glyphicon glyphicon-tags'></i> <span>Benachrichtigung</span></li>"
				+ "</ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<span>"
				+ benachrichtigung.getHtml()
				+ "</span>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";
		return jsp;
	}
}
