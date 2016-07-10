package de.dbae.uninet.javaClasses;

import java.util.Date;

/**
 * Diese Klasse stellt alle wichtigen Informationen einer Nachricht zur Verf&&uuml;gung.<br>
 * @author Marvin Wolf
 */
public class Nachricht {
	private int senderId;
	private String name;
	private Date date;
	private String nachrichtenText;
	
	public Nachricht(int senderId, String name, String nachrichtenText, Date date) {
		this.senderId = senderId;
		this.name = name;
		this.nachrichtenText = nachrichtenText;
		this.date = date;
	}
	
	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNachrichtenText() {
		return nachrichtenText;
	}

	public void setNachrichtenText(String nachrichtenText) {
		this.nachrichtenText = nachrichtenText;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
