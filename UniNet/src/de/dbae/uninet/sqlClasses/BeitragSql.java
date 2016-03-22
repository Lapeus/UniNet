package de.dbae.uninet.sqlClasses;

public class BeitragSql {

	public BeitragSql() {
	}
	
	public String getLike() {
		String sql = "INSERT INTO beitraglikes VALUES (?, ?)";
		return sql;
	}
	
	public String getEntferneLike() {
		String sql = "DELETE FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
		return sql;
	}
	
	public String getKommentar() {
		String sql = "INSERT INTO kommentare (beitragsID, kommentar, verfasserID, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
		return sql;
	}
	 
	public String getBeitrag() {
		String sql = "SELECT VerfasserID, Vorname, Nachname, Nachricht, AnzahlLikes, AnzahlKommentare, Datum, Uhrzeit, Sichtbarkeit FROM beitragsView WHERE beitragsID = ?";
		return sql;
	}
	
	public String getLikeAufBeitragSql() {
		String sql = "SELECT COUNT(beitragsID) FROM beitraglikes WHERE beitragsID = ? AND studentID = ?";
		return sql;
	}
	
	public String getLoeschenSql() {
		String sql = "DELETE FROM beitraege WHERE beitragsID = ?";
		return sql;
	}
	
	public String getKommentare() {
		String sql = "SELECT verfasserID, kommentarID, vorname, nachname, kommentar, datum, uhrzeit FROM kommentare INNER JOIN Nutzer ON verfasserID = userID WHERE beitragsID = ?";
		return sql;
	}
	
	public String getLoescheKommentar() {
		String sql = "DELETE FROM kommentare WHERE kommentarID = ?";
		return sql;
	}
	
	public String getUnterkommentare() {
		String sql = "SELECT verfasserID, kommentarID, vorname, nachname, kommentar, datum, uhrzeit FROM unterkommentare INNER JOIN Nutzer ON verfasserID = userID WHERE antwortAuf = ?";
		return sql;
	}
	
	public String getLoescheUnterkommentar() {
		String sql = "DELETE FROM unterkommentare WHERE kommentarID = ?";
		return sql;
	}
	
	public String getInsertUnterkommentar() {
		String sql = "INSERT INTO unterkommentare (antwortAuf, kommentar, verfasserID, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
		return sql;
	}
	
	public String getKommentareZuUnterkommentare() {
		String sql = "SELECT verfasserID, kommentarID, vorname, nachname, kommentar, datum, uhrzeit FROM kommentareZuUnterkommentare INNER JOIN Nutzer ON verfasserID = userID WHERE antwortAuf = ?";
		return sql;
	}
	
	public String getLoescheKommentarZuUnterkommentar() {
		String sql = "DELETE FROM kommentareZuUnterkommentare WHERE kommentarID = ?";
		return sql;
	}
	
	public String getInsertKommentarZuUnterkommentar() {
		String sql = "INSERT INTO kommentareZuUnterkommentare (antwortAuf, kommentar, verfasserID, datum, uhrzeit) VALUES (?, ?, ?, ?, ?)";
		return sql;
	}
	
	public String getBeitragMelden() {
		String sql = "INSERT INTO beitragMeldungen VALUES (?, ?)";
		return sql;
	}
	
	public String getOrtNameSql() {
		String sql = "SELECT name FROM beitragsOrt WHERE beitragsID = ?";
		return sql;
	}
	
	
}
