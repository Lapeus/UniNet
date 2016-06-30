package de.dbae.uninet.sqlClasses;

public class AdminSql {
	//Constructor
	public AdminSql() {
	}
	//Methods
	public String getAdminsSql() {
		String sql = "SELECT vorname, nachname, uniname FROM nutzer FULL OUTER JOIN admins ON (nutzer.userid = admins.adminid) FULL OUTER JOIN universitaeten ON (admins.uniid = universitaeten.uniid)WHERE Nutzertyp = 2;";
		return sql;
	}
}
