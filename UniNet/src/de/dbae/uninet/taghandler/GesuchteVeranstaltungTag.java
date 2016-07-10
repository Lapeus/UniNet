package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Veranstaltung;

/**
 * Dieser Tag dient der Darstellung einer gesuchten Veranstaltung.
 * Er wird auf der Suchergebnisse.jsp verwendet.
 * @author Marvin Wolf
 */
public class GesuchteVeranstaltungTag extends TagSupport {
	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private Veranstaltung veranstaltung = null;

	// veranstaltung
	public boolean isveranstaltung() {
		return veranstaltung != null;
	}
	
	public Veranstaltung getVeranstaltungen() {
		return veranstaltung;
	}

	public void setVeranstaltungen(Veranstaltung veranstaltung) {
		this.veranstaltung = veranstaltung;
	}

	@Override
	public int doStartTag() throws JspException {
		Writer page = pageContext.getOut();
		try {
			if (isveranstaltung()) {
				page.append(getVeranstaltungJSPCode());
			}
		} catch (IOException e) {
			System.out.println("GesucheteVeranstaltungTag - doStartTag");
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String getVeranstaltungJSPCode() {
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ ""
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'>"
				+ "<ul class='meta-search'>"
				+ "<li><i class='glyphicon glyphicon-education'></i><span>"+ veranstaltung.getDozent() +"</span></li>"
				+ "<li><i class='glyphicon glyphicon-tasks'></i><span>"+ veranstaltung.getSemester() +"</span></li>"
				+ "<li><i class='glyphicon glyphicon-tags'></i> <span>Veranstaltung</span></li>"
				+ "</ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<h3><a href='VeranstaltungenServlet?tab=infos&veranstaltungsID="+ veranstaltung.getId() +"' title=''>"+ veranstaltung.getName() +"</a></h3>"
				+ "<p>" + veranstaltung.getBeschreibung() + "</p>"
				+ "</div>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";;
		return jsp;
	}
}
