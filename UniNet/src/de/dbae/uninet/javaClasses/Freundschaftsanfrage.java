package de.dbae.uninet.javaClasses;

public class Freundschaftsanfrage {
	private int userId;
	private int freundId;
	private String nachricht;
	
	// C'TOR
	public Freundschaftsanfrage() {
		
	}
	
	public Freundschaftsanfrage(int userId, int freundId, String nachricht) {
		this.userId    = userId;
		this.freundId  = freundId;
		this.nachricht = nachricht;
	}

	// GETTER / SETTER
	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getFreundId() {
		return freundId;
	}


	public void setFreundId(int freundId) {
		this.freundId = freundId;
	}


	public String getNachricht() {
		return nachricht;
	}


	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}
	
	
}
