package de.dbae.uninet.sqlClasses;

public class AnmeldeSql {
	// Con
	public AnmeldeSql() {
	}
	
	// Methods
	public String getRegistrierungNutzerSql() {
		String sql = "INSERT INTO Nutzer (anrede, vorname, nachname, email, passwort)  VALUES (?,?,?,?,?)";
		return sql; 
	}
	
	public String getNutzerId() {
		String sql = "SELECT userid FROM nutzer WHERE email=?";
	    return sql;
	}
	
	public String getUniId() {
		String sql = "SELECT uniid FROM universitaeten WHERE uniname=?";
		return sql;
	}
	
	public String getStudiengangId() {
		String sql = "SELECT studiengangid FROM studiengaenge WHERE studiengangname=?";
		return sql;
	}
	
	public String getRegistrierungStudentSql () {
		String sql = "INSERT INTO studenten (studentid, uniid, studiengangid, online) VALUES (?,?,?,"+ true +")";
		return sql;
	}
	
	public String ueberpruefeAnmeldedaten() {
		String sql = "SELECT email,passwort FROM nutzer WHERE email=? AND passwort=?";
		return sql;
	}
	
	public String getUniList() {
		String sql = "SELECT uniname FROM universitaeten"; 	
		return sql;
	}
	
	public String getStudiengaenge() {
		String sql = "SELECT studiengangname FROM studiengaenge NATURAL JOIN Universitaeten WHERE uniname =?";
		return sql;
	}
}
