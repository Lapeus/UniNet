package de.dbae.uninet.servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.BeitragSql;
import de.dbae.uninet.sqlClasses.ProfilSql;

/**
 * Servlet implementation class ProfilServlet
 */
@WebServlet("/ProfilServlet")
public class ProfilServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
	private Connection con;
	private ProfilSql sqlSt = new ProfilSql();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Chatfreunde
		new LadeChatFreundeServlet().setChatfreunde(request);
				
		session = request.getSession();
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		String user = request.getParameter("userID");
		boolean eigenesProfil = false;
		if (user == null) {
			user = session.getAttribute("UserID").toString();
			eigenesProfil = true;
		}
		request.setAttribute("userID", user);
		request.setAttribute("beitragPosten", eigenesProfil);
		int userID = Integer.parseInt(user);
		try {
			request.setAttribute("beitragList", new BeitragServlet().getBeitraege(request, con, "Profilseite", userID));
			// Infos
			String sql = sqlSt.getSqlStatement("Infos");
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				String name = rs.getString(1) + " " + rs.getString(2);
				request.setAttribute("name", name);
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
				if (name.equals("Jurassic Park")) {
					soundAbspielen();
				}
			}
			sql = sqlSt.getSqlStatement("AnzahlFreunde");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, userID);
			rs = pStmt.executeQuery();
			if (rs.next()) {
				request.setAttribute("anzFreunde", rs.getInt(1));
			}
			// Weiterleitung
			request.getRequestDispatcher("Profil.jsp").forward(request, response);
		} catch (Exception e) {
			System.out.println("SQL Fehler im ProfilServlet");
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
	}
	
	private void soundAbspielen() {
		try {
            AudioInputStream ais = AudioSystem
                .getAudioInputStream(
                new File("C:\\Users\\chris_000\\Dropbox\\IMIT\\Jurassic Park.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		DBConnection dbcon = new DBConnection();
		con = dbcon.getCon();
		try {
			switch (name) {
			case "BeitragPosten":
				posteBeitrag(request, response);
				break;
			default:
				doGet(request, response);
				break;
			}
		} catch (Exception e) {
			System.out.println("SQL Fehler im ProfilServlet");
			e.printStackTrace();
		} finally {
			dbcon.close();
		}
	}
	
	private void posteBeitrag(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		session = request.getSession();
		BeitragSql sqlSt = new BeitragSql();
		String beitrag = request.getAttribute("beitrag").toString();
		int verfasserID = Integer.parseInt(session.getAttribute("UserID").toString());
		String sichtbarkeit = request.getParameter("sichtbarkeit");
		boolean sichtbar = true;
		if (sichtbarkeit.startsWith("Privat")) {
			sichtbar = false;
		}
		// Beitrag anlegen
		String sql = sqlSt.getSqlStatement("BeitragAnlegen1");
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, beitrag);
		pStmt.setInt(2, verfasserID);
		pStmt.setBoolean(3, sichtbar);
		pStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
		pStmt.setTime(5, new java.sql.Time(System.currentTimeMillis()));
		pStmt.executeUpdate();
		
		// BeitragsID abfragen
		sql = sqlSt.getSqlStatement("BeitragAnlegen2");
		pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		int beitragsID;
		if (rs.next()) {
			beitragsID = rs.getInt(1);
			// Chronikbeitrag eintragen
			sql = sqlSt.getSqlStatement("BeitragAnlegenChronik");
			pStmt = con.prepareStatement(sql);
			pStmt.setInt(1, beitragsID);
			pStmt.executeUpdate();
			String page = "ProfilServlet";
			if (request.getParameter("page") != null) {
				page = request.getParameter("page");
			}
			response.sendRedirect(page);
		} else {
			System.out.println("Problem beim Anlegen des Beitrags");
		}
	}
	
	private String getInfoString(String info) {
		return "<li class='list-group-item'>" + info + "</li>";
	}

}
