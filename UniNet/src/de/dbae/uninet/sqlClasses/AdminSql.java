package de.dbae.uninet.sqlClasses;
/**
 * 
 * @author Leon Schaffert
 */
public class AdminSql {
	//Constructor
	public AdminSql() {
	}
	//Methods
	public String getAdminsSql() {
		String sql = "SELECT vorname, nachname, uniname, userid FROM nutzer FULL OUTER JOIN admins ON (nutzer.userid = admins.adminid) FULL OUTER JOIN universitaeten ON (admins.uniid = universitaeten.uniid)WHERE Nutzertyp = 2";
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
		String sql = "SELECT uniid, uniname, unistandort FROM universitaeten";
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
	public String getStudiengangAnzeigeSql() {
		String sql = "SELECT studiengangid, studiengangname FROM studiengaenge WHERE uniid =?";
		return sql;
	}
	public String getStudiengangLoeschenSql() {
		String sql = "DELETE FROM studiengaenge WHERE studiengangid =?;";
		return sql;
	}
	public String getUniidFromAdminsSql() {
		String sql = "SELECT uniid FROM admins WHERE adminid =?;";
		return sql;
	}
	public String getUninameFromUniidSql() {
		String sql = "SELECT uniname FROM universitaeten WHERE uniid = ?;";
		return sql;
	}
	public String getStudiengangSql() {
		String sql = "SELECT studiengangname, uniid FROM studiengaenge WHERE studiengangname =?;";
		return sql;
	}
	public String getStudiengangAnlegenSql() {
		String sql = "INSERT INTO studiengaenge (studiengangname, uniid) VALUES (?,?);";
		return sql;
	}
	public String getVeranstaltungUebersichtSql() {
		String sql = "SELECT veranstaltungsid, name, dozent, semester FROM veranstaltungen WHERE uniid =?";
		return sql;
	}
	public String getVeranstaltungSql() {
		String sql ="SELECT name, uniid FROM veranstaltungen WHERE name =?;";
		return sql;
	}
	public String getVeranstaltungAnlegenSql() {
		String sql ="INSERT INTO veranstaltungen (name, uniid, semester, dozent, beschreibung, sonstiges) VALUES (?,?,?,?,?,?);";
		return sql;
	}
	public String getVeranstaltungLoeschenSql() {
		String sql ="DELETE FROM veranstaltungen WHERE veranstaltungsid =?;";
		return sql;
	}
	public String getAlleVeranstaltungsParameterSql() {
		String sql ="SELECT name, semester, dozent, beschreibung, sonstiges FROM veranstaltungen WHERE veranstaltungsid =?;";
		return sql;
	}
	public String getUpdateVeranstaltungSql() {
		String sql ="UPDATE veranstaltungen SET name = ?, semester = ?, dozent = ?, beschreibung = ?, sonstiges = ? WHERE veranstaltungsid = ?;";
		return sql;
	}
	public String getVeranstaltungPruefenSql() {
		String sql ="SELECT name, uniid FROM veranstaltungen;";
		return sql;
	}
	public String getVeranstaltungenStudiengaengeSql() {
		String sql ="INSERT INTO veranstaltungenstudiengaenge (veranstaltungsid, studiengangid) VALUES (?,?);";
		return sql;
	}
	public String getVeranstaltungsIDSql() {
		String sql ="SELECT veranstaltungsid FROM veranstaltungen WHERE name =?;";
		return sql;
	}
	public String getStudiengangsIDSql() {
		String sql="SELECT studiengangid FROM studiengaenge WHERE studiengangname =?;";
		return sql;
	}
	public String getBeitragBearbeitetSql() {
		String sql ="UPDATE beitragmeldungen SET adminbearbeitet = true WHERE beitragsid = ?;";
		return sql;
	}
}
