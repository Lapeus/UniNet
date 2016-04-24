package de.dbae.uninet.javaClasses;

import java.util.Date;

public class Nachricht {
	private String name;
	private Date date;
	private String nachrichtenText;
	
	public Nachricht(String name, String nachrichtenText, Date date) {
		this.name = name;
		this.nachrichtenText = nachrichtenText;
		this.date = date;
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
