package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Veranstaltung;
import de.dbae.uninet.sqlClasses.AdminSql;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet implementation class AdminVeranstaltungAnlegenServlet
 * 
 * @author Leon Schaffert
 */
@WebServlet("/AdminVeranstaltungAnlegenServlet")
public class AdminVeranstaltungAnlegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Die DBConnection
	 */
	private DBConnection dbcon;
	/**
	 * Die ID der aktuellen Universit&auml;t
	 */
	private int uniid;
	/**
	 * 
	 */
	private boolean forwarded;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminVeranstaltungAnlegenServlet() {
		super();
		forwarded = false;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AnmeldeSql sqlSt = new AnmeldeSql();
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		AdminSql aSql = new AdminSql();
		int userid = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		String sql = aSql.getUniidFromAdminsSql();
		PreparedStatement pStmt;
		ResultSet rs;
		try {
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userid);
			rs = pStmt.executeQuery();
			rs.next();
			uniid = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = aSql.getUninameFromUniidSql();
		try {
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, uniid);
			rs = pStmt.executeQuery();
			rs.next();
			String uni = rs.getString(1);
			updateStudiengaenge(request, response, con, sqlSt, uni);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!forwarded) {
			request.getRequestDispatcher("VeranstaltungAnlegen.jsp").forward(request, response);
			forwarded = true;
		}
		forwarded = false;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		String meldung = "";
		AdminSql aSql = new AdminSql();
		int userid = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		String sql = aSql.getUniidFromAdminsSql();
		PreparedStatement pStmt;
		try {
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userid);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			uniid = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Alle eingaben abfragen
		String name = request.getParameter("name");
		String dozent = request.getParameter("dozent");
		String semester = request.getParameter("semester");
		String beschreibung = request.getParameter("beschreibung");
		String sonstiges = request.getParameter("sonstiges");
		String studiengang = request.getParameter("studiengang");
		if (!name.equals("") && !dozent.equals("") && !semester.equals("") && !beschreibung.equals("")
				&& !sonstiges.equals("") && !studiengang.equals("")) {
			try {
				// ALLE DATEN VORHANDEN
				boolean nameDoppelt = false;
				sql = aSql.getVeranstaltungSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setString(1, name);
				ResultSet rs = pStmt.executeQuery();
				while (rs.next()) {
					if ((rs.getString(1).equals(name)) && (rs.getInt(2) == uniid)) {
						nameDoppelt = true;
					}
				}
				if (!nameDoppelt) {
					// Neue Veranstaltung in Tabelle speichern
					sql = aSql.getVeranstaltungAnlegenSql();
					pStmt = con.prepareStatement(sql);
					pStmt.setString(1, name);
					pStmt.setInt(2, uniid);
					pStmt.setString(3, semester);
					pStmt.setString(4, dozent);
					pStmt.setString(5, beschreibung);
					pStmt.setString(6, sonstiges);
					pStmt.execute();
					sql = aSql.getStudiengangsIDSql();
					pStmt = con.prepareStatement(sql);
					pStmt.setString(1, studiengang);
					rs = pStmt.executeQuery();
					rs.next();
					int studiengangid = rs.getInt(1);
					sql = aSql.getVeranstaltungsIDSql();
					pStmt = con.prepareStatement(sql);
					pStmt.setString(1, name);
					rs = pStmt.executeQuery();
					rs.next();
					int veranstaltungsid = rs.getInt(1);
					sql = aSql.getVeranstaltungenStudiengaengeSql();
					pStmt = con.prepareStatement(sql);
					pStmt.setInt(1, veranstaltungsid);
					pStmt.setInt(2, studiengangid);
					pStmt.execute();
					meldung = "Neue Veranstaltung wurde angelegt";
					request.setAttribute("meldung", meldung);
					request.getRequestDispatcher("AdminVeranstaltungenVerwaltenServlet").forward(request, response);
					forwarded = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				meldung = "Name ist bereits vergeben";
				request.setAttribute("meldung", meldung);
			}
		} else {
			// DATEN UNVOLLSTÄNDIG
			// Meldung bei unvollständiger Registrierung
			meldung = "Bitte f&uuml;llen Sie das Formular vollst&auml;ndig aus";
			request.setAttribute("meldung", meldung);
		}
		try {
			request.setAttribute("studiengangsList", getVeranstaltungen(request, con));
		} catch (SQLException sqlExc) {
			sqlExc.printStackTrace();
		}
		killConnection(con);
		try {
			if (con.isClosed()) {
				dbcon = new DBConnection();
				con = dbcon.getCon();
			}
			AdminSql sqlSt = new AdminSql();
			sql = sqlSt.getUninameFromUniidSql();
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, uniid);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String uniname = rs.getString(1);
				request.setAttribute("universitaet", uniname);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
		if (!forwarded) {
			request.getRequestDispatcher("AdminVeranstaltungenVerwaltenServlet").forward(request, response);
			forwarded = true;
		}
		forwarded = false;
	}

	private void killConnection(Connection con) {
		try {
			if (con != null) {
				dbcon.close();
			}
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	private List<Veranstaltung> getVeranstaltungen(HttpServletRequest request, Connection con) throws SQLException {
		List<Veranstaltung> vs = new ArrayList<Veranstaltung>();
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getVeranstaltungUebersichtSql();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, uniid);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			vs.add(new Veranstaltung(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
		}
		return vs;
	}

	private void updateStudiengaenge(HttpServletRequest request, HttpServletResponse response, Connection con,
			AnmeldeSql sqlSt, String uni) throws ServletException, IOException {
		List<String> studiengaenge = new ArrayList<String>();
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getStudiengaenge());
			pStmt.setString(1, uni);
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					studiengaenge.add(result.getString(i));
				}
			}
			request.setAttribute("studiengaenge", studiengaenge);
			if (!forwarded) {
				request.getRequestDispatcher("VeranstaltungAnlegen.jsp").forward(request, response);
				forwarded = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SQL Fehler - AnmeldeSQL.getStudiengaenge()");
		} finally {
			killConnection(con);
		}
	}

}
