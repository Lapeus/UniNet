package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Admin;
import de.dbae.uninet.javaClasses.Uni;

/**
 * Dieser Tag zeigt die Informationen aus dem Uni-Objekt auf der jsp an.
 * Er wird auf den Admin-Seiten des Projekts verwendet.
 * @author Leon Schaffert
 */
public class UniAnzeigeTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	/**
	 * Der Admin mit allen wichtigen Eigenschaften.
	 * @see Admin
	 */
	private Uni uni;


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
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die Uni.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String code = "";
		code += "<tr><td>" + uni.getUniID() + "</td>";
		code += "<td>" + uni.getUniName() + "</td>";
		code += "<td>" + uni.getUniStandort() + "<a class='pull-right' ";
		code += "href='UnisVerwaltenServlet?loeschen="+ uni.getUniID() + "' title='Uni l&ouml;schen'><span ";
		code += "class='glyphicon glyphicon glyphicon-trash' style='color: #3b5998;'></span></a>";
		code += "</td></tr>";
		return code;
	}
	
	/**
	 * Getter f&uuml;r die Uni.
	 * @return Das Uniobjekt
	 * @see Uni
	 */
	public Uni getUni() {
		return uni;
	}

	/**
	 * Setter f&uuml;r die Uni.
	 * @param uni Die Uni.
	 * @see Uni
	 */
	public void setUni(Uni uni) {
		this.uni = uni;
	}
}
