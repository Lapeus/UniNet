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
import de.dbae.uninet.javaClasses.Student;
import de.dbae.uninet.sqlClasses.AdminSql;

/**
 * Servlet implementation class AdminUserVerwaltenServlet
 * 
 * @author Leon Schaffert
 */
@WebServlet("/AdminUserVerwaltenServlet")
public class AdminUserVerwaltenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private DBConnection dbcon;
	/**
	 * 
	 */
	private int uniid;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminUserVerwaltenServlet() {
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
			// Setze alle Studenten als Attribut
			request.setAttribute("studentList", getStudenten(request, con, ""));
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
					sqlExt = " ORDER BY studentid";
				} else if (request.getParameter("sort").equals("vorname")) {
					sqlExt = " ORDER BY vorname";
				} else if (request.getParameter("sort").equals("nachname")) {
					sqlExt = " ORDER BY nachname";
				} else if (request.getParameter("sort").equals("email")) {
					sqlExt = " ORDER BY email";
				}

				request.setAttribute("studentList", getStudenten(request, con, sqlExt));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			killConnection(con);
		}
		if (request.getParameter("loeschen") != null) {
			int loeschUserid = Integer.parseInt(request.getParameter("loeschen"));
			System.out.println(loeschUserid);
			try {
				if (con.isClosed()) {
					dbcon = new DBConnection();
					con = dbcon.getCon();
				}
				sql = sqlSt.getStudentEmailSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, loeschUserid);
				ResultSet rs = pStmt.executeQuery();
				rs.next();
				String email = rs.getString(1);
				sql = sqlSt.getEmailSperrenSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setString(1, email);
				pStmt.executeUpdate();
				sql = sqlSt.getStudentLoeschenSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, loeschUserid);
				pStmt.executeUpdate();
				request.setAttribute("studentList", getStudenten(request, con, ""));
			} catch (SQLException sqlex) {
				System.err.println("SQL-Fehler!");
				sqlex.printStackTrace();
			} finally {
				//Verbindung schliessen
				killConnection(con);
			}
		}
		request.getRequestDispatcher("UserVerwalten.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
	private List<Student> getStudenten(HttpServletRequest request, Connection con, String sqlExt) throws SQLException {
		List<Student> studenten = new ArrayList<Student>();
		AdminSql sqlSt = new AdminSql();
		String sql = sqlSt.getStudentenSql();
		sql += sqlExt;
		sql += ";";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, uniid);
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			studenten.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
		}
		return studenten;
	}
}
