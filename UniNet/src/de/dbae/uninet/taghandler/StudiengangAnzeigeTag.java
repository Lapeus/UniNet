package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Studiengang;
import de.dbae.uninet.javaClasses.Uni;

/**
 * Dieser Tag zeigt die Informationen aus dem Studiengang-Objekt auf der jsp an.
 * Er wird auf den LocalAdmin-Seiten des Projekts verwendet.
 * @author Leon Schaffert
 */
public class StudiengangAnzeigeTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	/**
	 * Der Studiengang mit allen wichtigen Eigenschaften.
	 * @see Studiengang
	 */
	private Studiengang studiengang;


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
	 * Generiert den auf der jsp angezeigten Code f&uuml;r den Studiengang.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String code = "";
		code += "<tr><td>" + studiengang.getStudiengangsID() + "</td>";
		code += "<td>" + studiengang.getStudiengangsName() + "<a class='pull-right' ";
		code += "href='AdminStudiengaengeServlet?loeschen="+ studiengang.getStudiengangsID() + "' title='Studiengang l&ouml;schen'><span ";
		code += "class='glyphicon glyphicon glyphicon-trash' style='color: #3b5998;'></span></a>";
		code += "</td></tr>";
		return code;
	}
	
	/**
	 * Getter f&uuml;r den Studiengang.
	 * @return Das Studiengangsobjekt
	 * @see Uni
	 */
	public Studiengang getStudiengang() {
		return studiengang;
	}

	/**
	 * Setter f&uuml;r den Studiengang.
	 * @param studiengang Der Studiengang.
	 * @see Studiengang
	 */
	public void setStudiengang(Studiengang studiengang) {
		this.studiengang = studiengang;
	}
}
