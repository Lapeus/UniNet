package de.dbae.uninet.sqlClasses;

public class ProfilSql {

	public ProfilSql() {
	}
	
	public String getInfosSql() {
		String sql = "SELECT Vorname, Nachname, StudiengangName, Studienbeginn, Geburtstag, Wohnort, Hobbys, Interessen, UeberMich, Email FROM Nutzer INNER JOIN Studenten ON UserID = StudentID INNER JOIN Studiengaenge USING (StudiengangID) WHERE StudentID = ?";
		return sql;
	}
	
	public String getInfosBSql() {
		String sql = "SELECT Vorname, Nachname, Geburtstag, Wohnort, Hobbys, Interessen, UeberMich FROM Nutzer INNER JOIN Studenten ON UserID = StudentID WHERE StudentID = ?";
		return sql;
	}
	
	public String getAendereNamenSql() {
		String sql = "UPDATE Nutzer SET vorname = ?, nachname = ? WHERE userID = ?";
		return sql;
	}
	
	public String getAendereInfosSql() {
		String sql = "UPDATE Studenten SET geburtstag = ?, wohnort = ?, hobbys = ?, interessen = ?, ueberMich = ? WHERE studentID = ?";
		return sql;
	}
	public String getAnzahlFreunde() {
		String sql = "SELECT COUNT(Freund) FROM freundeView WHERE Nutzer = ?";
		return sql;
	}
	
	public String getBeitraegeSql() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID, Datum, Uhrzeit, Sichtbarkeit FROM beitragsView INNER JOIN chronikbeitraege USING (beitragsID) WHERE VerfasserID = ?";
		return sql;
	}
	
	public String getLikeAufBeitragSql() {
		String sql = "SELECT COUNT(beitragsID) FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
		return sql;
	}
	
}
