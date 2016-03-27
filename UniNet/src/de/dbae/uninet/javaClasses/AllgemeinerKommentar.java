package de.dbae.uninet.javaClasses;

/**
 * Diese abstrakte Klasse stellt die allgemeinen Informationen eines Kommentars zur Verf&uuml;gung.<br>
 * Von ihr erben die Klassen der speziellen Kommentare.
 * @see Kommentar
 * @see Unterkommentar
 * @see KommentarZuUnterkommentar
 * @author Christian Ackermann
 *
 */
public abstract class AllgemeinerKommentar {

	/**
	 * Die ID des Verfassers.
	 */
	private int userID;
	
	/**
	 * Die KommentarID.
	 */
	private int kommID;
	
	/**
	 * Der Name des Verfassers.
	 */
	private String name;
	
	/**
	 * Der Kommentar an sich.
	 */
	private String kommentar;
	
	/**
	 * Der Zeitstempel der Verfassung des Kommentars.
	 */
	private String timeStamp;
	
	/**
	 * Erstellt einen allgemeinen Kommentar.
	 * @param userID Die ID des Verfassers
	 * @param kommID Die KommentarID
	 * @param name Der Name des Verfassers
	 * @param kommentar Der Kommentar an sich
	 * @param timeStamp Der Zeitstempel der Verfassung des Kommentars
	 */
	public AllgemeinerKommentar(int userID, int kommID, String name, String kommentar, String timeStamp) {
		this.setUserID(userID);
		this.setKommID(kommID);
		this.setName(name);
		this.setKommentar(kommentar);
		this.setTimeStamp(timeStamp);
	}
	
	/**
	 * Getter f&uuml;r die ID des Verfassers.
	 * @return Die ID des Verfassers
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Setter f&uuml; die ID des Verfassers.
	 * @param userID Die ID des Verfassers
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * Getter f&uuml;r die KommentarID.
	 * @return Die KommentarID
	 */
	public int getKommID() {
		return kommID;
	}

	/**
	 * Setter f&uuml; die KommentarID.
	 * @param kommID Die KommentarID
	 */
	public void setKommID(int kommID) {
		this.kommID = kommID;
	}

	/**
	 * Getter f&uuml;r den Namen des Verfassers.
	 * @return Der Name des Verfassers
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter f&uuml; den Namen des Verfassers.
	 * @param name Der Name des Verfassers
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter f&uuml;r den Kommentar an sich.
	 * @return Der Kommentar
	 */
	public String getKommentar() {
		return kommentar;
	}

	/**
	 * Setter f&uuml; den Kommentar an sich.
	 * @param kommentar Der Kommentar
	 */
	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	/**
	 * Getter f&uuml;r den Zeitstempel der Verfassung des Kommentars.
	 * @return Der Zeitstempel
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Setter f&uuml; den Zeitstempel der Verfassung des Kommentars.
	 * @param timeStamp Der Zeitstempel
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
