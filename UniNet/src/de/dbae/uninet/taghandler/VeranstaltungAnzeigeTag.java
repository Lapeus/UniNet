package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Studiengang;
import de.dbae.uninet.javaClasses.Veranstaltung;

/**
 * Dieser Tag zeigt die Informationen aus dem Veranstaltung-Objekt auf der jsp an.
 * Er wird auf den LocalAdmin-Seiten des Projekts verwendet.
 * @author Leon Schaffert
 */
public class VeranstaltungAnzeigeTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	/**
	 * Die Veranstaltung mit allen wichtigen Eigenschaften.
	 * @see Veranstaltung
	 */
	private Veranstaltung veranstaltung;


	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (Exception e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die Veranstaltung.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String code = "";
		code += "<tr><td>" + veranstaltung.getId() + "</td>";
		code += "<td><a href='AdminVeranstaltungBearbeitenServlet?veranstaltungsid=" + veranstaltung.getId();
		code += "' title='Veranstaltung bearbeiten'>" + veranstaltung.getName() + "</a></td>";
		code += "<td>" + veranstaltung.getDozent() + "</td>";
		code += "<td>" + veranstaltung.getSemester() + "<a class='pull-right' ";
		code += "href='AdminVeranstaltungenVerwaltenServlet?loeschen=" + veranstaltung.getId() + "' title='Veranstaltung l&ouml;schen'><span ";
		code += "class='glyphicon glyphicon glyphicon-trash' style='color: #3b5998;'></span></a>";
		code += "</td></tr>";
		return code;
	}
	
	/**
	 * Getter f&uuml;r die Veranstaltung.
	 * @return Das Veranstaltungsobjekt
	 * @see Uni
	 */
	public Veranstaltung getVeransaltung() {
		return veranstaltung;
	}

	/**
	 * Setter f&uuml;r die Veranstaltung.
	 * @param veranstaltung Die Veranstaltung.
	 * @see Veranstaltung
	 */
	public void setVeranstaltung(Veranstaltung veranstaltung) {
		this.veranstaltung = veranstaltung;
	}
}
