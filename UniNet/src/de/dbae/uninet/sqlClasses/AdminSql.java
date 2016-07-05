package de.dbae.uninet.sqlClasses;

public class AdminSql {
	//Constructor
	public AdminSql() {
	}
	//Methods
	public String getAdminsSql() {
		String sql = "SELECT vorname, nachname, uniname, userid FROM nutzer FULL OUTER JOIN admins ON (nutzer.userid = admins.adminid) FULL OUTER JOIN universitaeten ON (admins.uniid = universitaeten.uniid)WHERE Nutzertyp = 2;";
		return sql;
	}
	public String getAdminLoeschen1Sql() {
		String sql = "DELETE FROM admins WHERE adminid =?;";
		return sql;
	}
	public String getAdminLoeschen2Sql() {
		String sql = "DELETE FROM nutzer WHERE userid =?;";
		return sql;
	}
	public String getUniversitaetenSql() {
		String sql = "SELECT uniname FROM universitaeten WHERE uniname =?;";
	return sql;
	}
	public String getUniAnzeigeSql() {
		String sql = "SELECT uniid, uniname, unistandort FROM universitaeten;";
		return sql;
	}
	public String getUniLoeschenSql() {
		String sql ="DELETE FROM universitaeten WHERE uniid =?;";
	return sql;
	}
	public String getUniAnlegenSql() {
		String sql = "INSERT INTO universitaeten (uniname, unistandort) VALUES (?,?);";
	return sql;
	}
}
