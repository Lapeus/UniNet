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
import de.dbae.uninet.javaClasses.Studiengang;
import de.dbae.uninet.sqlClasses.AdminSql;

/**
 * Servlet implementation class AdminStudiengaengeServlet
 * 
 * @author Leon Schaffert
 */
@WebServlet("/AdminStudiengaengeServlet")
public class AdminStudiengaengeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Die DB-Verbindung.
	 */
	private DBConnection dbcon;
	/**
	 * 
	 */
	private int uniid;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminStudiengaengeServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		int userid = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getUniidFromAdminsSql();
		PreparedStatement pStmt;
		try {
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userid);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			uniid = rs.getInt(1);
			try {
				// Setze alle Studiengaenge als Attribut
				request.setAttribute("studiengangsList", getStudiengaenge(request, con, uniid, ""));
			} catch (NullPointerException npex) {
				response.sendRedirect("FehlerServlet?fehler=Session");
			} catch (SQLException sqlex) {
				response.sendRedirect("FehlerServlet?fehler=DBCon");
			} finally {
				// Verbindung schliessen
				dbcon.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (con.isClosed()) {
				dbcon = new DBConnection();
				con = dbcon.getCon();
			}
			sql = sqlSt.getUninameFromUniidSql();
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, uniid);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			String uniname = rs.getString(1);
			request.setAttribute("universitaet", uniname);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
		if (request.getParameter("sort") != null) {
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				String sqlExt = "";
				if (request.getParameter("sort").equals("id")) {
					sqlExt = " ORDER BY studiengangid";
				} else if (request.getParameter("sort").equals("name")) {
					sqlExt = " ORDER BY studiengangname";
				}

				request.setAttribute("studiengangsList", getStudiengaenge(request, con, uniid, sqlExt));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			killConnection(con);

		}

		if (request.getParameter("loeschen") != null) {

			sql = sqlSt.getStudiengangLoeschenSql();
			int studiengangid = Integer.parseInt(request.getParameter("loeschen"));
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, studiengangid);
				pStmt.execute();
				request.setAttribute("studiengangsList", getStudiengaenge(request, con, uniid, ""));
			} catch (SQLException sqlex) {
				System.err.println("SQL-Fehler!");
				sqlex.printStackTrace();
			} finally {
				// Verbindung schliessen
				dbcon.close();
			}
		}
		request.getRequestDispatcher("StudiengaengeVerwalten.jsp").forward(request, response);
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
		if (request.getParameter("studiengang") != null) {
			// Alle eingaben abfragen
			String name = request.getParameter("studiengang");
			if (!name.equals("")) {
				try {
					// ALLE DATEN VORHANDEN
					boolean nameDoppelt = false;
					sql = aSql.getStudiengangSql();
					pStmt = con.prepareStatement(sql);
					pStmt.setString(1, name);
					ResultSet rs = pStmt.executeQuery();
					while (rs.next()) {
						if ((rs.getString(1).equals(name)) && (rs.getInt(2) == uniid)) {
							nameDoppelt = true;
						}
					}
					System.out.println(!nameDoppelt);
					if (!nameDoppelt) {
						// Neuen Studiengang in Tabelle speichern
						sql = aSql.getStudiengangAnlegenSql();
						pStmt = con.prepareStatement(sql);
						pStmt.setString(1, name);
						pStmt.setInt(2, uniid);
						pStmt.executeUpdate();
						meldung = "Neuer Studiengang wurde angelegt";
						request.setAttribute("meldung", meldung);
						request.getRequestDispatcher("AdminStudiengaengeServlet").forward(request, response);
						forwarded = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					meldung = "Name ist bereits vergeben";
					request.setAttribute("meldung", meldung);
				}
			}
		} else {
			// DATEN UNVOLLSTÄNDIG
			// Meldung bei unvollständiger Registrierung
			meldung = "Bitte f&uuml;llen Sie das Formular vollst&auml;ndig aus";
			request.setAttribute("meldung", meldung);
		}
		try {
			request.setAttribute("studiengangsList", getStudiengaenge(request, con, uniid, ""));
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
			request.getRequestDispatcher("StudiengaengeVerwalten.jsp").forward(request, response);
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

	private List<Studiengang> getStudiengaenge(HttpServletRequest request, Connection con, int uniid, String sqlExt)
			throws SQLException {
		List<Studiengang> studiengaenge = new ArrayList<Studiengang>();
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getStudiengangAnzeigeSql();
		sql += sqlExt;
		sql += ";";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, uniid);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			studiengaenge.add(new Studiengang(rs.getInt(1), rs.getString(2)));
		}
		return studiengaenge;
	}

}
