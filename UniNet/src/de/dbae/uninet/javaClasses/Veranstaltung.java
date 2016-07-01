package de.dbae.uninet.javaClasses;

/**
 * Diese Klasse stellt alle wichtigen Informationen einer Veranstaltung zur Verf&&uuml;gung.<br>
 * @author Christian Ackermann
 */
public class Veranstaltung {

	/**
	 * Die VeranstaltungsID.
	 */
	private int id;
	
	/**
	 * Der Veranstaltungsname.
	 */
	private String name;
	
	/**
	 * Der Name des Dozenten.
	 */
	private String dozent;
	
	/**
	 * Das empfohlene Semester (String, da auch z.B. 3-4 m&ouml;glich ist).
	 */
	private String semester;
	
	/**
	 * Beschreibung der Veranstaltung.
	 */
	private String beschreibung;
	
	/**
	 * Sonstige Informationen.
	 */
	private String sonstiges;
	
	/**
	 * Erstellt eine Veranstaltung f&uuml;r die linke Spalte.
	 * @param id Die VeranstaltungsID
	 * @param name Der Veranstaltungsname
	 */
	public Veranstaltung(int id, String name) {
		this.setId(id);
		this.setName(name);
	}
	
	/**
	 * Erstellt eine Veranstaltung f&uuml;r den Mittelblock mit allen Informationen.
	 * @param id Die VeranstaltungsID
	 * @param name Der Veranstaltungsname
	 * @param dozent Der Name des Dozenten
	 * @param semester Das empfohlene Semester
	 * @param beschreibung Die Beschreibung der Veranstaltung
	 * @param sonstiges Sonstige Informationen
	 */
	public Veranstaltung(int id, String name, String dozent, String semester, String beschreibung, String sonstiges) {
		this.setId(id);
		this.setName(name);
		this.setDozent(dozent);
		this.setSemester(semester);
		this.setBeschreibung(beschreibung);
		this.setSonstiges(sonstiges);
	}
	
	/**
	 * Erstellt eine Veranstaltung f&uuml;r den Mittelblock mit allen Informationen.
	 * @param id Die VeranstaltungsID
	 * @param name Der Veranstaltungsname
	 * @param dozent Der Name des Dozenten
	 * @param semester Das empfohlene Semester
	 * @param beschreibung Die Beschreibung der Veranstaltung
	 */
	public Veranstaltung(int id, String name, String dozent, String semester, String beschreibung) {
		this.setId(id);
		this.setName(name);
		this.setDozent(dozent);
		this.setSemester(semester);
		this.setBeschreibung(beschreibung);
	}

	
	/**
	 * Getter f&uuml;r die VeranstaltungsID.
	 * @return Die VeranstaltungsID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter f&uuml;r die VeranstaltungsID.
	 * @param id Die VeranstaltungsID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter f&uuml;r den Veranstaltungsnamen.
	 * @return Der Veranstaltungsname
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter f&uuml;r den Veranstaltungsnamen
	 * @param name Der Veranstaltungsname
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter f&uuml;r den Namen des Dozenten.
	 * @return Der Name des Dozenten
	 */
	public String getDozent() {
		return dozent;
	}

	/**
	 * Setter f&uuml;r den Namen des Dozenten.
	 * @param dozent Der Name des Dozenten
	 */
	public void setDozent(String dozent) {
		this.dozent = dozent;
	}

	/**
	 * Getter f&uuml;r das empfohlene Semester.
	 * @return Das Semester
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * Setter f&uuml;r das empfohlene Semester.
	 * @param semester Das Semester
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}

	/** 
	 * Getter f&uuml;r die Beschreibung der Veranstaltung.
	 * @return Die Beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * Setter f&uuml;r die Beschreibung der Veranstaltung.
	 * @param beschreibung Die Beschreibung
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * Getter f&uuml;r sonstige Informationen.
	 * @return Sonstige Informationen
	 */
	public String getSonstiges() {
		return sonstiges;
	}

	/** 
	 * Setter f&uuml;r sonstige Informationen.
	 * @param sonstiges Sonstige Informationen
	 */
	public void setSonstiges(String sonstiges) {
		this.sonstiges = sonstiges;
	}
	
}
