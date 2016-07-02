package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Gruppe;
import de.dbae.uninet.javaClasses.Student;

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
			System.out.println("GesuchteGruppeTag - doStartTag");
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String getGruppeJSPCode() {
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ "<a href='#' title='Lorem ipsum' class='thumbnail'><img alt='Profilbild'/></a>"
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'>"
				+ "<ul class='meta-search'>"
				+ "<li><i class='glyphicon glyphicon-calendar'></i><span>"+ group.getGruendung() +"</span></li>"
				+ "<li><i class='glyphicon glyphicon-user'></i><span>"+ group.getAdmin() +"</span></li>"				
				+ "<li><i class='glyphicon glyphicon-tags'></i> <span>Gruppe</span></li>"
				+ "</ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<h3><a href='GruppenServlet?tab=beitraege&gruppenID="+ group.getId() +"' title=''>"+ group.getName() +"</a></h3>"
				+ "<p>" + group.getBeschreibung() + "</p>"
				+ "</div>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";;
		return jsp;
	}
}
