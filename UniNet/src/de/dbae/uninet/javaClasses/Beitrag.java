package de.dbae.uninet.javaClasses;

import java.util.List;

/**
 * Diese Klasse verwaltet alle wichtigen Informationen eines Beitrags.<br>
 * Ein Objekt dieser Klasse kann dem Beitrag-Tag &uuml;bergeben werden, sodass dieser dann alle Informationen
 * entsprechend darstellen kann.
 * @author Christian Ackermann
 */
public class Beitrag {

	/**
	 * Die UserID des Verfassers.
	 */
	private int userID;
	
	/**
	 * Der Name des Verfassers.
	 */
	private String name;
	
	/**
	 * Der Zeitstempel der Verfassung (dd.mm.yyyy hh:mm:ss Sichtbarkeit)
	 */
	private String timeStamp;
	
	/**
	 * Der Beitrag selbst.
	 */
	private String nachricht;
	
	/**
	 * Anzahl der Likes.
	 */
	private int likes;
	
	/**
	 * Anzahl der Kommentare.
	 */
	private int kommentare;
	
	/**
	 * Die BeitragsID.
	 */
	private int beitragsID;
	
	/**
	 * Angabe, ob der Beitrag vom aktuellen Nutzer mit 'interessiert mich nicht besonders' markiert wurde.
	 */
	private boolean like;
	
	/**
	 * Liste aller Kommentare, die zu diesem Beitrag verfasst wurden.
	 * @see Kommentar
	 */
	private List<Kommentar> kommentarList;
	
	/**
	 * Gibt an, ob der Beitrag vom aktuellen Nutzer verfasst wurde, sodass dieser ihn l&ouml;schen darf.
	 */
	private boolean loeschenErlaubt;
	
	/**
	 * Name der Gruppe / Veranstaltung, in dem der Beitrag gepostet wurde.<br>
	 * Falls er in der Chronik gepostet wurde, ist der Wert standardm&auml;&szlig;ig leer.
	 */
	private String ortName = "";
	
	private String ortLink = "";
	
	/**
	 * Gibt an, ob der Beitrag bereits bearbeitet wurde.
	 */
	private boolean bearbeitet = false;
	
	/**
	 * Erstellt einen Beitrag mit allen Eigenschaften.<br>
	 * Einziger Konstruktor dieser Klasse.
	 * @param userID Die ID des Verfassers
	 * @param name Der Name des Verfassers
	 * @param timeStamp Der Zeitstempel der Verfassung
	 * @param nachricht Der Beitrag an sich
	 * @param likes Die Anzahl der Personen, die das nicht besonders interessiert
	 * @param kommentare Die Anzahl der Kommentare
	 * @param beitragsID Die ID des Beitrags
	 * @param like Wahrheitswert, ob der Beitrag den aktuellen Nutzer nicht besonders interessiert
	 * @param loeschenErlaubt Wahrheitswert, ob der Beitrag vom aktuellen Nutzer gel&ouml;scht werden darf
	 * @param bearbeitet Wahrheitswert, ob der Beitrag bearbeitet wurde
	 */
	public Beitrag(int userID, String name, String timeStamp, String nachricht, int likes, int kommentare, int beitragsID, boolean like, boolean loeschenErlaubt, boolean bearbeitet) {
		this.setUserID(userID);
		this.setName(name);
		this.setTimeStamp(timeStamp);
		this.setNachricht(nachricht);
		this.setLikes(likes);
		this.setKommentare(kommentare);
		this.setBeitragsID(beitragsID);
		this.setLike(like);
		this.setLoeschenErlaubt(loeschenErlaubt);
		this.setBearbeitet(bearbeitet);
	}

	/**
	 * Getter f&uuml;r die UserID.
	 * @return Die UserID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Setter f&uuml;r die UserID.
	 * @param userID Die UserID
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * Getter f&uuml;r den Namen des Verfassers.
	 * @return Der Name des Verfassers
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter f&uuml;r den Namen des Verfassers.
	 * @param name Der Name des Verfassers
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter f&uuml;r den Zeitstempel.
	 * @return Der Zeitstempel
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

    /**
     * Setter f&uuml;r den Zeitstempel der Verfassung des Beitrags
     * @param timeStamp Der Zeitstempel
     */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Getter f&uuml;r den Beitrag an sich.
	 * @return Der Beitrag
	 */
	public String getNachricht() {
		return nachricht;
	}

	/**
	 * Setter f&uuml;r den eigentlichen Beitrag
	 * @param nachricht Der eigentliche Beitrag
	 */
	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

	/**
	 * Getter f&uuml;r die Anzahl der Likes.
	 * @return Die Anzahl der Likes
	 */
	public int getLikes() {
		return likes;
	}

	/**
	 * Setter f&uuml;r die Anzahl der Likes.
	 * @param likes Die Anzahl der Likes
	 */
	public void setLikes(int likes) {
		this.likes = likes;
	}

	/**
	 * Getter f&uuml;r die Anzahl der Kommentare.
	 * @return Die Anzahl der Kommentare
	 */
	public int getKommentare() {
		return kommentare;
	}

	/**
	 * Setter f&uuml;r die Anzahl der Kommentare.
	 * @param kommentare Die Anzahl der Kommentare
	 */
	public void setKommentare(int kommentare) {
		this.kommentare = kommentare;
	}

	/**
	 * Getter f&uuml;r die BeitragsID.
	 * @return Die BeitragsID
	 */
	public int getBeitragsID() {
		return beitragsID;
	}

	/**
	 * Setter f&uuml;r die BeitragsID
	 * @param beitragsID Die BeitragsID
	 */
	public void setBeitragsID(int beitragsID) {
		this.beitragsID = beitragsID;
	}

	/**
	 * Getter f&uuml;r den Wahrheitswert, ob der Beitrag den aktuellen Nutzer nicht besonders interessiert.
	 * @return Wahrheitswert
	 */
	public boolean isLike() {
		return like;
	}

	/**
	 * Setter f&uuml;r den Wahrheitswert, ob der Beitrag den aktuellen Nutzer nicht besonders interessiert.
	 * @param like Wahrheitswert
	 */
	public void setLike(boolean like) {
		this.like = like;
	}

	/**
	 * Getter f&uuml;r die Liste der Kommentare.
	 * @return Die Liste der Kommentare
	 */
	public List<Kommentar> getKommentarList() {
		return kommentarList;
	}

	/**
	 * Setter f&uuml;r die Liste der Kommentare.
	 * @param kommentarList Die Liste der Kommentare
	 */
	public void setKommentarList(List<Kommentar> kommentarList) {
		this.kommentarList = kommentarList;
	}

	/**
	 * Getter f&uuml;r den Wahrheitswert, ob der Beitrag vom Nutzer gel&ouml;scht werden darf.
	 * @return Wahrheitswert
	 */
	public boolean getLoeschenErlaubt() {
		return loeschenErlaubt;
	}

	/**
	 * Setter f&uuml;r den Wahrheitswert, ob der Beitrag vom Nutzer gel&ouml;scht werden darf.
	 * @param loeschenErlaubt Wahrheitswert
	 */
	public void setLoeschenErlaubt(boolean loeschenErlaubt) {
		this.loeschenErlaubt = loeschenErlaubt;
	}

	/**
	 * Getter f&uuml;r den Namen des Ortes, an dem der Beitrag gepostet wurde.
	 * @return Name des Ortes
	 */
	public String getOrtName() {
		return ortName;
	}

	/**
	 * Setter f&uuml;r den Namen des Ortes, an dem der Beitrag gepostet wurde.
	 * @param ortName Der Name des Ortes
	 */
	public void setOrtName(String ortName) {
		this.ortName = ortName;
	}

	/**
	 * Getter f&uuml;r den Wahrheitswert, ob der Beitrag bearbeitet wurde.
	 * @return Wahrheitswert
	 */
	public boolean isBearbeitet() {
		return bearbeitet;
	}

	/**
	 * Setter f&uuml;r den Wahrheitswert, ob der Beitrag bearbeitet wurde.
	 * @param bearbeitet Wahrheitswert, ob der Beitrag bearbeitet wurde
	 */
	public void setBearbeitet(boolean bearbeitet) {
		this.bearbeitet = bearbeitet;
	}

	/** 
	 * Getter f&uuml;r den Ort (Gruppe / Veranstaltung), an dem der Beitrag gepostet wurde.
	 * @return Den Ort, an dem der Beitrag gepostet wurde
	 */
	public String getOrtLink() {
		return ortLink;
	}

	/**
	 * Setter f&uuml;r den Ort (Gruppe / Veranstaltung), an dem der Beitrag gepostet wurde.
	 * @param ortLink Der Ort, an dem der Beitrag gepostet wurde
	 */
	public void setOrtLink(String ortLink) {
		this.ortLink = ortLink;
	}

}
