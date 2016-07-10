package de.dbae.uninet.javaClasses;

import java.util.Date;

/**
 * Diese Klasse stellt alle wichtigen Informationen einem gesuchten Nutzer zur Verf&&uuml;gung.<br>
 * @author Marvin Wolf
 */
public class GesuchterNutzer {

	/**
	 * Der Vorname des gesuchten Nutzers.
	 */
	private String vorname;
	
	/**
	 * Der Nachname des gesuchten Nutzers.
	 */
	private String nachname;
	
	/**
	 * Die UserID des gesuchten Nutzers.
	 */
	private int userID;
	
	/**
	 * Die UserID des gesuchten Nutzers.
	 */
	private Date geburtsdatum;
	
	/**
	 * Der Online-Status des gesuchten Nutzers.<br>
	 * Diese ist im Suchmodus relevant.
	 * Falls nicht gesetzt immer false.
	 */
	private boolean online = false;
	
	/**
	 * Wenn Neutzer geuscht wird. Ist er mit dem suchenden Befreundet?<br>
	 * Dieser ist nur im Suchmodus relevant.
	 * Falls nicht gesetzt immer false.
	 */
	private boolean istFreund = false;
	
	/**
	 * Erstellt einen gesuchten Nutzer und setzt seinen Online-Status.
	 * @param vorname Der Vorname
	 * @param nachname Der Nachname
	 * @param userID Die UserID
	 * @param online Der Online-Status
	 */
	public GesuchterNutzer(String vorname, String nachname, int userID, Date geburtsdatum ,boolean online, boolean istFreund) {
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setUserID(userID);
		this.setGeburtsdatum(geburtsdatum);
		this.setOnline(online);
		this.setFreund(istFreund);
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
	 * Getter f&uuml;r das Geburtsdatum.
	 * @return Das Geburtsdatum
	 */
	public Date getGeburtsdatum() {
		return geburtsdatum;
	}
	
	/**
	 * Getter f&uuml;r das Geburtsdatum.
	 * @param geburtsdatum Das Geburtsdatum
	 */
	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
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
	 * Getter f&uuml;r den Freundschaftstatus.
	 * @return Der Freundschaftsstatus
	 */
	public boolean isFreund() {
		return istFreund;
	}

	/** 
	 * Setter f&uuml;r den Freundschaftstatus.
	 * @param istFreund Der Freundschaftsstatus
	 */
	public void setFreund(boolean istFreund) {
		this.istFreund = istFreund;
	}
	
}
