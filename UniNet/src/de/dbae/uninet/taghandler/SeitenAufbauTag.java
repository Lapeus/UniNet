package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Student;

/**
 * Dieser Tag erstellt den kompletten Standardaufbau einer jsp.<br>
 * Dies umfasst eine Kopfzeile, die standardm&auml;&szlig;ige linke Spalte, 
 * einen variablen Mittelblock im Body und die Chatspalte rechts.
 * @author Christian Ackermann
 */
public class SeitenAufbauTag extends TagSupport {
	
	private static final long serialVersionUID = 929940257312046566L;
	
	/**
	 * Eine Liste mit Freunden zum Chatten die gerade online sind.
	 * @see Student
	 * @see LinkeSpalteTag
	 */
	private List<Student> chatfreunde;
	
	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * F&uuml;gt eine Kopfzeile und eine linke Spalte hinzu, sowie den Aufbau f&uuml;r den mittleren Teil.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		String erg = "";
		erg += new KopfzeileTag().getHtmlCode();
		erg += "<div class='mainPart'>";
		erg += new LinkeSpalteTag().getHtmlCode();
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
	 * Schlie&szlig;t das Konstrukt der mittleren Spalte und h&auml;ngt die rechte Spalte an die jsp an.
	 */
	public int doEndTag() {
		Writer out = pageContext.getOut();
		String erg = "";
		erg += "</div><div class='col-md-1'></div></div></div>";
		erg += new RechteSpalteTag().getHtmlCode(chatfreunde);
		try {
			out.append(erg);
		} catch (IOException e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	/**
	 * Getter f&uuml;r die Chatfreunde
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
