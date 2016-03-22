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
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Veranstaltung;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.ProfilSql;
import de.dbae.uninet.sqlClasses.VeranstaltungenSql;

/**
 * Servlet implementation class VeranstaltungenServlet
 */
@WebServlet("/VeranstaltungenServlet")
public class VeranstaltungenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VeranstaltungenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		if (name == null) {
			name = "Veranstaltung";
		}
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (VeranstaltungenGet)");
		try {
			String sql = new VeranstaltungenSql().getVeranstaltungen();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			List<Veranstaltung> veranstaltungList = new ArrayList<Veranstaltung>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String bezeichnung = rs.getString(2);
				veranstaltungList.add(new Veranstaltung(id, bezeichnung));
			}
			request.setAttribute("veranstaltungList", veranstaltungList);
		} catch (Exception e) {
			System.err.println("SQL Fehler in VeranstaltungenServlet");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		switch (name) {
		case "Veranstaltung":
			veranstaltung(request, response);
			break;
		case "Uebersicht":
			request.setAttribute("tab", "infos");
			uebersicht(request, response);
			break;
		default:
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		if (name == null) {
			name = "";
		}
		switch (name) {
		case "BeitragPosten":
			posteBeitrag(request, response);
			break;

		default:
			doGet(request, response);
			break;
		}
	}
	
	private void uebersicht(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (VeranstaltungenUebersicht)");
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		try {
			String sql = new VeranstaltungenSql().getAlleVeranstaltungen();
			PreparedStatement pStmt = con.prepareStatement(sql);
			//pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			List<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String bezeichnung = rs.getString(2);
				veranstaltungen.add(new Veranstaltung(id, bezeichnung));
			}
			request.setAttribute("veranstaltungen", veranstaltungen);
			request.getRequestDispatcher("Veranstaltungsuebersicht.jsp").forward(request, response);
		} catch (Exception e) {
			System.out.println("SQL Fehler in VeranstaltungenServlet");
		} finally {
			if (con != null) {
				try {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void veranstaltung(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		int userID = Integer.parseInt(request.getSession().getAttribute("UserID").toString());
		// VeranstaltungsID lesen und setzen
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		// Aktuellen Tab lesen und setzen
		String tab = request.getParameter("tab");
		request.setAttribute("tab", tab);
		// Kontrolle, ob man bereits in der Veranstaltung eingeschrieben ist
		boolean eingetragen = false;
		for (Veranstaltung veranstaltung : (ArrayList<Veranstaltung>)request.getAttribute("veranstaltungList")) {
			if ((veranstaltung.getId() + "").equals(id)) {
				eingetragen = true;
			}
		}
		request.setAttribute("eingetragen", eingetragen);
		try {
			// VeranstaltungsInfos laden
			String sql = new VeranstaltungenSql().getInfos();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, Integer.parseInt(id));
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				String name = rs.getString(1);
				String semester = rs.getString(2);
				String dozent = rs.getString(3);
				String beschreibung = rs.getString(4);
				String sonstiges = rs.getString(5);
				Veranstaltung veranstaltung = new Veranstaltung(Integer.parseInt(id), name, dozent, semester, beschreibung, sonstiges);
				request.setAttribute("veranstaltung", veranstaltung);
			}
			switch (tab) {
			case "beitraege":
				if (eingetragen) {
					request.setAttribute("beitraegeActive", "active");
					request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Veranstaltungen", userID, id));
				} else {
					request.setAttribute("infosActive", "active");
					request.setAttribute("tab", "infos");
				}
				break;
			case "infos":
				request.setAttribute("infosActive", "active");
				break;
			case "mitglieder":
				request.setAttribute("mitgliederActive", "active");
				break;
			default:
				break;
			}
			request.getRequestDispatcher("Veranstaltungen.jsp").forward(request, response);
		} catch (Exception e) {
			System.out.println("SQL Fehler in VeranstaltungenServlet");
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession();
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde geöffnet (VeranstaltungBeitragPosten)");
		BeitragSql sqlSt = new BeitragSql();
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
				// Veranstaltungsbeitrag eintragen
				sql = sqlSt.getBeitragAnlegenSqlVeranstaltung();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				int veranstaltungsID = Integer.parseInt(request.getParameter("id"));
				pStmt.setInt(2, veranstaltungsID);
				pStmt.executeUpdate();
				response.sendRedirect("VeranstaltungenServlet?tab=beitraege&id=" + veranstaltungsID);
			} else {
				System.out.println("Problem beim Anlegen des Beitrags");
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler aufgetreten");
			e.printStackTrace();
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
}
