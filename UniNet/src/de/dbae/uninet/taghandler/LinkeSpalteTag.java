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
		if (use.equals("standard")) {
			getHtmlCode(true);
			try {
				out.append(erg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SKIP_BODY;
		} else {
			erg += "<div class='linkeSpalte'>";
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
	
	public String getHtmlCode(boolean...optionalParameter) {
		boolean standard = optionalParameter.length > 0 ? optionalParameter[0] : false;
		String erg = "";
		erg += "<div class='linkeSpalte'>";
		erg += "<ul class='nav nav-pills nav-stacked menueSpalte'>";
		erg += "<li role='presentation'><a href='ProfilServlet'>Profil anzeigen</a></li>";
		erg += "<li role='presentation'><a href='ProfilBearbeitenServlet'>Profil bearbeiten</a></li><br>";
		erg += "<li role='presentation'><a href='NachrichtenServlet'>Nachrichten</a></li>";
		erg += "<li role='presentation'><a href='VeranstaltungenServlet?name=Uebersicht'>Veranstaltungen</a></li>";
		erg += "<li role='presentation'><a href='GruppenServlet?name=Uebersicht'>Gruppen</a></li></ul>";
		if (!standard) {
			erg +="</div>";
		}
		return erg;
	}
	
	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}
}
