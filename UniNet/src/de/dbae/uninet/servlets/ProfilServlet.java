package de.dbae.uninet.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.sqlClasses.ProfilSql;
import de.dbae.uninet.sqlClasses.StartseiteSql;

/**
 * Servlet implementation class ProfilServlet
 */
@WebServlet("/ProfilServlet")
public class ProfilServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (Profil)");
		ProfilSql sqlSt = new ProfilSql();
		String user = request.getParameter("userID");
		if (user == null) {
			user = session.getAttribute("UserID").toString();
		}
		int userID = Integer.parseInt(user);
		try {
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Profilseite", userID));
			// Infos
			String sql = sqlSt.getInfosSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				request.setAttribute("name", rs.getString(1) + " " + rs.getString(2));
				request.setAttribute("studiengang", rs.getString(3));
				SimpleDateFormat sdf = new SimpleDateFormat("d.MM.yyyy");
				request.setAttribute("studienbeginn", sdf.format(new Date(rs.getDate(4).getTime())));
				if (rs.getString(5) != null) {
					request.setAttribute("geburtstag", getInfoString("Geburtstag:<br>" + sdf.format(new Date(rs.getDate(5).getTime()))));
				}
				if (rs.getString(6) != null && !rs.getString(6).equals("")) {
					request.setAttribute("wohnort", getInfoString("Wohnort:<br>" + rs.getString(6)));
				}
				if (rs.getString(7) != null && !rs.getString(7).equals("")) {
					request.setAttribute("hobbys", getInfoString("Hobbys:<br>" + rs.getString(7)));
				}
				if (rs.getString(8) != null && !rs.getString(8).equals("")) {
					request.setAttribute("interessen", getInfoString("Interessen:<br>" + rs.getString(8)));
				}
				if (rs.getString(9) != null && !rs.getString(9).equals("")) {
					request.setAttribute("ueberMich", getInfoString("Über mich:<br>" + rs.getString(9)));
				}
				request.setAttribute("email", rs.getString(10));
			}
			sql = sqlSt.getAnzahlFreunde();
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			rs = pStmt.executeQuery();
			if (rs.next()) {
				request.setAttribute("anzFreunde", rs.getInt(1));
			}
			
			// Chatfreunde
			LadeChatFreundeServlet cfs = new LadeChatFreundeServlet();
			request.setAttribute("chatfreunde", cfs.getChatfreunde(session));
		
			// Weiterleitung
			request.getRequestDispatcher("Profil.jsp").forward(request, response);
		} catch (Exception e) {
			response.getWriter().append("SQL-Fehler");
			e.printStackTrace();
		} finally {
			try {
				if (con!=null) {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				}
			} catch (SQLException ignored) {}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		switch (name) {
		case "BeitragPosten":
			posteBeitrag(request, response);
			break;
		default:
			break;
		}
	}
	
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (ProfilBeitragPosten)");
		ProfilSql sqlSt = new ProfilSql();
		String beitrag = request.getParameter("beitrag");
		int verfasserID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sichtbarkeit = request.getParameter("sichtbarkeit");
		boolean sichtbar = true;
		if (sichtbarkeit.startsWith("Privat")) {
			sichtbar = false;
		}
		try {
			// Beitrag anlegen
			String sql = sqlSt.getBeitragAnlegenSql1();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setString(1, beitrag);
			pStmt.setInt(2, verfasserID);
			pStmt.setBoolean(3, sichtbar);
			pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
			pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
			pStmt.executeUpdate();
			
			// BeitragsID abfragen
			sql = sqlSt.getBeitragAnlegenSql2();
			pStmt = con.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			int beitragsID;
			if (rs.next()) {
				beitragsID = rs.getInt(1);
				// Chronikbeitrag eintragen
				sql = sqlSt.getBeitragAnlegenSql3();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.executeUpdate();
				response.sendRedirect("ProfilServlet");
			} else {
				System.out.println("Problem beim Anlegen des Beitrags");
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler aufgetreten");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Die Verbindung wurde geschlossen");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getInfoString(String info) {
		return "<li class='list-group-item'>" + info + "</li>";
	}

}
