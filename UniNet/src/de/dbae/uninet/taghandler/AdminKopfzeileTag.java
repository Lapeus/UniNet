package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Dieser Tag erstellt die Kopfzeile.<br>
 * Er wird direkt oder indirekt auf jeder jsp verwendet.
 * @author Leon Schaffert
 */
public class AdminKopfzeileTag extends TagSupport {

	private static final long serialVersionUID = -5650080610199722432L;
	
	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			System.out.println("Fehler beim Anh�ngen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die Kopfzeile.<br>
	 * @return Den entsprechenden Html-Code
	 */
	public String getHtmlCode() {
		String kopfzeile = "";
		kopfzeile += "<nav class='navbar navbar-default navbar-fixed-top'>";
		kopfzeile += "<div class='container-fluid kopfzeile'>";
		kopfzeile += "<div class='navbar-header nav-justified'>";
		kopfzeile += "<ul class='nav navbar-nav navbar-left'>";
		kopfzeile += "<li><label class='platzhalter'></label></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile logo'><b>UNINET</b></a></li></ul>";
		kopfzeile += "<ul class='nav navbar-nav navbar-right'>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='UnisVerwaltenServlet'>Universit&auml;ten Verwalten</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='AdminAnlegenServlet'>Administrator anlegen</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='AdminServlet'>Administratoren verwalten</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='LogoutServlet'>Logout</a></li>";
		kopfzeile += "</ul></div></div></nav>";
		return kopfzeile;
	}

}
