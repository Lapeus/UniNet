package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Emoticon;

/**
 * Dieser Tag zeigt in zwei Spalten eine Liste von Emoticons mit ihren K&uuml;rzeln an.<br>
 * Er wird ausschlie&szlig;lich auf der Hilfe-Seite verwendet.
 * @author Christian Ackermann
 */
public class EmoticonTag extends TagSupport {

	private static final long serialVersionUID = -803336778178995908L;
	
	/**
	 * Wahrheitswert, ob es die erste Zeile ist (&Uuml;berschriften m&uuml;ssen angezeigt werden).
	 */
	private boolean firstRow = false;
	/**
	 * Liste mit Emoticons, die auf der linken Seite angezeigt wird.
	 */
	private List<Emoticon> listLinks;
	
	/**
	 * Liste mit Emoticons, die auf der rechten Seite angezeigt wird.
	 */
	private List<Emoticon> listRechts;

	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen");
			// TODO Fehler
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die Tabelle mit den Emoticons.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String erg = "";
		erg += "<div class='row' style='background-color: white;'>";
		erg += "<div class='col-md-6'>";
		erg += "<table style='background-color: white;'>";
		// Wenn es die erste Spalte ist, muessen die Ueberschriften in schwarz angezeigt werden, sonst in weiss, damit die Abstaende trotzdem korrekt sind
		String farbe = firstRow ? "black" : "white";
		erg += "<tr><th style='padding: 12px; color: " + farbe + ";'>Emoticon</th><th style='padding: 12px; color: " + farbe + ";'>Kürzel</th></tr>";
		// Fuellung der linken Spalte der Tabelle mit allen in der Liste enthaltenen Emoticons
		for (Emoticon emo : listLinks) {
			erg += "<tr><td style='font-size: 24px; padding-left: 12px;'>" + emo.getBild() + "</td>";
			erg += "<td style='padding-left: 12px'>" + emo.getCode() + "</td></tr>";
		}
		erg += "</table></div>";		
		erg += "<div class='col-md-6'>";
		erg += "<table style='background-color: white;'>";
		erg += "<tr><th style='padding: 12px; color: " + farbe + ";'>Emoticon</th><th style='padding: 12px; color: " + farbe + ";'>Kürzel</th></tr>";
		// Fuellung der rechten Spalte der Tabelle mit allen in der Liste enthaltenen Emoticons
		for (Emoticon emo : listRechts) {
			erg += "<tr><td style='font-size: 24px; padding-left: 12px;'>" + emo.getBild() + "</td>";
			erg += "<td style='padding-left: 12px'>" + emo.getCode() + "</td></tr>";
		}
		erg += "</table></div>";				
		erg += "</div>";
		return erg;
	}

	/**
	 * Getter f&uuml;r die linke Emoticon-Liste.
	 * @return Die linke Emoticon-Liste
	 */
	public List<Emoticon> getListLinks() {
		return listLinks;
	}

	/**
	 * Setter f&uuml;r die linke Emoticon-Liste.
	 * @param listLinks Die linke Emoticon-Liste
	 */
	public void setListLinks(List<Emoticon> listLinks) {
		this.listLinks = listLinks;
	}

	/**
	 * Getter f&uuml;r die rechte Emoticon-Liste.
	 * @return Die rechte Emoticon-Liste
	 */
	public List<Emoticon> getListRechts() {
		return listRechts;
	}

	/**
	 * Setter f&uuml;r die rechte Emoticon-Liste.
	 * @param listRechts Die rechte Emoticon-Liste
	 */
	public void setListRechts(List<Emoticon> listRechts) {
		this.listRechts = listRechts;
	}

	/**
	 * Getter f&uuml;r den Wahrheitswert, ob es die erste Zeile ist.
	 * @return Wahrheitswert
	 */
	public boolean isFirstRow() {
		return firstRow;
	}

	/**
	 * Setter f&uuml;r den Wahrheitswert, ob es die erste Zeile ist.
	 * @param firstRow Wahrheitswert
	 */
	public void setFirstRow(boolean firstRow) {
		this.firstRow = firstRow;
	}
}
