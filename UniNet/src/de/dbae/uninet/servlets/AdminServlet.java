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
import de.dbae.uninet.javaClasses.Admin;
import de.dbae.uninet.sqlClasses.AdminSql;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Die DBConnection
	 */
	private DBConnection dbcon;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
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
			// Setze alle LocalAdmins als Attribut
			request.setAttribute("adminList", getAdmins(request, con, ""));
		} catch (NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			// Verbindung schliessen
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
					sqlExt = " ORDER BY userid";
				} else if (request.getParameter("sort").equals("vorname")) {
					sqlExt = " ORDER BY vorname";
				} else if (request.getParameter("sort").equals("nachname")) {
					sqlExt = " ORDER BY nachname";
				} else if (request.getParameter("sort").equals("uni")) {
					sqlExt = " ORDER BY uniname";
				}

				request.setAttribute("adminList", getAdmins(request, con, sqlExt));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			killConnection(con);
		}
		if (request.getParameter("loeschen") != null) {
			AdminSql sqlSt = new AdminSql();
			String sql = sqlSt.getAdminLoeschen1Sql();
			int userid = Integer.parseInt(request.getParameter("loeschen"));
			System.out.println(userid);
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, userid);
				pStmt.executeUpdate();
				sql = sqlSt.getAdminLoeschen2Sql();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1,  userid);
				pStmt.executeUpdate();
				request.setAttribute("adminList", getAdmins(request, con, ""));
			} catch (SQLException sqlex) {
				System.err.println("SQL-Fehler!");
				sqlex.printStackTrace();
			} finally {
				//Verbindung schliessen
				killConnection(con);
			}
		}
		request.getRequestDispatcher("AdminVerwaltung.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	/**
	 * 
	 * @param con
	 */
	private void killConnection(Connection con) {
		try {
			if (con != null) {
				dbcon.close();
			}
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}
	private List<Admin> getAdmins(HttpServletRequest request, Connection con, String sqlExt) throws SQLException {
		List<Admin> admins = new ArrayList<Admin>();
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getAdminsSql();
		sql += sqlExt;
		sql += ";";
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			admins.add(new Admin(rs.getString(1), rs.getString(2),2 , rs.getString(3), rs.getInt(4)));
		}
		return admins;
	}
}
