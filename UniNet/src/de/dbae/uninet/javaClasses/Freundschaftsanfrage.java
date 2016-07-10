package de.dbae.uninet.javaClasses;

import java.util.Date;

/**
 * Diese Klasse stellt alle wichtigen Informationen einer Freundschaftsanfrage zur Verf&&uuml;gung.<br>
 * @author Marvin Wolf
 */
public class Freundschaftsanfrage {
	private int userID;
	private int freundID;
	private String freundName;
	private String nachricht;
	private Date datum;
	
	// C'TOR
	public Freundschaftsanfrage(String nachricht, int userID, int freundID, String freundName,Date datum) {
		this.nachricht = nachricht;
		this.userID = userID;
		this.freundID = freundID;
		this.freundName = freundName;
		this.datum = datum;
	}

	// GETTER / SETTER
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getFreundID() {
		return freundID;
	}

	public void setFreundID(int freundID) {
		this.freundID = freundID;
	}

	public String getFreundName() {
		return freundName;
	}

	public void setFreundName(String freundName) {
		this.freundName = freundName;
	}

	public String getNachricht() {
		return nachricht;
	}


	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}
}
