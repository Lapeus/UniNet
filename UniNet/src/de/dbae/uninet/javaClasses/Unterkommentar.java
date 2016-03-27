package de.dbae.uninet.javaClasses;

import java.util.List;

/**
 * Diese Klasse stellt alle Informationen eines Unterkommentars zur Verf&uuml;gung.<br>
 * Sie erbt wie die anderen Kommentare von AllgemeinerKommentar.
 * @see AllgemeinerKommentar
 * @author Christian Ackermann
 */
public class Unterkommentar extends AllgemeinerKommentar {

	/**
	 * Die ID des zugeh&ouml;rigen Kommentars.
	 */
	private int antwortAufKommID;
	
	/**
	 * Die Liste der Kommentare zu diesem Unterkommentar.
	 */
	private List<KommentarZuUnterkommentar> kommentarList;
	
	/**
	 * Erstellt einen neuen Unterkommentar.<br>
	 * Einziger Konstruktor dieser Klasse.
	 * @param userID Die ID des Verfassers
	 * @param kommID Die KommentarID
	 * @param name Der Name des Verfassers
	 * @param kommentar Der Kommentar an sich
	 * @param antwortAufKommID Die KommentarID des zugeh&ouml;rigen Kommentars
	 * @param timeStamp Der Zeitstempel der Verfassung des Kommentars
	 */
	public Unterkommentar(int userID, int kommID, String name, String kommentar, int antwortAufKommID, String timeStamp) {
		super(userID, antwortAufKommID, name, kommentar, timeStamp);
		this.setAntwortAufKommID(antwortAufKommID);
	}

	/** 
	 * Getter f&uuml;r die Liste der Kommentare zu diesem Unterkommentar.
	 * @return Die Liste der Kommentare zu diesem Unterkommentar
	 */
	public List<KommentarZuUnterkommentar> getKommentarList() {
		return kommentarList;
	}

	/**
	 * Setter f&uuml;r die Liste der Kommentare zu diesem Unterkommentar.
	 * @param kommentarList Die Liste der Kommentare zu diesem Unterkommentar
	 */
	public void setKommentarList(List<KommentarZuUnterkommentar> kommentarList) {
		this.kommentarList = kommentarList;
	}

	/**
	 * Getter f&uuml;r die KommentarID des zugeh&ouml;rigen Kommentars.
	 * @return Die KommentarID des zugeh&ouml;rigen Kommentars
	 */
	public int getAntwortAufKommID() {
		return antwortAufKommID;
	}

	/**
	 * Setter f&uuml;r die KommentarID des zugeh&ouml;rigen Kommentars.
	 * @param antwortAufKommID Die KommentarID des zugeh&ouml;rigen Kommentars
	 */
	public void setAntwortAufKommID(int antwortAufKommID) {
		this.antwortAufKommID = antwortAufKommID;
	}
}
