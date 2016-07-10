package de.dbae.uninet.sqlClasses;

public class BenachrichtigungenSql {
	
	public BenachrichtigungenSql() {
		
	}
	
	public String getFreundesanfragenSql() {
		return "SELECT DISTINCT benachrichtigung, freundid, datum FROM (SELECT * FROM Benachrichtigungen WHERE art = 1 AND gelesen = false AND userid=? ORDER BY benachrichtigungid) as temp;";
	}
	
	public String bestaetigeFreundschaftSql() {
		return "INSERT INTO freunde (freund1, freund2) VALUES (? , ?);";
	}
	
	public String lehneFreunschaftAbSql() {
		return "DELETE FROM benachrichtigungen WHERE (userid = ? AND freundID = ?) OR (userID = ? AND freundID = ?)  AND art = 1;";
	}
	
	public String getNameZuID() {
		return "SELECT Vorname, Nachname FROM Nutzer WHERE UserID = ?";
	}
	
	public String  getBenachrichtigungSql() {
		return "SELECT benachrichtigung, datum FROM Benachrichtigungen WHERE art=0 AND userid=? ORDER BY benachrichtigungid;";
	}
	
	public String loescheBenachrictigungSql() {
		return "UPDATE benachrichtigungen SET gelesen = true WHERE art = 0 AND userid = ?;";
	}
}
