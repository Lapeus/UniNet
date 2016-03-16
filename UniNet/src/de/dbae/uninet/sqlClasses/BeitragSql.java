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
}
