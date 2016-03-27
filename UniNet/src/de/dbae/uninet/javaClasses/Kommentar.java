package de.dbae.uninet.javaClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt alle wichtigen Informationen eines Kommentars zur Verf&uuml;gung.<br>
 * Sie erbt wie die anderen Kommentare von AllgemeinerKommentar.
 * @see AllgemeinerKommentar
 * @author Christian Ackermann
 */
public class Kommentar extends AllgemeinerKommentar {

	/**
	 * Die Liste der Unterkommentare.
	 * @see Unterkommentar
	 */
	private List<Unterkommentar> kommentarList = new ArrayList<Unterkommentar>();
	
	/**
	 * Die zugeh&ouml;rige BeitragsID.
	 */
	private int zielID;
	
	/**
	 * Die ID des aktuellen Users zum Test, ob der Beitrag von ihm ist.
	 */
	private int userIDsession;
	
	/** 
	 * Erstellt einen neuen Kommentar mit allen wichtigen Eigenschaften.<br>
	 * Einziger Konstruktor dieser Klasse.
	 * @param userID Die ID des Verfassers
	 * @param kommID Die KommentarID
	 * @param name Der Name des Verfassers
	 * @param kommentar Der Kommentar an sich
	 * @param kommentarList Die Liste aller Unterkommentare zu diesem Kommentar
	 * @param zielID Die zugeh&ouml;rige BeitragsID
	 * @param userIDsession Die ID des aktuellen Users
	 * @param timeStamp Der Zeitstempel der Verfassung des Kommentars
	 */
	public Kommentar(int userID, int kommID, String name, String kommentar, List<Unterkommentar> kommentarList, int zielID, int userIDsession, String timeStamp) {
		super(userID, kommID, name, kommentar, timeStamp);
		this.setKommentarList(kommentarList);
		this.setZielID(zielID);
		this.setUserIDsession(userIDsession);
	}

	/**
	 * Getter f&uuml;r die Liste der Unterkommentare.
	 * @return Die Liste der Unterkommentare
	 * @see Unterkommentar
	 */
	public List<Unterkommentar> getKommentarList() {
		return kommentarList;
	}

	/**
	 * Setter f&uuml; die Liste der Unterkommentare.
	 * @param kommentarList Die Liste der Unterkommentare
	 */
	public void setKommentarList(List<Unterkommentar> kommentarList) {
		this.kommentarList = kommentarList;
	}

	/**
	 * Getter f&uuml;r die zugeh&ouml;rige BeitragsID.
	 * @return Die BeitragsID
	 */
	public int getZielID() {
		return zielID;
	}

	public void setZielID(int zielID) {
		this.zielID = zielID;
	}

	/**
	 * Getter f&uuml;r die ID des aktuellen Users.
	 * @return Die ID des aktuellen Users
	 */
	public int getUserIDsession() {
		return userIDsession;
	}

	/**
	 * Setter f&uuml; die ID des aktuellen Users.
	 * @param userIDsession Die ID des aktuellen Users
	 */
	public void setUserIDsession(int userIDsession) {
		this.userIDsession = userIDsession;
	}

}
