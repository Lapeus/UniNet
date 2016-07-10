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
import de.dbae.uninet.javaClasses.Uni;
import de.dbae.uninet.sqlClasses.AdminSql;

/**
 * Servlet implementation class UnisVerwaltenServlet
 */
@WebServlet("/UnisVerwaltenServlet")
public class UnisVerwaltenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DBConnection dbcon;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnisVerwaltenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Oeffne eine neue DB-Verbindung
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		try {
			// Setze alle Unis als Attribut
			request.setAttribute("uniList", getUnis(request, con, ""));
		} catch (NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Verbindung schliessen
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
					sqlExt = " ORDER BY uniid";
				} else if (request.getParameter("sort").equals("name")) {
					sqlExt = " ORDER BY uniname";
				} else if (request.getParameter("sort").equals("standort")) {
					sqlExt = " ORDER BY unistandort";
				}

				request.setAttribute("uniList", getUnis(request, con, sqlExt));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			killConnection(con);
		}
		if (request.getParameter("loeschen") != null) {
			AdminSql sqlSt = new AdminSql();
			String sql = sqlSt.getUniLoeschenSql();
			int uniid = Integer.parseInt(request.getParameter("loeschen"));
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, uniid);
				pStmt.execute();
				request.setAttribute("uniList", getUnis(request, con, ""));
			} catch (SQLException sqlex) {
				System.err.println("SQL-Fehler!");
				sqlex.printStackTrace();
			} finally {
				// Verbindung schliessen
				dbcon.close();
			}
		}
		request.getRequestDispatcher("UniVerwaltung.jsp").forward(request, response);
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
		boolean forwarded = false;
		if (request.getParameter("registrieren") != null) {
			// Alle eingaben abfragen
			String name = request.getParameter("uniname");
			String standort = request.getParameter("standort");
			if (!name.equals("") && !standort.equals("")) {
				try {
					// ALLE DATEN VORHANDEN
					boolean nameDoppelt = false;
					String sql = aSql.getUniversitaetenSql();
					PreparedStatement pStmt = con.prepareStatement(sql);
					pStmt.setString(1, name);
					ResultSet rs = pStmt.executeQuery();
					while (rs.next()) {
						if (rs.getString(1).equals(name)) {
							nameDoppelt = true;
						}
					}
					if (!nameDoppelt) {
						// Neue Uni in Tabelle speichern
						sql = aSql.getUniAnlegenSql();
						pStmt = con.prepareStatement(sql);
						pStmt.setString(1, name);
						pStmt.setString(2, standort);
						pStmt.executeUpdate();
						meldung = "Neue Uni wurde angelegt";
						request.setAttribute("meldung", meldung);
						request.getRequestDispatcher("UnisVerwaltenServlet").forward(request, response);
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
		}
		try {
			request.setAttribute("uniList", getUnis(request, con, ""));
		} catch (SQLException sqlExc) {
			sqlExc.printStackTrace();
		}
		if (!forwarded) {
			request.getRequestDispatcher("UniVerwaltung.jsp").forward(request, response);
		}
		killConnection(con);
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

	private List<Uni> getUnis(HttpServletRequest request, Connection con, String sqlExt) throws SQLException {
		List<Uni> unis = new ArrayList<Uni>();
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getUniAnzeigeSql();
		sql += sqlExt;
		sql += ";";
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			unis.add(new Uni(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		return unis;
	}
}
