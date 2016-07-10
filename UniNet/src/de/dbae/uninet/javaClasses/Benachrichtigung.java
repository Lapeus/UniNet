package de.dbae.uninet.javaClasses;

import java.util.Date;

public class Benachrichtigung {
	private String html;
	private Date date;
	
	public Benachrichtigung(String html, Date date) {
		this.html = html;
		this.date = date;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String sHtml) {
		this.html = sHtml;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
