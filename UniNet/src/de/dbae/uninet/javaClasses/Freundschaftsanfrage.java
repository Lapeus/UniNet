package de.dbae.uninet.javaClasses;

import java.util.Date;

public class Freundschaftsanfrage {
	private String nachricht;
	private Date datum;
	
	// C'TOR
	public Freundschaftsanfrage(String nachricht, Date datum) {
		this.nachricht = nachricht;
		this.datum = datum;
	}

	// GETTER / SETTER
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
