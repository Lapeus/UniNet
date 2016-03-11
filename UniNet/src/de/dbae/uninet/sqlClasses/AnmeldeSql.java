package de.dbae.uninet.sqlClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.dbae.uninet.dbConnections.DBConnection;

public class AnmeldeSql {
	// Var
	Connection con;
	
	// Con
	public AnmeldeSql(Connection con) {
		this.con = con;
	}
	
	// Methods
	public void registrierungsSql(boolean gender, String vorname, String nachname, String email, String password1) {
		String sql = "INSERT INTO Nutzer (anrede, vorname, nachname, passwort, email)  VALUES ('"+ gender +"','"+ vorname +"','"+ nachname +"','"+ email +"','"+ password1 +"')";
		PreparedStatement pStmt;
		try {
			pStmt = con.prepareStatement(sql);
			pStmt.execute();
		} catch (SQLException e) {
			System.out.println("E-Mail schon registriert");
		} 
	}
	public String getPreparedAnmeldeSql() {
		return "";
	}
	
	public List<String> getUniList() {
		List<String> unis = new ArrayList<>();
		con = new DBConnection().getCon();
		String sql = "SELECT uniname FROM universitaeten";
		try {
			PreparedStatement pStmt = con.prepareStatement(sql);
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					unis.add(result.getString(i));
				} 
			}
		} catch (SQLException e) {
			System.out.println("SQL Fehler - AnmeldeSQL.getUniList");
		} 	
		return unis;
	}
}
