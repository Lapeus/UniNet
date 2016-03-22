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
				+ "FROM veranstaltungsmitglieder INNER JOIN nutzer ON studentID = userID WHERE veranstaltungsID = ?";
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
				+ "FROM veranstaltungsmitglieder INNER JOIN veranstaltungen USING (veranstaltungsID)"
				+ "ORDER BY Name";
		return sql;
	}
	
	
}
