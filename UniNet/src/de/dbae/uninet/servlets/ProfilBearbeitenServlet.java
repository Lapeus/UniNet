package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.ProfilSql;

/**
 * Servlet implementation class ProfilBearbeitenServlet
 */
@WebServlet("/ProfilBearbeitenServlet")
public class ProfilBearbeitenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilBearbeitenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (ProfilBearbeiten)");
		ProfilSql sqlSt = new ProfilSql();
		try {
			String sql = sqlSt.getInfosBSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				request.setAttribute("vorname", rs.getString(1));
				request.setAttribute("nachname", rs.getString(2));
				SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
				String date = "";
				try {
					date = sdf.format(new Date(rs.getDate(3).getTime()));
				} catch (Exception e){}
				request.setAttribute("geburtstag", date);
				request.setAttribute("wohnort", rs.getString(4));
				request.setAttribute("hobbys", rs.getString(5));
				request.setAttribute("interessen", rs.getString(6));
				request.setAttribute("ueberMich", rs.getString(7));
				request.getRequestDispatcher("ProfilBearbeiten.jsp").forward(request, response);
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler in ProfilBearbeitenServlet");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Verbindung erfolgreich beendet!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (ProfilBearbeiten)");
		ProfilSql sqlSt = new ProfilSql();
		try {
			String sql = sqlSt.getAendereNamenSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setString(1, request.getParameter("vorname").toString());
			pStmt.setString(2, request.getParameter("nachname").toString());
			pStmt.setInt(3, userID);
			pStmt.executeUpdate();
			sql = sqlSt.getAendereInfosSql();
			pStmt = con.prepareStatement(sql);
			SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
			Date date = null;
			try {
				date = new Date(sdf.parse(request.getParameter("geburtstag").toString()).getTime());
			} catch (Exception e) {
			}
			String wohnort = request.getParameter("wohnort").toString();
			String hobbys = request.getParameter("hobbys").toString();
			String interessen = request.getParameter("interessen").toString();
			String ueberMich = request.getParameter("ueberMich").toString();
			pStmt.setDate(1, date);
			pStmt.setString(2, wohnort);
			pStmt.setString(3, hobbys);
			pStmt.setString(4, interessen);
			pStmt.setString(5, ueberMich);
			pStmt.setInt(6, userID);
			pStmt.executeUpdate();
			response.sendRedirect("ProfilServlet?userID=" + userID);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SQL Fehler in ProfilBearbeitenServlet");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Verbindung erfolgreich beendet!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
