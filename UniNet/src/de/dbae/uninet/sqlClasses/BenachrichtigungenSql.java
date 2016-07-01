package de.dbae.uninet.sqlClasses;

public class BenachrichtigungenSql {
	
	public BenachrichtigungenSql() {
		
	}
	
	public String getFreundesanfragen() {
		return "SELECT DISTINCT benachrichtigung, datum FROM (SELECT * FROM Benachrichtigungen WHERE art = 1 AND gelesen = false AND userid=? ORDER BY benachrichtigungid) as temp;";
	}
}
