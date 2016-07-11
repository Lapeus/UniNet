package de.dbae.uninet.dbConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Diese Klasse verwaltet die Datenbank-Verbindungen.
 * Damit nicht mehr Verbindungen ge&ouml;ffnet als geschlossen werden, bekommt jedes Servlet nur eine Instanz 
 * dieser Klasse &uuml;bergeben, welche dann die Verbindungen verwaltet.
 * @author Christian Ackermann
 *
 */
public class DBConnection {

	/**
	 * Die Datenbank-Verbindung.
	 */
	private Connection con = null;
	
	/**
	 * Die Anzahl der momentan ge&ouml;ffneten Verbindungen.
	 */
	public static int anzahlVerbindungen = 0;
	
	/**
	 * Der DB-Driver.
	 */
	static final String DRIVER = "org.postgresql.Driver";
	
	/*static final String DB_SERVER = "abgabe-dbae.iis.uni-hildesheim.de:5432";
	static final String DB_NAME = "db_ackerwolf";
	static final String PASSWORD = "sicher123";
	static final String USER = "group_ackerwolf";*/
	
	// Eure lokalen Daten hier einsetzen und obige auskommentieren
	
	/**
	 * Der DB-Server.
	 */
	static final String DB_SERVER = "localhost:5432";
	
	/**
	 * Der DB-Name.
	 */
	static final String DB_NAME = "postgres";
	
	/**
	 * Das DB-Passwort.
	 */
	static final String PASSWORD = "sicher123";
	
	/**
	 * Der DB-Benutzername.
	 */
	static final String USER = "postgres";

	/**
	 * Die DB-URL
	 */
	static final String URL = "jdbc:postgresql://"+DB_SERVER+ "/"+DB_NAME;
	
	
	/**
	 * Erzeugt eine neue DBConnection.
	 * Initialisiert die DB-Verbindung oder gibt eine entsprechende Fehlermeldung aus.
	 */
	public DBConnection () {
		try {
			Class.forName(DRIVER);
			// Oeffnet die Verbindung
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber konnte nicht geladen werden! " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Ein SQL-Fehler ist aufgetreten! " + e.getMessage());
		}
	}
	
	/**
	 * Gibt die Verbindung an das aufrufende Servlet zur&uuml;ck.
	 * @return Die Verbindung
	 */
	public Connection getCon() {
		System.out.println("Die Verbindung wurde geöffnet(" + ++anzahlVerbindungen + ")");
		return con;
	}
	
	/**
	 * Schlie&szlig;t die Datenbank-Verbindung, sofern sie existiert hat.
	 */
	public void close() {
		try {
			// Wenn eine geoeffnet wurde
			if (con != null) {
				// Wird sie geschlossen
				con.close();
				System.out.println("Die Verbindung wurde erfolgreich beendet! (" + --anzahlVerbindungen + ")");
			}
		} catch (SQLException e) {
			System.err.println("SQL-Fehler");
			e.printStackTrace();
		}
	}

}