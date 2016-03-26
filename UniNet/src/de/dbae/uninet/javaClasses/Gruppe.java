package de.dbae.uninet.javaClasses;

public class Gruppe {

	private int id;
	private String name;
	private String beschreibung;
	private String gruendung;
	private String admin;
	private int adminID;
	
	public Gruppe(int id, String name) {
		this.setId(id);
		this.setName(name);
	}
	
	public Gruppe(int id, String name, String beschreibung, String gruendung, String admin, int adminID) {
		this.setId(id);
		this.setName(name);
		this.setBeschreibung(beschreibung);
		this.setGruendung(gruendung);
		this.setAdmin(admin);
		this.setAdminID(adminID);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getGruendung() {
		return gruendung;
	}

	public void setGruendung(String gruendung) {
		this.gruendung = gruendung;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public int getAdminID() {
		return adminID;
	}

	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
}
