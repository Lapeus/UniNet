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
 * Servlet implementation class AdminVeranstaltungVerwaltenServlet
 */
@WebServlet("/AdminVeranstaltungenVerwaltenServlet")
public class AdminVeranstaltungenVerwaltenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * Die DB Connection
     */
	private DBConnection dbcon;
	 /**
     * die id der Aktuellen Uni
     */
	private int uniid;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminVeranstaltungenVerwaltenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				// Setze alle Veranstaltungen als Attribut
				request.setAttribute("veranstaltungList", getVeranstaltungen(request, con, ""));
			} catch (NullPointerException npex) {
				response.sendRedirect("FehlerServlet?fehler=Session");
			} catch (SQLException sqlex) {
				response.sendRedirect("FehlerServlet?fehler=DBCon");
			} finally {
				// Verbindung schliessen
				killConnection(con);
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
			killConnection(con);
		}
		if (request.getParameter("sort") != null) {
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				String sqlExt = "";
				if (request.getParameter("sort").equals("id")) {
					sqlExt = " ORDER BY veranstaltungsid";
				} else if (request.getParameter("sort").equals("name")) {
					sqlExt = " ORDER BY name";
				} else if (request.getParameter("sort").equals("dozent")) {
					sqlExt = " ORDER BY dozent";
				} else if (request.getParameter("sort").equals("semester")) {
					sqlExt = " ORDER BY semester";
				}

				request.setAttribute("veranstaltungList", getVeranstaltungen(request, con, sqlExt));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			killConnection(con);

		}
		if (request.getParameter("loeschen") != null) {
			
			sql = sqlSt.getVeranstaltungLoeschenSql();
			int veranstaltungsid = Integer.parseInt(request.getParameter("loeschen"));
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, veranstaltungsid);
				pStmt.execute();
				request.setAttribute("veranstaltungList", getVeranstaltungen(request, con, ""));
			} catch (SQLException sqlex) {
				System.err.println("SQL-Fehler!");
				sqlex.printStackTrace();
			} finally {
				// Verbindung schliessen
				killConnection(con);
			}
		}
		request.getRequestDispatcher("VeranstaltungenVerwalten.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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

	private List<Veranstaltung> getVeranstaltungen(HttpServletRequest request, Connection con, String sqlExt) throws SQLException {
		List<Veranstaltung> vs = new ArrayList<Veranstaltung>();
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getVeranstaltungUebersichtSql();
		sql += sqlExt;
		sql += ";";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, uniid);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			vs.add(new Veranstaltung(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
		}
		return vs;
	}
}
