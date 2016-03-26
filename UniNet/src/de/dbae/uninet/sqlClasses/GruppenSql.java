package de.dbae.uninet.sqlClasses;

public class GruppenSql {

	public String getGruppeGruenden() {
		String sql = "INSERT INTO gruppen(name, beschreibung, gruendung, adminID) VALUES (?, ?, ?, ?)";
		return sql;
	}
	
	public String getGruppeLoeschen() {
		String sql = "DELETE FROM gruppen WHERE gruppenID = ?";
		return sql;
	}
	
	public String getStudentHinzufuegen() {
		String sql = "INSERT INTO gruppenmitglieder VALUES (?, ?)";
		return sql;
	}
	
	public String getBeschreibungAendern() {
		String sql = "UPDATE gruppen SET beschreibung = ? WHERE gruppenID = ?";
		return sql;
	}
	
	public String getBeitraege() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit "
				+ "FROM gruppenbeitraege INNER JOIN beitragsView USING (beitragsID) WHERE gruppenID = ? ORDER BY beitragsID DESC";
		return sql;
	}
	public String getGruppen() {
		String sql = "SELECT Name, gruppenID FROM gruppenmitglieder INNER JOIN studenten USING (studentID) INNER JOIN gruppen USING (gruppenID) WHERE studentID = ?";
		return sql;
	}
	
	public String getInfos() {
		String sql = "SELECT Name, Beschreibung, Gruendung, Vorname, Nachname, AdminID FROM Gruppen INNER JOIN Nutzer ON (adminID = userID) WHERE gruppenID = ?";
		return sql;
	}
	
	public String getVerlassen() {
		String sql = "DELETE FROM gruppenmitglieder WHERE gruppenID = ? AND studentID = ?";
		return sql;
	}
	
	public String getMitglieder() {
		String sql = "SELECT Vorname, Nachname, StudentID "
				+ "FROM gruppenmitglieder INNER JOIN nutzer ON studentID = userID WHERE gruppenID = ? ORDER BY ";
		return sql;
	}
	
	public String getMitgliederZufuegen() {
		String sql = "INSERT INTO gruppenmitglieder VALUES (?, ?)";
		return sql;
	}
	
	public String getNeueGruppenID() {
		String sql = "SELECT MAX(gruppenID) FROM gruppen";
		return sql;
	}
	
	public String getFreunde() {
		String sql= "SELECT DISTINCT freund AS freundID, Vorname, Nachname FROM gruppenmitglieder "
				+ "INNER JOIN freundeview ON studentID = nutzer "
				+ "INNER JOIN nutzer ON freund = userID WHERE gruppenID = ?";
		return sql;
	}
}
