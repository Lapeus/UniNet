package de.dbae.uninet.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Beziehungsrechner;
import de.dbae.uninet.javaClasses.Semesterrechner;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet bedient die aktiven Anfragen der Anmeldeseite.
 * 
 * @author Marvin Wolf
 */
@WebServlet("/AnmeldeAktivServlet")
public class AnmeldeAktivServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection dbcon;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnmeldeAktivServlet() {
		super();
	}

	/**
	 * Updated die Studiengänge, wenn die Person eine Uni ausgewählt hat.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		felderFuellen(request);
		String uni = request.getParameter("uni");
		updateStudiengaenge(request, response, con, sqlSt, uni);
	}

	/**
	 * Verarbeitet den Anmeldevorgang und Leitet an die Startseite weiter.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		String meldung = "";
		String meldung1 = "";

		if (request.getParameter("anmelden") != null) {
			// ANMELDEN
			try {
				String password = request.getParameter("password");
				String email = request.getParameter("email");

				// Statement für userid
				String stUserid = sqlSt.getNutzerId();
				PreparedStatement eins = con.prepareStatement(stUserid);
				eins.setString(1, email);
				// Execute userID
				ResultSet rsUserid = eins.executeQuery();
				String userid = "";
				if (rsUserid.next()) {
					userid = rsUserid.getString(1);
				}

				// Statement für email und PW kontrolle
				String sql = sqlSt.ueberpruefeAnmeldedaten();
				PreparedStatement pStmt = con.prepareStatement(sql);
				pStmt.setString(1, email);
				ResultSet rs = pStmt.executeQuery();

				if (!rs.next()) {
					// Wenn die Anmeldedaten nicht in der DB sind
					meldung1 = "Ihre E-Mail-Adresse konnte nicht zugeordnet werden";
					request.setAttribute("meldung", meldung1);
					request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
				} else {
					// Wenn die Anmeldedaten in der DB sind
					// Teste, ob Passwort passt
					String hash = "";
					String password1 = password + rs.getString(2);
					try {
						MessageDigest digest = MessageDigest.getInstance("MD5");
						digest.update(password1.getBytes(), 0, password1.length());
						hash = new BigInteger(1, digest.digest()).toString();
					} catch (NoSuchAlgorithmException e) {
						System.out.println("Passwort Hash Fehler");
						e.printStackTrace();
					}
					// Teste ob die Passwörter übereinstimmen
					if (!hash.equals(rs.getString(1))) {
						meldung1 = "Es wurde ein falsches Passwort eingegeben!";
						request.setAttribute("meldung", meldung1);
						request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
					} else {
						// Setze den User auf Online
						sql = sqlSt.getOnlineUpdate();
						pStmt = con.prepareStatement(sql);
						pStmt.setInt(1, Integer.parseInt(userid));
						pStmt.executeUpdate();
						// Setze die UserID fuer das SessionTracking
						HttpSession userSession = request.getSession();
						userSession.setAttribute("UserID", userid);
						// Setzt fuer den Fall, dass kein Profilbild eingestellt
						// wurde, das Default-Profilbild
						defaultProfilbildSetzen(userid, con);
						// Teste ob es sich um einen Studenten oder LocalAdmin
						// handelt
						sql = sqlSt.getStudentenIDs();
						pStmt = con.prepareStatement(sql);
						rs = pStmt.executeQuery();
						List<String> studentenIDs = new ArrayList<String>();
						while (rs.next()) {
							studentenIDs.add(rs.getString(1));
						}
						sql = sqlSt.getNutzerTyp();
						pStmt = con.prepareStatement(sql);
						pStmt.setString(1, email);
						rs = pStmt.executeQuery();
						int nutzertyp = 4;
						while (rs.next()) {
							nutzertyp = rs.getInt(1);
						}
						// Wenn es ein Student ist, leite an Startseite weiter
						/*
						 * if (studentenIDs.contains(userid)) { //
						 * Freundesbewertung fuer diesen Nutzer aktualisieren
						 * new
						 * Beziehungsrechner().setBeziehung(Integer.parseInt(
						 * userid)); response.sendRedirect("StartseiteServlet");
						 * // Sonst an die Admin Verwaltung
						 */
						if (nutzertyp == 1) {
							response.sendRedirect("AdminServlet");
						} else if (nutzertyp == 2) {
							response.sendRedirect("AdminBeitraegeServlet");
						} else {
							// Wenn es kein Admin ist, leite an Startseite
							// weiter
							// Freundesbewertung fuer diesen Nutzer
							// aktualisieren
							new Beziehungsrechner().setBeziehung(Integer.parseInt(userid));
							response.sendRedirect("StartseiteServlet");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				killConnection(con);
			}
		} else if (request.getParameter("registrieren") != null) {
			// REGISTRIERUNG

			// Alle eingaben abfragen
			String anrede = request.getParameter("anrede");
			boolean bAnrede = anrede.equals("Herr") ? true : false;
			String vorname = request.getParameter("vorname");
			String nachname = request.getParameter("nachname");
			String email = request.getParameter("email");
			String uni = request.getParameter("uni");
			String studiengang = request.getParameter("studiengang");
			int semester = Integer.parseInt(request.getParameter("semester"));
			String password1 = request.getParameter("password1");
			String password2 = request.getParameter("password2");

			// Felder bei reloead befuellen
			felderFuellen(request);

			if (!vorname.equals("") && !nachname.equals("") && isCorrectEmail(email) && !uni.equals("")
					&& !password1.equals("") && !password2.equals("") && !isVorhanden(email)) {
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
							System.out.println("ERROR - AnmeldeAktivServlet - Passwort hashen");
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
						pStmtNutzer.setInt(7, 3);
						pStmtNutzer.execute();
						// Statement für userid
						String stUserid = sqlSt.getNutzerId();
						PreparedStatement eins = con.prepareStatement(stUserid);
						eins.setString(1, email);
						// Statement für uniid
						String stUniid = sqlSt.getUniId();
						PreparedStatement zwei = con.prepareStatement(stUniid);
						zwei.setString(1, uni);
						// Statement für studiengangid
						String stStudiengangid = sqlSt.getStudiengangId();
						PreparedStatement drei = con.prepareStatement(stStudiengangid);
						drei.setString(1, studiengang);
						// Execute userID
						ResultSet rsUserid = eins.executeQuery();
						rsUserid.next();
						String userid = rsUserid.getString(1);
						// Execute uniid
						ResultSet rsUniid = zwei.executeQuery();
						rsUniid.next();
						String uniid = rsUniid.getString(1);
						// Execute studiengangsid
						ResultSet rsStudiengangid = drei.executeQuery();
						rsStudiengangid.next();
						String studiengangid = rsStudiengangid.getString(1);
						// Studentendaten aus Nutzerregistrierung in Tablle
						// speichern
						PreparedStatement psTmtStudent = con.prepareStatement(sqlSt.getRegistrierungStudentSql());
						psTmtStudent.setInt(1, Integer.parseInt(userid));
						psTmtStudent.setInt(2, Integer.parseInt(uniid));
						psTmtStudent.setInt(3, Integer.parseInt(studiengangid));
						psTmtStudent.setDate(4, new Date(new Semesterrechner().getStudienbeginn(semester)));
						psTmtStudent.execute();
						// Default-Profilsichtbarkeiten setzen
						psTmtStudent = con.prepareStatement(sqlSt.getRegistrierungStudentSql2());
						psTmtStudent.setInt(1, Integer.parseInt(userid));
						psTmtStudent.execute();
						// Freundschaft mit sich selbst eintragen
						PreparedStatement psTmtFreund = con
								.prepareStatement("INSERT INTO freunde VALUES (?, ?, 1000, 1000)");
						psTmtFreund.setInt(1, Integer.parseInt(userid));
						psTmtFreund.setInt(2, Integer.parseInt(userid));
						psTmtFreund.executeUpdate();
						// Setzt fuer den Fall, dass kein Profilbild eingestellt
						// wurde, das Default-Profilbild
						defaultProfilbildSetzen(userid, con);

						HttpSession userSession = request.getSession();
						userSession.setAttribute("UserID", userid);
						response.sendRedirect("StartseiteServlet");
					} else {
						// PASSWÖRTER UNGLEICH

						// Meldung bei ungleichem Passwort
						meldung = "Keine &Uuml;bereinstimmung - Geben das Passwort erneut ein";
						request.setAttribute("meldung", meldung);
						// Studiengaenge updaten
						updateStudiengaenge(request, response, con, sqlSt, uni);
					}
				} catch (Exception e) {
					// EMAIL SCHON REGISTRIERT

					e.printStackTrace();
					// Meldung bei gleicher E-Mail
					meldung = "E-Mail wird schon verwendet.";
					request.setAttribute("meldung", meldung);
					// Studiengaenge updaten
					updateStudiengaenge(request, response, con, sqlSt, uni);
				} finally {
					killConnection(con);
				}
			} else {
				// DATEN UNVOLLSTÄNDIG
				
				// E-Mail ist ungültig
				if (!isCorrectEmail(email)) {
					meldung = "Bitte geben Sie eine gültige E-Mail-Adresse an!";
				}
				// E-Mail ist gebannt oder schon vorhanden
				else if (isVorhanden(email)) {
					meldung = "Die angebenene E-Mail-Adresse wird bereits verwendet oder wurde blockiert!";
				}
				// Meldung bei unvollständiger Registrierung
				else {
					meldung = "Bitte f&uuml;llen Sie das Formular vollst&auml;ndig aus";
				}
				request.setAttribute("meldung", meldung);
				// Studiengaenge updaten
				updateStudiengaenge(request, response, con, sqlSt, uni);
			}
		} else {
			// Felder bei reloead befuellen
			felderFuellen(request);
			String uni = request.getParameter("uni");
			updateStudiengaenge(request, response, con, sqlSt, uni);
		}
	}

	/**
	 * Prüft den übergebenen String auf ein gültiges E-Mail-Format
	 */
	private boolean isCorrectEmail(String email) {
		boolean isCorrect = false;
		// Email abfrage mit Regex
		Pattern pattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isCorrect = true;
		}
		return isCorrect;
	}

	private boolean isVorhanden(String email) {
		Connection con = dbcon.getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		boolean isVorhanden = true;
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.isVorhandenOrGespert());
			pStmt.setString(1, email);
			pStmt.setString(2, email);
			ResultSet rs = pStmt.executeQuery();
			if (!rs.next()) {
				isVorhanden = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isVorhanden;
	}

	/**
	 * Beendet die übergebene Connection
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

	/**
	 * Füllt die schon angegebenen Felder nach einem Update
	 */
	private void felderFuellen(HttpServletRequest request) {
		// und in die Felder füllen wenn neu geladen
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

	/**
	 * Updated die Studiengänge, wenn die Person eine Uni ausgewählt hat.
	 */
	private void updateStudiengaenge(HttpServletRequest request, HttpServletResponse response, Connection con,
			AnmeldeSql sqlSt, String uni) throws ServletException, IOException {
		List<String> studiengaenge = new ArrayList<String>();
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getStudiengaenge());
			// Ausgewählte Uni setzen
			pStmt.setString(1, uni);
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			// Alle Studiengänge in Liste schreiben
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					studiengaenge.add(result.getString(i));
				}
			}
			request.setAttribute("studiengaenge", studiengaenge);
			request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SQL Fehler - updateStudiengaenge()");
			// Wieder auf die Anmeldeseite verweisen
			request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
		} finally {
			killConnection(con);
		}
	}

	/**
	 * Setzt für den neue angelegten Nutzer das Standard-Profilbild
	 */
	private void defaultProfilbildSetzen(String userid, Connection con) {
		// Profilbild-Default setzen
		// Select-Statement fuer alle Nutzer
		String sql = "SELECT profilbild, userID FROM nutzer";
		try {
			PreparedStatement pStmt = con.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			// Lade die DefaultProfilbilder
			List<File> files = new ArrayList<File>();
			files = Arrays.asList(new File(getServletContext().getRealPath("Pictures")).listFiles());
			// Fuer alle Nutzer
			while (rs.next()) {
				// Wenn kein Profilbild gesetzt wurde
				if (rs.getBytes(1) == null) {
					// Lade eines der DefaulProfilBilder
					int rand = (int) (Math.random() * files.size());
					BufferedImage img = ImageIO.read(files.get(rand));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(img, "jpg", baos);
					// Und schreibe es in die DB
					sql = "UPDATE nutzer SET profilbild = ? WHERE userID = ?";
					pStmt = con.prepareStatement(sql);
					pStmt.setBytes(1, baos.toByteArray());
					pStmt.setInt(2, rs.getInt(2));
					pStmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - AnmeldeAktivServelet - defaultProfilbilderSetzen");
			e.printStackTrace();
		}
	}

}
