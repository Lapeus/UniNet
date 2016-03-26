package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class KopfzeileTag extends TagSupport {

	private static final long serialVersionUID = -5650080610199722432L;
	
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	public String getHtmlCode() {
		String kopfzeile = "";
		kopfzeile += "<nav class='navbar navbar-default navbar-fixed-top'>";
	    kopfzeile += "<div class='container-fluid kopfzeile'>";
		kopfzeile += "<div class='navbar-header nav-justified'>";
		kopfzeile += "<ul class='nav navbar-nav navbar-left'>";
		kopfzeile += "<li><label class='platzhalter'></label></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile logo' href='StartseiteServlet'><b>UNINET</b></a></li>";
		kopfzeile += "<li><form class='navbar-form navbar-left' role='search' action='SuchergebnisseServlet' method='get'>";
		kopfzeile += "<div class='form-group containerColor'>";
		kopfzeile += "<input type='text' class='form-control' name='suchanfrage' size=50 placeholder='Suchbegriff eingeben'>";
		kopfzeile += "</div>";
		kopfzeile += "<button type='submit' class='btn btn-default'>Suchen</button>";
		kopfzeile += "</form></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='ProfilServlet'>Profilseite</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='StartseiteServlet'>Startseite</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='NachrichtenServlet'><b>Chat(1)</b></a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='BenachrichtigungenServlet'><b>Benachrichtigung(2)</b></a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='FreundeFindenServlet'>Freunde finden</a></li>";
		kopfzeile += "</ul>";
		kopfzeile += "<ul class='nav navbar-nav navbar-right'>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='EmoticonServlet'>Hilfe</a></li>";
		kopfzeile += "<li><a class='navbar-brand kopfzeile' href='LogoutServlet'>Logout</a></li>";
		kopfzeile += "</ul>";
		kopfzeile += "</div></div></nav>";
		return kopfzeile;
	}

}
