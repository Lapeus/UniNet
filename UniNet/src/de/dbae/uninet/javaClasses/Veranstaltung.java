package de.dbae.uninet.javaClasses;

public class Veranstaltung {

	private int id;
	private String name;
	private String dozent;
	private String semester;
	private String beschreibung;
	private String sonstiges;
	
	public Veranstaltung(int id, String name) {
		this.setId(id);
		this.setName(name);
	}
	
	public Veranstaltung(int id, String name, String dozent, String semester, String beschreibung, String sonstiges) {
		this.setId(id);
		this.setName(name);
		this.setDozent(dozent);
		this.setSemester(semester);
		this.setBeschreibung(beschreibung);
		this.setSonstiges(sonstiges);
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

	public String getDozent() {
		return dozent;
	}

	public void setDozent(String dozent) {
		this.dozent = dozent;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getSonstiges() {
		return sonstiges;
	}

	public void setSonstiges(String sonstiges) {
		this.sonstiges = sonstiges;
	}
	
}
