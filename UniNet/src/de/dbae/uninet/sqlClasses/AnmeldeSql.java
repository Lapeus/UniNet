package de.dbae.uninet.sqlClasses;

public class AnmeldeSql {
	// Con
	public AnmeldeSql() {
	}
	
	// Methods
	public String getRegistrierungNutzerSql(boolean gender, String vorname, String nachname, String email, String password1) {
		String sql = "INSERT INTO Nutzer (anrede, vorname, nachname, email, passwort)  VALUES ('"+ gender +"','"+ vorname +"','"+ nachname +"','"+ email +"','"+ password1 +"')";
		return sql; 
	}
	
	public String getNutzerId(String email) {
		String sql = "SELECT userid FROM nutzer WHERE email='"+ email +"'";
		System.out.println(sql);
	    return sql;
	}
	
	public String getUniId(String uniname) {
		String sql = "SELECT uniid FROM universitaeten WHERE uniname='"+ uniname +"'";
		System.out.println(sql);
		return sql;
	}
	
	public String getStudiengangId(String studiengangname) {
		String sql = "SELECT studiengangid FROM studiengaenge WHERE studiengangname='"+ studiengangname +"'";
		System.out.println(sql);
		return sql;
	}
	
	public String getRegistrierungStudentSql (String userid, String uniid, String studiengangid) {
		String sql = "INSERT INTO studenten (studentid, uniid, studiengangid, online) VALUES ("+ userid + ","+ uniid +","+ studiengangid +","+ true +")";
		System.out.println(sql);
		return sql;
	}
	
	public String ueberpruefeAnmeldedaten(String email, String password) {
		String sql = "SELECT email,passwort FROM nutzer WHERE email='"+ email +"' AND passwort='"+ password + "'";
		return sql;
	}
	
	public String getUniList() {
		String sql = "SELECT uniname FROM universitaeten"; 	
		return sql;
	}
	
	public String getStudiengaenge(String uni) {
		String sql = "SELECT studiengangname FROM ((studiengaengeunis NATURAL JOIN universitaeten) NATURAL JOIN studiengaenge) WHERE uniname='"+uni+"'";
		return sql;
	}
}
