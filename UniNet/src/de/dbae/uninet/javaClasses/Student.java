package de.dbae.uninet.javaClasses;

/**
 * Diese Klasse verwaltet die Informationen eines allgemeinen Studenten.<br>
 * Verwendet wird diese Klasse immer dann, wenn ein Name als Link auf dessen Profilseite fungieren soll.<br>
 * Dies passiert zum Beispiel im Chat und bei der Mitgliederauflistung in Gruppen und Veranstaltungen.
 * @author Christian Ackermann
 */
public class Student {

	/**
	 * Der Vorname des Studenten.
	 */
	private String vorname;
	
	/**
	 * Der Nachname des Studenten.
	 */
	private String nachname;
	
	/**
	 * Die UserID des Studenten.
	 */
	private int userID;
	
	/**
	 * Die email des Studenten.
	 */
	private String email;
	
	/**
	 * Der Online-Status des Studenten.<br>
	 * Dieser ist nur im Chat relevant, weshalb er sonst standardm&auml;&szlig;ig auf <b>false</b> steht.
	 */
	private boolean online = false;
	
	/**
	 * Anzahl der Nachrichten, die der User dem aktuellen User geschickt hat,
	 * die dieser noch nicht gelesen hat.<br>
	 * Diese Anzahl wird in der Chatspalte hinter dem Namen angezeigt.
	 */
	private int anzahlUngeleseneNachrichten = 0;
	
	/**
	 * Erstellt einen neuen Studenten.<br>
	 * Hauptkonstruktor dieser Klasse.
	 * @param vorname Der Vorname
	 * @param nachname Der Nachname
	 * @param userID Die UserID
	 */
	public Student(String vorname, String nachname, int userID) {
		this(vorname, nachname, userID, false, 0);
	}
	
	/**
	 * Erstellt einen Studenten und setzt seinen Online-Status.
	 * @param vorname Der Vorname
	 * @param nachname Der Nachname
	 * @param userID Die UserID
	 * @param online Der Online-Status
	 * @param anzahlUngeleseneNachrichten Die Anzahl der Nachrichten, die der aktuelle User noch nicht
	 * gelesen hat
	 */
	public Student(String vorname, String nachname, int userID, boolean online, int anzahlUngeleseneNachrichten) {
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setUserID(userID);
		this.setOnline(online);
		this.setAnzahlUngeleseneNachrichten(anzahlUngeleseneNachrichten);
	}
	
	/**
	 * Erstellt einen Studenten inklusive E-Mail.
	 * @param vorname Der Vorname
	 * @param nachname Der Nachname
	 * @param userID Die UserID 
	 * @param email Die Email
	 */
	public Student (String vorname, String nachname, int userID, String email) {
		this(vorname, nachname, userID, false, 0);
		this.email = email;
	}
	
	/**
	 * Getter f&uuml;r den Vornamen.
	 * @return Der Vorname
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Setter f&uuml;r den Vornamen.
	 * @param vorname Der Vorname
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	 * Getter f&uuml;r den Nachnamen.
	 * @return Der Nachname
	 */
	public String getNachname() {
		return nachname;
	}

	/** 
	 * Setter f&uuml;r den Nachnamen.
	 * @param nachname Der Nachname
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
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
	 * Getter f&uuml;r den Online-Status.
	 * @return Der Online-Status
	 */
	public boolean isOnline() {
		return online;
	}

	/** 
	 * Setter f&uuml;r den Online-Status.
	 * @param online Der Online-Status
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * Gibt die Anzahl der ungelesenen Nachrichten zur&uuml;ck.
	 * @return Die Anzahl der ungelesenen Nachrichten
	 */
	public int getAnzahlUngeleseneNachrichten() {
		return anzahlUngeleseneNachrichten;
	}

	/**
	 * Setzt die Anzahl der ungelesenen Nachrichten.
	 * @param anzahlUngeleseneNachrichten Die Anzahl der ungelesenen Nachrichten
	 */
	public void setAnzahlUngeleseneNachrichten(int anzahlUngeleseneNachrichten) {
		this.anzahlUngeleseneNachrichten = anzahlUngeleseneNachrichten;
	}

	/**
	 * Getter f&uuml;r die E-Mail.
	 * @return Die E-Mail
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter f&uuml;r die E-Mail.
	 * @param email Die E-Mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
