package de.dbae.uninet.sqlClasses;

public class ProfilSql {

	public ProfilSql() {
	}
	
	public String getInfosSql() {
		String sql = "SELECT Vorname, Nachname, UniName, StudiengangName, Studienbeginn FROM Nutzer INNER JOIN Studenten ON UserID = StudentID INNER JOIN Universitaeten USING (UniID) INNER JOIN Studiengaenge USING (StudiengangID) WHERE StudentID = ?";
		return sql;
	}
	
	public String getAnzahlFreunde() {
		String sql = "SELECT COUNT(Freund) FROM freundeView WHERE Nutzer = ?";
		return sql;
	}
	
	public String getBeitraegeSql() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, BeitragsID FROM beitragsView WHERE VerfasserID = ?";
		return sql;
	}
	
	public String getBeitragAnlegenSql1() {
		String sql = "INSERT INTO beitraege (beitrag, verfasserID, sichtbarkeit) VALUES (?, ?, ?)";
		return sql;
	}
	
	public String getBeitragAnlegenSql2() {
		String sql = "SELECT MAX(beitragsID) FROM beitraege";
		return sql;
	}
	
	public String getBeitragAnlegenSql3() {
		String sql = "INSERT INTO chronikbeitraege VALUES (?)";
		return sql;
	}
	
	
	
	
}
