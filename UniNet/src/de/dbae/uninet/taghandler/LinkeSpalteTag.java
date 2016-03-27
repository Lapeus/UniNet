package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Dieser Tag erstellt die linke Spalte der jsps.<br>
 * Mit dem optionalen Attribut use kann entweder eine Standard- oder eine benutzerdefinierte Kopfzeile erstellt werden.
 * @author Christian Ackermann
 */
public class LinkeSpalteTag extends TagSupport {

	private static final long serialVersionUID = 1291626405868011739L;
	
	/**
	 * Attribut um zu definieren, ob die Spalte standardm&auml;&szlig;ig oder benutzerdefiniert sein soll.<br>
	 * use = 'standard' erzeugt die Standard-Spalte. Dabei wird der Body &uuml;bersprungen.
	 * Soll die Spalte benutzerdefiniert sein, wird das Attribut weggelassen und der Aufbau richtet sich nach dem Body.
	 */
	private String use = "";

	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body (use='standard') oder ber&uuml;cksichtigt ihn.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		String erg = "";
		try {
			if (use.equals("standard")) {
				getHtmlCode(true);
				out.append(erg);
				return SKIP_BODY;
			} else {
				erg += "<div class='linkeSpalte'>";
				out.append(erg);
				return EVAL_BODY_INCLUDE;
			}
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
			return SKIP_BODY;
		}
		
	}
	
	/**
	 * Aktionen zum Abschluss des Tags.<br>
	 * F&uuml;r den Fall der benutzerdefinierten Spalte muss der Container-Tag noch geschlossen werden.
	 */
	public int doEndTag() {
		Writer out = pageContext.getOut();
		try {
			out.append("</div>");
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die linke Spalte.<br>
	 * @param optionalParameter Ein optionaler Parameter, analog zu use
	 * @return Den entsprechenden Html-Code
	 */
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
	
	/**
	 * Getter f&uuml;r use.
	 * @return Den entsprechenden String
	 */
	public String getUse() {
		return use;
	}

	/**
	 * Setter f&uuml;r use.
	 * @param use Der entsprechende String
	 */
	public void setUse(String use) {
		this.use = use;
	}
}

