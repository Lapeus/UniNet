package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Dieser Tag erstellt die mittlere Spalte.<br>
 * Er wird direkt oder indirekt auf jeder jsp verwendet.
 * @author Christian Ackermann
 */
public class MittlereSpalteTag extends TagSupport {

	private static final long serialVersionUID = 9133244508825478468L;

	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * F&uuml;gt das Konstrukt f&uuml;r die mittlere Spalte in die jsp ein.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		String erg = "";
		erg += "<div class='mittlereSpalte'><div class='row'><div class='col-md-1'></div><div class='col-md-10'>";
		try {
			out.append(erg);
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * Aktionen zum Abschluss des Tags.<br>
	 * Schlie&szlig;t die Container des Konstruktes f&uuml;r die mittlere Spalte.
	 */
	public int doEndTag() {
		Writer out = pageContext.getOut();
		String erg = "";
		erg += "</div><div class='col-md-1'></div></div></div>";
		try {
			out.append(erg);
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
