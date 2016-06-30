package de.dbae.uninet.sqlClasses;

public class SuchergebnisseSql {
	public SuchergebnisseSql() {
		
	}
	
	// SQL Methoden
	
	public String getFreundesanfragen() {
		return "SELECT DISTINCT benachrichtigung, datum FROM (SELECT * FROM Benachrichtigungen WHERE art = 1 AND gelesen = false AND userid=? ORDER BY benachrichtigungid) as temp;";
	}
}


