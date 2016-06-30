package de.dbae.uninet.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RespectBinding;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet implementation class AdminAnlegenServlet
 */
@WebServlet("/AdminAnlegenServlet")
public class AdminAnlegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection dbcon;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAnlegenServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("AdminAnlegen.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		String meldung = "";
		if (request.getParameter("registrieren") != null) {
			// REGISTRIERUNG
			
			// Alle eingaben abfragen
			String anrede = request.getParameter("anrede");
			boolean bAnrede = anrede.equals("Herr") ? true : false;
			String vorname = request.getParameter("vorname");
			String nachname = request.getParameter("nachname");
			String email = request.getParameter("email");	
			String uni = request.getParameter("uni");
			String password1 = request.getParameter("password1");
			String password2 = request.getParameter("password2");
			
			// Felder bei reload befuellen
			felderFuellen(request);
			
			if (!vorname.equals("") && !nachname.equals("") && !email.equals("") && !uni.equals("")
					&& !password1.equals("") && !password2.equals("")) {
				// ALLE DATEN VORHANDEN
				
				try {
					if (password1.equals(password2)) {
						// PASSWÖRTER GLEICH
						// Passwort hashen
						String hash = "";
						String salt = "";
						try {
							MessageDigest digest = MessageDigest.getInstance("MD5");
							Random random = new Random();
							byte[] salt2 = new byte[16];
							random.nextBytes(salt2);
							password1 += salt2;
							digest.update(password1.getBytes(), 0, password1.length());
							hash = new BigInteger(1, digest.digest()).toString();
							salt = salt2.toString();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// Nutzer Registrierung in Tabelle speichern
						PreparedStatement pStmtNutzer = con.prepareStatement(sqlSt.getRegistrierungNutzerSql());
						pStmtNutzer.setBoolean(1, bAnrede);
						pStmtNutzer.setString(2, vorname);
						pStmtNutzer.setString(3, nachname);
						pStmtNutzer.setString(4, email);
						pStmtNutzer.setString(5, hash);
						pStmtNutzer.setString(6, salt);
						pStmtNutzer.setInt(7, 2);
						pStmtNutzer.execute();
						// Statement für userid
						String stUserid = sqlSt.getNutzerId();
						PreparedStatement eins = con.prepareStatement(stUserid);
						eins.setString(1, email);
						// Statement für uniid
						String stUniid = sqlSt.getUniId();
						PreparedStatement zwei = con.prepareStatement(stUniid);
						zwei.setString(1, uni);
						// Execute userID
						ResultSet rsUserid = eins.executeQuery();
						rsUserid.next();
						String userid = rsUserid.getString(1);
						// Execute uniid
						ResultSet rsUniid = zwei.executeQuery();
						rsUniid.next();
						String uniid = rsUniid.getString(1);
						// Studentendaten aus Nutzerregistrierung in Tablle
						// speichern
						PreparedStatement pStmtAdmin = con.prepareStatement(sqlSt.getRegistrierungAdminSql());
						pStmtAdmin.setInt(1, Integer.parseInt(userid));
						pStmtAdmin.setInt(2, Integer.parseInt(uniid));
						pStmtAdmin.execute();
						HttpSession userSession = request.getSession();
						userSession.setAttribute("UserID", userid);
						meldung = "Lokaler Administrator wurde angelegt";
						request.getRequestDispatcher("AdminVerwaltung.jsp").forward(request, response);
					} else {
						// PASSWÖRTER UNGLEICH

						// Meldung bei ungleichem Passwort
						meldung = "Keine &Uuml;bereinstimmung - Geben das Passwort erneut ein";
						request.setAttribute("meldung", meldung);
					}
				} catch (Exception e) {
					// EMAIL SCHON REGISTRIERT
					
					e.printStackTrace();
					// Meldung bei gleicher E-Mail
					meldung = "E-Mail wird schon verwendet.";
					request.setAttribute("meldung", meldung);
				} finally {
					killConnection(con);
				}
			} else {
				// DATEN UNVOLLSTÄNDIG
				
				// Meldung bei unvollständiger Registrierung
				meldung = "Bitte f&uuml;llen Sie das Formular vollst&auml;ndig aus";
				request.setAttribute("meldung", meldung);
			}
		} else {
			// Felder bei reloead befuellen
			felderFuellen(request);
		}
		request.getRequestDispatcher("AdminAnlegen.jsp").forward(request, response);

	}
	private void killConnection(Connection con){
		try {
			if (con != null) {
				dbcon.close();
			}
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}
	
	private void felderFuellen(HttpServletRequest request) {
		// die Felder füllen wenn neu geladen wird
		String anrede = request.getParameter("anrede");
		boolean bAnrede = anrede.equals("Herr") ? true : false;
		String vorname = request.getParameter("vorname");
		String nachname = request.getParameter("nachname");
		String email = request.getParameter("email");
		if (bAnrede) {
			request.setAttribute("anredeM", new String("selected='selected'"));
		} else {
			request.setAttribute("anredeF", new String("selected='selected'"));
		}
		request.setAttribute("vorname", vorname);
		request.setAttribute("nachname", nachname);
		request.setAttribute("email", email);
	}


}
