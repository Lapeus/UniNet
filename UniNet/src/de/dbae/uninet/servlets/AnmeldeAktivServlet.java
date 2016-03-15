package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet implementation class StudiengaengeServlet
 */
@WebServlet("/AnmeldeAktivServlet")
public class AnmeldeAktivServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnmeldeAktivServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Leer
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		String meldung = "";
		String meldung1 = "";

		if (request.getParameter("anmelden") != null) {
			// wenn der AnmeldeButton gedrückt wurde

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
				pStmt.setString(2, password);
				ResultSet rs = pStmt.executeQuery();

				if (!rs.next()) {
					// Wenn die Anmeldedaten nicht in der DB sind
					
					meldung1 = "Bitte überprüfen Sie ihre Anmeldedaten";
					request.setAttribute("meldung", meldung1);
					request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
				} else {
					// Wenn die Anmeldedaten in der DB sind
					
					HttpSession userSession = request.getSession();
					System.out.println("SessionID: " + userSession.getId());
					userSession.setAttribute("UserID", userid);
					request.getRequestDispatcher("/StartseiteServlet").forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					killConnection(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			// Alle eingaben abfragen
			String anrede = request.getParameter("anrede");
			boolean bAnrede = anrede.equals("Herr") ? true : false;
			String vorname = request.getParameter("vorname");
			String nachname = request.getParameter("nachname");
			String email = request.getParameter("email");
			String uni = request.getParameter("uni");
			String studiengang = request.getParameter("studiengang");
			String password1 = request.getParameter("password1");
			String password2 = request.getParameter("password2");

			// und in die Felder füllen wenn neu geladen
			if (bAnrede) {
				request.setAttribute("anredeM", new String("selected='selected'"));
			} else {
				request.setAttribute("anredeF", new String("selected='selected'"));
			}

			request.setAttribute("vorname", vorname);
			request.setAttribute("nachname", nachname);
			request.setAttribute("email", email);

			if (request.getParameter("laden") != null) {
				// Wenn der Laden Button gedrückt wurde

				updateStudiengaenge(request, response, con, sqlSt, uni);
			} else if (!vorname.equals("") && !nachname.equals("") && !email.equals("") && !uni.equals("")
					&& !password1.equals("") && !password2.equals("")) {
				// Wenn alle Daten angegeben sind

				try {
					if (password1.equals(password2)) {
						// Wenn die Passwörter übereinstimmen

						// Nutzer Registrierung in Tabbelle speichern
						System.out.println("1");
						PreparedStatement pStmtNutzer = con.prepareStatement(sqlSt.getRegistrierungNutzerSql());
						System.out.println("2");
						pStmtNutzer.setBoolean(1, bAnrede);
						pStmtNutzer.setString(2, vorname);
						pStmtNutzer.setString(3, nachname);
						pStmtNutzer.setString(4, email);
						pStmtNutzer.setString(5, password1);
						System.out.println("3");
						System.out.println(pStmtNutzer.toString());
						pStmtNutzer.execute();
						// Statement für userid
						String stUserid = sqlSt.getNutzerId();
						PreparedStatement eins = con.prepareStatement(stUserid);   
						eins.setString(1,email);
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
						// Studentendaten aus Nutzerregistrieung in Tablle
						// speichern
						PreparedStatement psTmtStudent = con.
								prepareStatement(sqlSt.getRegistrierungStudentSql());
						psTmtStudent.setInt(1, Integer.parseInt(userid));
						psTmtStudent.setInt(2, Integer.parseInt(uniid));
						psTmtStudent.setInt(3, Integer.parseInt(studiengangid));
						psTmtStudent.execute();
						HttpSession userSession = request.getSession();
						userSession.setAttribute("UserID", userid);
						request.getRequestDispatcher("/StartseiteServlet").forward(request, response);
					} else {
						// Wenn die Passwörter nich übereinstimmen

						// Meldung bei gleicher E-Mail´
						meldung = "Keine übereinstimmung - Geben das Passwort erneut ein";
						request.setAttribute("meldung", meldung);
						updateStudiengaenge(request, response, con, sqlSt, uni);
					}

				} catch (Exception e) {
					// Wenn die E-Mail schon vorhanden ist
					e.printStackTrace();
					// Meldung bei gleicher E-Mail´
					meldung = "E-Mail wird schon verwendet.";
					request.setAttribute("meldung", meldung);
					updateStudiengaenge(request, response, con, sqlSt, uni);
				} finally {
					try {
						killConnection(con);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				// Wenn die Daten noch nicht vollständig angegeben sind

				meldung = "Bitte füllen Sie das Formular vollständig aus";
				request.setAttribute("meldung", meldung);
				updateStudiengaenge(request, response, con, sqlSt, uni);
			}
		}
	}

	private void killConnection(Connection con) throws Exception {
		try {
			if (con != null) {
				con.close();
				System.out.println("Die Verbindung wurde erfolgreich beendet!");
			}
		} catch (SQLException ignored) {
		}
	}

	private void updateStudiengaenge(HttpServletRequest request, HttpServletResponse response, Connection con,
			AnmeldeSql sqlSt, String uni) throws ServletException, IOException {
		List<String> studiengaenge = new ArrayList<String>();
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getStudiengaenge());
			pStmt.setString(1, uni);
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					studiengaenge.add(result.getString(i));
				}
			}
			request.setAttribute("studiengaenge", studiengaenge);
			request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
		} catch (SQLException e) {
			System.out.println("SQL Fehler - AnmeldeSQL.getStudiengaenge()");
			request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
		} finally {
			try {
				killConnection(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
