package de.dbae.uninet.sqlClasses;

public class ProfilSql {

	public ProfilSql() {
	}
	
	public String getInfosSql() {
		String sql = "SELECT Vorname, Nachname, UniName, StudiengangName, Studienbeginn FROM Nutzer INNER JOIN Studenten ON UserID = StudentID INNER JOIN Universitaeten USING (UniID) INNER JOIN Studiengaenge USING (StudiengangID) WHERE StudentID = ?";
		return sql;
	}
	
	public String getBeitraegeSql() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare FROM beitragsView WHERE VerfasserID = ?";
		return sql;
	}
}
