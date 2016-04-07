package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Student;

/**
 * Dieser Tag erstellt die rechte Spalte der jsps.<br>
 * Das Attribut chatfreunde ist eine Liste von Studenten, die momentan online sind und mit denen gechattet werden kann.
 * @author Christian Ackermann
 */
public class RechteSpalteTag extends TagSupport {

	private static final long serialVersionUID = 2224529992245088071L;
	
	/**
	 * Eine Liste mit Freunden zum Chatten die gerade online sind.
	 * @see Student
	 */
	private List<Student> chatfreunde;
	
	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode(chatfreunde));
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r die rechte Spalte.<br>
	 * @param chatfreunde Die Liste mit den Chatfreunden
	 * @return Den entsprechenden Html-Code
	 */
	public String getHtmlCode(List<Student> chatfreunde) {
		String erg = "<div class='rechteSpalte'>";
		erg += "<ul class='nav nav-stacked chatSpalte'>";
		erg += "<li role='presentation'><H4 class='mittig'><b>FREUNDE";
		erg += " online</b></H4></li><br>";
		erg += "<li><ul class='nav nav-pills nav-stacked'>";
		int anzahlOnline = 0;
		// Fuer jeden Freund aus der Liste
		for (Student freund : chatfreunde) {
			String online = "";
			// Schaue ob er online ist
			if (freund.isOnline()) {
				anzahlOnline++;
				// Wenn ja, haenge das Online-Symbol an seinen Namen
				online = "<span class='glyphicon glyphicon-record pull-right' style='color: #00aa00;'></span>";
			}
			erg += "<li role='presentation' class='chatfreunde'><a href='NachrichtenServlet?userID=" + freund.getUserID()+ "'><img class='media-object kopfzeile' alt='' src='LadeProfilbildServlet?userID=" + freund.getUserID() + "'</img>&nbsp;" + freund.getVorname() + " " + freund.getNachname() + online + "</a></li>";
		}
		erg += "</ul></ul></div>";
		if (anzahlOnline == 1) 
			erg = erg.replace("FREUNDE", "1 Freund");
		else 
			erg = erg.replace("FREUNDE", anzahlOnline + " Freunde");
		return erg;
	}
	
	/**
	 * Getter f&uuml;r die Chatfreunde.
	 * @return Die Liste der Chatfreunde
	 */
	public List<Student> getChatfreunde() {
		return chatfreunde;
	}

	/**
	 * Setter f&uuml;r die Chatfreunde
	 * @param chatfreunde Die Liste der Chatfreunde
	 */
	public void setChatfreunde(List<Student> chatfreunde) {
		this.chatfreunde = chatfreunde;
	}

}