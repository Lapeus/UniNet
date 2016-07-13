package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Gruppe;

/**
 * Dieser Tag dient der Darstellung einer gesuchten Gruppe.
 * Er wird auf der Suchergebnisse.jsp verwendet.
 * @author Marvin Wolf
 */
public class GesuchteGruppeTag extends TagSupport {
	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private Gruppe group = null;

	// GRUPPE
	public boolean isGruppe() {
		return group != null;
	}
	
	public Gruppe getGroup() {
		return group;
	}

	public void setGroup(Gruppe gruppe) {
		this.group = gruppe;
	}

	@Override
	public int doStartTag() throws JspException {
		Writer page = pageContext.getOut();
		try {
			if (isGruppe()) {
				page.append(getGruppeJSPCode());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String getGruppeJSPCode() {
		// Beschreibung 
		String beschreibung = "Keine Gruppenbeschreibung vorhanden.";
		if (group.getBeschreibung() != null) {
			beschreibung = group.getBeschreibung();
		}
		// Datum
			String sDatum = "Keine Angaben";
			if (group.getGruendung() != null) {
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatDate2 = new SimpleDateFormat("dd.MM.yyyy");
				try {
					Date datum = formatDate.parse(group.getGruendung());
					sDatum = formatDate2.format(datum);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ ""
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'>"
				+ "<ul class='meta-search'>"
				+ "<li><i class='glyphicon glyphicon-calendar'></i><span>"+ sDatum +"</span></li>"
				+ "<li><i class='glyphicon glyphicon-user'></i><span>"+ group.getAdmin() +"</span></li>"				
				+ "<li><i class='glyphicon glyphicon-tags'></i> <span>Gruppe</span></li>"
				+ "</ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<h3><a href='GruppenServlet?tab=beitraege&gruppenID="+ group.getId() +"' title=''>"+ group.getName() +"</a></h3>"
				+ "<p>" + beschreibung + "</p>"
				+ "</div>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";
		return jsp;
	}
}
