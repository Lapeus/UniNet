package de.dbae.uninet.sqlClasses;

public class AnmeldeSql {
	// Con
	public AnmeldeSql() {
	}
	
	// Methods
	public String getRegistrierungsSql(boolean gender, String vorname, String nachname, String email, String password1) {
		String sql = "INSERT INTO Nutzer (anrede, vorname, nachname, email, passwort)  VALUES ('"+ gender +"','"+ vorname +"','"+ nachname +"','"+ email +"','"+ password1 +"')";
		return sql; 
	}
	public String getPreparedAnmeldeSql() {
		return "";
	}
	
	public String getUniList() {
		String sql = "SELECT uniname FROM universitaeten"; 	
		return sql;
	}
}
