package de.dbae.uninet.javaClasses;

/**
 * Diese Klasse stellt alle wichtigen Informationen eines Kommentars zu einem Unterkommentar zur Verf&uuml;gung.<br>
 * Sie erbt wie die anderen Kommentare von AllgemeinerKommentar.
 * @see AllgemeinerKommentar
 * @author Christian Ackermann
 */
public class KommentarZuUnterkommentar extends AllgemeinerKommentar {

	/**
	 * Der Name des Verfassers des Unterkommentars, auf den Bezug genommen wird.
	 */
	private String antwortAufKommName;
	
	/**
	 * Die ID des Unterkommentars, auf den Bezug genommen wird.
	 */
	private int antwortAufKommID;
	
	/** 
	 * Erstellt einen neuen Kommentar zu einem Unterkommentar.<br>
	 * Einziger Konstruktor dieser Klasse.
	 * @param userID Die ID des Verfassers
	 * @param kommID Die KommentarID
	 * @param name Der Name des Verfassers
	 * @param kommentar Der Kommentar an sich
	 * @param antwortAufKommName Der Name des Verfassers des Unterkommentars, auf den Bezug genommen wird
	 * @param antwortAufKommID Die ID des Unterkommentars, auf den Bezug genommen wird
	 * @param timeStamp Der Zeitstempel der Verfassung des Kommentars
	 */
	public KommentarZuUnterkommentar(int userID, int kommID, String name, String kommentar, String antwortAufKommName, int antwortAufKommID, String timeStamp) {
		super(userID, antwortAufKommID, antwortAufKommName, kommentar, timeStamp);
		this.setAntwortAufKommName(antwortAufKommName);
		this.setAntwortAufKommID(antwortAufKommID);
	}

	/**
	 * Getter f&uuml;r den Namen des Verfassers des Unterkommentars.
	 * @return Der Name des Verfassers des Unterkommentars
	 */
	public String getAntwortAufKommName() {
		return antwortAufKommName;
	}

	/** 
	 * Setter f&uuml;r den Namen des Verfassers des Unterkommentars.
	 * @param antwortAufKommName Der Name des Verfassers des Unterkommentars
	 */
	public void setAntwortAufKommName(String antwortAufKommName) {
		this.antwortAufKommName = antwortAufKommName;
	}

	/**
	 * Getter f&uuml;r die KommentarID des Unterkommentars.
	 * @return Die KommentarID des Unterkommentars
	 */
	public int getAntwortAufKommID() {
		return antwortAufKommID;
	}

	/**
	 * Setter f&uuml;r die KommentarID des Unterkommentars.
	 * @param antwortAufKommID Die KommentarID des Unterkommentars
	 */
	public void setAntwortAufKommID(int antwortAufKommID) {
		this.antwortAufKommID = antwortAufKommID;
	}

}
