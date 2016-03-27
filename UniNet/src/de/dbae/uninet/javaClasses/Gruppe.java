package de.dbae.uninet.javaClasses;

/**
 * Diese Klasse stellt alle wichtigen Informationen einer Gruppe zur Verf&&uuml;gung.<br>
 * @author Christian Ackermann
 */
public class Gruppe {

	/**
	 * Die GruppenID.
	 */
	private int id;
	
	/**
	 * Der Gruppenname.
	 */
	private String name;
	
	/**
	 * Die Beschreibung der Gruppe.
	 */
	private String beschreibung;
	
	/**
	 * Der Tag der Gr&uuml;ndung.
	 */
	private String gruendung;
	
	/**
	 * Der Name des Gruppenadministrators.
	 */
	private String admin;
	
	/**
	 * Die ID des Gruppenadministrators.
	 */
	private int adminID;
	
	/**
	 * Erstellt eine neue Gruppe f&uuml;r die Gruppen&uuml;bersicht in der linken Spalte.<br>
	 * @param id Die GruppenID
	 * @param name Der Gruppennamme
	 */
	public Gruppe(int id, String name) {
		this.setId(id);
		this.setName(name);
	}
	
	/** 
	 * Erstellt eine neue Gruppe f&uuml;r den Hauptteil der Gruppenjsp.<br>
	 * Hauptkonstruktor dieser Klasse.
	 * @param id Die GruppenID
	 * @param name Der Gruppenname
	 * @param beschreibung Die Beschreibung der Gruppe
	 * @param gruendung Der Tag der Gr&uuml;ndung
	 * @param admin Der Name des Gruppenadministrators
	 * @param adminID Die ID des Gruppenadministrators
	 */
	public Gruppe(int id, String name, String beschreibung, String gruendung, String admin, int adminID) {
		this.setId(id);
		this.setName(name);
		this.setBeschreibung(beschreibung);
		this.setGruendung(gruendung);
		this.setAdmin(admin);
		this.setAdminID(adminID);
	}

	/**
	 * Getter f&uuml;r die GruppenID.
	 * @return Die GruppenID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter f&uuml;r die GruppenID.
	 * @param id Die GruppenID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter f&uuml;r den Gruppennamen.
	 * @return Der Gruppenname
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter f&uuml;r den Gruppennamen.
	 * @param name Der Gruppenname
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter f&uuml;r die Beschreibung der Gruppe.
	 * @return Die Beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * Setter f&uuml;r die Beschreibung der Gruppe.
	 * @param beschreibung Die Beschreibung
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * Getter f&uuml;r den Tag der Gr&uuml;ndung.
	 * @return Der Gr&uuml;ndungstag
	 */
	public String getGruendung() {
		return gruendung;
	}

	/**
	 * Setter f&uuml;r den Tag der Gr&uuml;ndung.
	 * @param gruendung Der Gr&uuml;ndungstag
	 */
	public void setGruendung(String gruendung) {
		this.gruendung = gruendung;
	}

	/**
	 * Getter f&uuml;r den Namen des Gruppenadministrators.
	 * @return Der Name des Gruppenadministrators
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * Setter f&uuml;r den Namen des Gruppenadministrators.
	 * @param admin Der Name des Gruppenadministrators
	 */
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/**
	 * Getter f&uuml;r die ID des Gruppenadministrators.
	 * @return Die ID des Gruppenadministrators
	 */
	public int getAdminID() {
		return adminID;
	}

	/**
	 * Setter f&uuml;r die ID des Gruppenadministrators.
	 * @param adminID Die ID des Gruppenadministrators
	 */
	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
}
