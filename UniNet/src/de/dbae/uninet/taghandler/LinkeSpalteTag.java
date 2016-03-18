package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class LinkeSpalteTag extends TagSupport {

	private static final long serialVersionUID = 1291626405868011739L;
	private String use = "";

	public int doStartTag() {
		Writer out = pageContext.getOut();
		String erg = "";
		erg += "<div class='linkeSpalte'>";
		if (use.equals("standard")) {
			erg += "<ul class='nav nav-pills nav-stacked menueSpalte'>";
			erg += "<li role='presentation'><a href='ProfilServlet'>Profil anzeigen</a></li>";
			erg += "<li role='presentation'><a href='#'>Profil bearbeiten</a></li><br>";
			erg += "<li role='presentation'><a href='NachrichtenServlet'>Nachrichten</a></li>";
			erg += "<li role='presentation'><a href='#'>Veranstaltungen</a></li>";
			erg += "<li role='presentation'><a href='#'>Gruppen</a></li></ul>"; 
			try {
				out.append(erg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SKIP_BODY;
		} else {
			try {
				out.append(erg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return EVAL_BODY_INCLUDE;
		}
	}
	
	public int doEndTag() {
		Writer out = pageContext.getOut();
		try {
			out.append("</div>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	public String getHtmlCode() {
		String erg = "";
		erg += "<div class='linkeSpalte'>";
		erg += "<ul class='nav nav-pills nav-stacked menueSpalte'>";
		erg += "<li role='presentation'><a href='ProfilServlet'>Profil anzeigen</a></li>";
		erg += "<li role='presentation'><a href='#'>Profil bearbeiten</a></li><br>";
		erg += "<li role='presentation'><a href='NachrichtenServlet'>Nachrichten</a></li>";
		erg += "<li role='presentation'><a href='#'>Veranstaltungen</a></li>";
		erg += "<li role='presentation'><a href='#'>Gruppen</a></li></ul></div>"; 
		return erg;
	}
	
	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}
}
