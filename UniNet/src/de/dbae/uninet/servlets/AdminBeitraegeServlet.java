package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.javaClasses.HashtagVerarbeitung;
import de.dbae.uninet.sqlClasses.AdminSql;
import de.dbae.uninet.sqlClasses.BeitragSql;

/**
 * Servlet implementation class AdminBeitraegeServlet
 * 
 * @author Leon Schaffert
 */
@WebServlet("/AdminBeitraegeServlet")
public class AdminBeitraegeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Die DBConnection
	 */
	private DBConnection dbcon;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBeitraegeServlet() {
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
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		try {
			if (request.getParameter("name") != null) {
				int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
				if (request.getParameter("name").equals("BeitragLoeschen")) {
					// new
					// ErstelleBenachrichtigung(con).adminBeitragGeloescht(beitragsID);
					BeitragSql sqlSt = new BeitragSql();
					String sql = sqlSt.getSqlStatement("BeitragLoeschen");
					PreparedStatement pStmt = con.prepareStatement(sql);
					pStmt.setInt(1, beitragsID);
					pStmt.executeUpdate();
				} else if (request.getParameter("name").equals("Beitragok")) {

					AdminSql aSql = new AdminSql();
					String sql = aSql.getBeitragBearbeitetSql();
					PreparedStatement pStmt;
					pStmt = con.prepareStatement(sql);
					pStmt.setInt(1, beitragsID);
					pStmt.execute();
				}
			}
			request.setAttribute("beitragList", getBeitraege(request, con, userID));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbcon.close();
		request.getRequestDispatcher("BeitragsVerwaltung.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Standardmaessig soll der Beitrag und nicht die Like-Liste angezeigt
		// werden
		request.setAttribute("beitragAnzeigen", true);
		// Lade die Aktion, die vom Servlet durchgefuehrt werden soll
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		BeitragSql sqlSt = new BeitragSql();
		try {
			if (request.getParameter("name") != null) {
				int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
				// Bisherige Hashtags loeschen
				String sql = sqlSt.getSqlStatement("HashtagsLoeschen");
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.executeUpdate();
				// Beitrag bearbeiten
				sql = sqlSt.getSqlStatement("BeitragBearbeiten");
				pStmt = con.prepareStatement(sql);
				String beitrag = new HashtagVerarbeitung(request.getAttribute("neuerBeitrag").toString())
						.setHashTags(con, beitragsID);
				pStmt.setString(1, beitrag);
				pStmt.setInt(2, beitragsID);
				pStmt.executeUpdate();
				AdminSql aSql = new AdminSql();
				sql = aSql.getBeitragBearbeitetSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.executeUpdate();
				// new
				// ErstelleBenachrichtigung(con).adminBeitragGeaendert(beitragsID);
			}
		} catch (

		NullPointerException npex) {
			response.sendRedirect("FehlerServlet?fehler=Session");
		} catch (SQLException sqlex) {
			response.sendRedirect("FehlerServlet?fehler=DBCon");
		} finally {
			dbcon.close();
		}
		doGet(request, response);
	}

	private List<Beitrag> getBeitraege(HttpServletRequest request, Connection con, int userID) throws SQLException {
		List<Beitrag> beitraege = new ArrayList<Beitrag>();
		BeitragServlet bs = new BeitragServlet();
		// Beitraege
		beitraege = bs.getGemeldeteBeitraege(con);
		return beitraege;
	}
}
