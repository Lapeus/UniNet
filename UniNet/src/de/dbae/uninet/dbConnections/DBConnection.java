package de.dbae.uninet.dbConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private Connection con;
	public static int anzahlVerbindungen = 0;
	static final String DRIVER = "org.postgresql.Driver";
	
	/*static final String DB_SERVER = "abgabe-dbae.iis.uni-hildesheim.de:5432";
	static final String DB_NAME = "db_ackerwolf";
	static final String PASSWORD = "sicher123";
	static final String USER = "group_ackerwolf";*/
	
	// Eure lokalen Daten hier einsetzen und obige auskommentieren
	
	static final String DB_SERVER = "localhost:5432";
	static final String DB_NAME = "postgres";
	static final String PASSWORD = "asdfghjkl";
	static final String USER = "postgres";

	static final String URL = "jdbc:postgresql://"+DB_SERVER+ "/"+DB_NAME;
	
	public DBConnection () {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber konnte nicht geladen werden! " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Ein SQL-Fehler ist aufgetreten! " + e.getMessage());
		}
	}
	
	public Connection getCon() {
		System.out.println("Die Verbindung wurde geöffnet(" + ++anzahlVerbindungen + ")");
		return con;
	}
	
	public void close() {
		try {
			con.close();
			System.out.println("Die Verbindung wurde erfolgreich beendet! (" + --anzahlVerbindungen + ")");
		} catch (SQLException e) {
			System.err.println("SQL-Fehler");
			e.printStackTrace();
		}
	}

}