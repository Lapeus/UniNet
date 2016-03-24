package de.dbae.uninet.sqlClasses;

public class VeranstaltungenSql {

	public String getInfos() {
		String sql = "SELECT Name, Semester, Dozent, Beschreibung, Sonstiges FROM veranstaltungen WHERE veranstaltungsID = ?";
		return sql;
	}
	
	public String getBeitraege() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit "
				+ "FROM veranstaltungsbeitraege INNER JOIN beitragsView USING (beitragsID) WHERE veranstaltungsID = ? ORDER BY beitragsID DESC";
		return sql;
	}
	
	public String getMitglieder() {
		String sql = "SELECT Vorname, Nachname, StudentID "
				+ "FROM veranstaltungsmitglieder INNER JOIN nutzer ON studentID = userID WHERE veranstaltungsID = ? ORDER BY ";
		return sql;
	}
	
	public String getVeranstaltungen() {
		String sql = "SELECT veranstaltungsID, Name "
				+ "FROM veranstaltungsmitglieder INNER JOIN veranstaltungen USING (veranstaltungsID) WHERE studentID = ?"
				+ "ORDER BY Name";
		return sql;
	}
	
	public String getAlleVeranstaltungen() {
		String sql = "SELECT DISTINCT veranstaltungsID, Name "
				+ "FROM veranstaltungsmitglieder INNER JOIN veranstaltungen USING (veranstaltungsID) INNER JOIN studenten USING (uniID) "
				+ "WHERE studenten.studentID = ? ORDER BY Name";
		return sql;
	}
	
	public String getEinschreiben() {
		String sql = "INSERT INTO veranstaltungsmitglieder VALUES (?, ?)";
		return sql;
	}
	
	public String getAusschreiben() {
		String sql = "DELETE FROM veranstaltungsmitglieder WHERE veranstaltungsID = ? AND studentID = ?";
		return sql;
	}
	
	
}
