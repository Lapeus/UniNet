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
import de.dbae.uninet.javaClasses.Beitrag;
import de.dbae.uninet.javaClasses.Kommentar;
import de.dbae.uninet.javaClasses.KommentarZuUnterkommentar;
import de.dbae.uninet.javaClasses.Unterkommentar;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.ProfilSql;

/**
 * Servlet implementation class BeitragServlet
 */
@WebServlet("/BeitragServlet")
public class BeitragServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BeitragServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		HttpSession session = request.getSession();
		if (request.getParameter("anzeigen") == null) {
			request.setAttribute("anzeigen", false);
		} else {
			request.setAttribute("anzeigen", true);
			name = null;
		}
		if (name == null) {
			kompletterLoad(request, response, session);
		} else {
			switch (name) {
			case "Like":
				likeBeitrag(request, response);
				break;
			case "Kommentar":
				kommentieren(request, response);
				break;
			case "BeitragLoeschen":
				beitragLoeschen(request, response);
				break;
			case "KommentarLoeschen":
				kommentarLoeschen(request, response, "kommentar");
				break;
			case "KommLoeschen":
				kommentarLoeschen(request, response, "komm");
				break;
			case "KzukommLoeschen":
				kommentarLoeschen(request, response, "kzukomm");
				break;
			case "AntwortAufKommentar":
				antwortVorbereiten(request, response, "kommentar");
				break;
			case "AntwortAufKomm":
				antwortVorbereiten(request, response, "komm");
				break;
			case "KommentarAntwort":
				kommentarAntworten(request, response);
			default:
				break;
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void kompletterLoad(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde ge�ffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		String beitragId = request.getParameter("beitragsID");
		int beitragsID = Integer.parseInt(beitragId);
		try {
			// Beitraege
			String sql = sqlSt.getBeitrag();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			ResultSet rs = pStmt.executeQuery();
			Beitrag beitrag;
			if (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2) + " " + rs.getString(3);
				String timeStamp = "Zeitstempel P";//rs.getString(3) + " " + rs.getBoolean(4);
				String nachricht = rs.getString(4);
				int anzahlLikes = rs.getInt(5);
				int anzahlKommentare = rs.getInt(6);
				sql = sqlSt.getLikeAufBeitragSql();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.setInt(2, userID);
				ResultSet rs2 = pStmt.executeQuery();
				if (rs2.next()) {
					boolean like = rs2.getInt(1) == 0 ? false : true;
					String loeschenErlaubt = userID == id ? "X" : "";
					beitrag = new Beitrag(id, name, timeStamp, nachricht, anzahlLikes, anzahlKommentare, beitragsID, like, loeschenErlaubt);
					beitrag.setKommentarList(getKommentare(con, beitragsID, userID));
					request.setAttribute("beitrag", beitrag);
					if (like) {
						request.setAttribute("liClass", "geliket");
					} else {
						request.setAttribute("liClass", "");
					}
					request.getRequestDispatcher("Beitrag.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im BeitragServlet");
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
	
	private List<Kommentar> getKommentare(Connection con, int beitragsID, int userIDsession) {
		List<Kommentar> kommentarList = new ArrayList<Kommentar>();
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = sqlSt.getKommentare();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				// Kommentar-Werte
				int userID = rs.getInt(1);
				int kommID = rs.getInt(2);
				String name = rs.getString(3) + " " + rs.getString(4);
				String kommentar = rs.getString(5);
				// SQL Abfrage der Unterkommentare
				sql = sqlSt.getUnterkommentare();
				pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, kommID);
				ResultSet rs2 = pStmt.executeQuery();
				List<Unterkommentar> unterkommentarList = new ArrayList<Unterkommentar>();
				while (rs2.next()) {
					// Unterkommentar-Werte
					int userID2 = rs2.getInt(1);
					int kommID2 = rs2.getInt(2);
					String name2 = rs2.getString(3) + " " + rs2.getString(4);
					String kommentar2 = rs2.getString(5);
					Unterkommentar ukomm = new Unterkommentar(userID2, kommID2, name2, kommentar2, kommID);
					// SQL Abfrage der KommentareZuUnterkommentare
					sql = sqlSt.getKommentareZuUnterkommentare();
					pStmt = con.prepareStatement(sql);
					pStmt.setInt(1, kommID2);
					ResultSet rs3 = pStmt.executeQuery();
					List<KommentarZuUnterkommentar> kzukommList = new ArrayList<KommentarZuUnterkommentar>();
					while (rs3.next()) {
						// KommentarZuUnterkommentar-Werte
						int userID3 = rs3.getInt(1);
						int kommID3 = rs3.getInt(2);
						String name3 = rs3.getString(3) + " " + rs3.getString(4);
						String kommentar3 = rs3.getString(5);
						kzukommList.add(new KommentarZuUnterkommentar(userID3, kommID3, name3, kommentar3, name2, userID2));
					}
					ukomm.setKommentarList(kzukommList);
					unterkommentarList.add(ukomm);
				}
				Kommentar komm = new Kommentar(userID, kommID, name, kommentar, unterkommentarList, beitragsID, userIDsession);
				kommentarList.add(komm);
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im BeitragServlet");
		}
		return kommentarList;
	}
	
	private void likeBeitrag(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		String page = request.getParameter("page");
		if (page == null) {
			page = "BeitragServlet?beitragsID=" + beitragsID;
		}
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde ge�ffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = sqlSt.getLike();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setInt(2, userID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			try {
				String sql = sqlSt.getEntferneLike();
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setInt(1, beitragsID);
				pStmt.setInt(2, userID);
				pStmt.executeUpdate();
			} catch (Exception e2) {
				System.out.println("SQL Fehler in BeitragServlet");
			}
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
			try {
				response.sendRedirect(page);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void kommentieren(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		String page = request.getParameter("page");
		if (page == null) {
			page = "BeitragServlet?beitragsID=" + beitragsID;
		}
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde ge�ffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = sqlSt.getKommentar();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.setString(2, request.getParameter("kommentar"));
			pStmt.setInt(3, userID);
			System.out.println(pStmt.toString());
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("SQL Fehler in BeitragServlet beim Kommentieren");
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
			try {
				response.sendRedirect(page);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void beitragLoeschen(HttpServletRequest request, HttpServletResponse response) {
		int beitragsID = Integer.parseInt(request.getParameter("beitragsID"));
		String page = request.getParameter("page");
		if (page == null) {
			page = "StartseiteServlet";
		}
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde ge�ffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = sqlSt.getLoeschenSql();
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("SQL Fehler in BeitragServlet");
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
			try {
				response.sendRedirect(page);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void kommentarLoeschen(HttpServletRequest request, HttpServletResponse response, String komm) {
		int kommID = Integer.parseInt(request.getParameter("id"));
		String page = request.getParameter("page");
		if (page == null) {
			page = "BeitragServlet?beitragsID=" + request.getParameter("beitragsID");
		}
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde ge�ffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = "";
			switch (komm) {
			case "kommentar":
				sql = sqlSt.getLoescheKommentar();
				break;
			case "komm":
				sql = sqlSt.getLoescheUnterkommentar();
				break;
			case "kzukomm":
				sql = sqlSt.getLoescheKommentarZuUnterkommentar();
				break;
			default:
				break;
			}
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, kommID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("SQL Fehler in BeitragServlet");
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
			try {
				response.sendRedirect(page);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void antwortVorbereiten(HttpServletRequest request, HttpServletResponse response, String komm) {
		request.setAttribute("anzeigen", true);
		request.setAttribute("tiefe", komm);
		request.setAttribute("kommID", request.getParameter("kommID"));
		request.setAttribute("name", null);
		try {
			request.getRequestDispatcher("BeitragServlet?anzeigen=true&beitragsID=" + request.getParameter("beitragsID")).forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void kommentarAntworten(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		int userID = Integer.parseInt(session.getAttribute("UserID").toString());
		String tiefe = request.getParameter("tiefe");
		int kommID = Integer.parseInt(request.getParameter("kommID"));
		String page = "BeitragServlet?beitragsID=" + request.getParameter("beitragsID");
		Connection con = new DBConnection().getCon();
		System.out.println("Verbindung wurde ge�ffnet (Beitrag)");
		BeitragSql sqlSt = new BeitragSql();
		try {
			String sql = "";
			switch (tiefe) {
			case "kommentar":
				sql = sqlSt.getInsertUnterkommentar();
				break;
			case "komm":
				sql = sqlSt.getInsertKommentarZuUnterkommentar();
				break;
			default:
				break;
			}
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, kommID);
			pStmt.setString(2, request.getParameter("kommentar"));
			pStmt.setInt(3, userID);
			pStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("SQL Fehler in BeitragServlet");
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
			try {
				response.sendRedirect(page);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
