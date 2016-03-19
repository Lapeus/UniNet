package de.dbae.uninet.sqlClasses;

public class AnmeldeSql {
	// Con
	public AnmeldeSql() {
	}
	
	// Methods
	public String getRegistrierungNutzerSql() {
		String sql = "INSERT INTO Nutzer (anrede, vorname, nachname, email, passwort, salt)  VALUES (?,?,?,?,?,?)";
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
		String sql = "SELECT passwort, salt FROM nutzer WHERE email=?";
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
	
	public String getOnlineUpdate() {
		String sql = "UPDATE Studenten SET Online = TRUE WHERE StudentID = ?";
		return sql;
	}
	
	public String getOfflineUpdate() {
		String sql = "UPDATE Studenten SET Online = FALSE WHERE StudentID = ?";
		return sql;
	}
	
	public String getStudentenIDs() {
		String sql = "SELECT StudentID FROM Studenten";
		return sql;
	}
	
}
