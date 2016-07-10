package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

/**
 * @author Leon Schaffert
 * Servlet implementation class AdminVeranstaltungBearbeiten
 */
@WebServlet("/AdminVeranstaltungBearbeitenServlet")
public class AdminVeranstaltungBearbeitenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Die DBConnection
	 */
	private DBConnection dbcon;
	/**
	 * Die ID der aktuellen Uni
	 */
	private int uniid;
	/**
	 * Die ID der aktuellen Veranstaltung
	 */
	private int veranstaltungsid;
	/**
	 * Der vorherige Name der Veranstaltung
	 */
	private String alterName;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminVeranstaltungBearbeitenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			veranstaltungsid = Integer.parseInt(request.getParameter("veranstaltungsid"));
			alterName = felderFuellen(request, veranstaltungsid);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("VeranstaltungBearbeiten.jsp").forward(request, response);
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
		boolean forwarded = false;
			// Alle eingaben abfragen
			String name = request.getParameter("name");
			String dozent = request.getParameter("dozent");
			String semester = request.getParameter("semester");
			String beschreibung = request.getParameter("beschreibung");
			String sonstiges = request.getParameter("sonstiges");
			if (!name.equals("") && !dozent.equals("") && !semester.equals("") && !beschreibung.equals("") && !sonstiges.equals("")) {
				try {
					// ALLE DATEN VORHANDEN
					boolean nameDoppelt = false;
					sql = aSql.getVeranstaltungPruefenSql();
					pStmt = con.prepareStatement(sql);
					ResultSet rs = pStmt.executeQuery();
					while (rs.next()) {
						if ((rs.getString(1).equals(name)) && (rs.getInt(2) == uniid) && (!name.equals(alterName))) {
							nameDoppelt = true;
						}
					}
					if (!nameDoppelt) {
						// Veränderte Werte der Veranstaltung in Tabelle speichern
						sql = aSql.getUpdateVeranstaltungSql();
						pStmt = con.prepareStatement(sql);
						pStmt.setString(1, name);
						pStmt.setString(2, semester);
						pStmt.setString(3, dozent);
						pStmt.setString(4, beschreibung);
						pStmt.setString(5, sonstiges);
						pStmt.setInt(6, veranstaltungsid);
						pStmt.execute();
						meldung = "Veranstaltung wurde bearbeitet";
						request.setAttribute("meldung", meldung);
						request.getRequestDispatcher("AdminVeranstaltungenVerwaltenServlet").forward(request, response);
						forwarded = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					meldung = "Name ist bereits vergeben";
					request.setAttribute("meldung", meldung);
				}
			}  else {
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
		}
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


	private String felderFuellen(HttpServletRequest request, int veranstaltungsid) throws SQLException {
		// die Felder füllen wenn geladen wird
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		AdminSql aSql = new AdminSql();
		String sql = aSql.getAlleVeranstaltungsParameterSql();
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, veranstaltungsid);
		ResultSet rs = pStmt.executeQuery();
		String alterName ="";
		while (rs.next()) {
			String name = rs.getString(1);
			String dozent = rs.getString(3);
			String semester = rs.getString(2);
			String beschreibung = rs.getString(4);
			String sonstiges = rs.getString(5);
			request.setAttribute("name", name);
			request.setAttribute("dozent", dozent);
			request.setAttribute("semester", semester);
			request.setAttribute("beschreibung", beschreibung);
			request.setAttribute("sonstiges", sonstiges);
			alterName = name;
		}
		return alterName;
	}
}
