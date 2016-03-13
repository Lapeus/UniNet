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

import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;

import com.sun.rmi.rmid.ExecOptionPermission;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Leer
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = new DBConnection().getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		String meldung = "";
		String meldung1 = "";

		if (request.getParameter("anmelden") != null){
			// wenn der AnmeldeButton gedrückt wurde
			
			try {
				String password = request.getParameter("password");
				String email = request.getParameter("email");
				
				String sql = sqlSt.ueberpruefeAnmeldedaten(email, password);
				PreparedStatement pStmt = con.prepareStatement(sql);
				ResultSet rs = pStmt.executeQuery();
				
				if (!rs.next()) {
					meldung1 = "Bitte überprüfen Sie ihre Anmeldedaten";
					request.setAttribute("meldung",meldung1);
					request.getRequestDispatcher("Anmeldung.jsp").forward(request, response);
				} else {
					HttpSession userSession = request.getSession();
					userSession.setAttribute("UserID", sqlSt.getNutzerId(email));
					request.getRequestDispatcher("Startseite.jsp").forward(request, response);
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
			}else {	
				request.setAttribute("anredeF", new String("selected='selected'"));
			}
			
			request.setAttribute("vorname", vorname);
			request.setAttribute("nachname", nachname);
			request.setAttribute("email", email);
			
			if (request.getParameter("laden") != null) {
			// Wenn der Laden Button gedrückt wurde
			
			updateStudiengaenge(request, response, con, sqlSt, uni);
			} else if (!vorname.equals("") && !nachname.equals("") && !email.equals("") && !uni.equals("") && !password1.equals("") && !password2.equals("")) {
				// Wenn alle Daten angegeben sind
				
				try {
					if(password1.equals(password2)) {
						// Wenn die Passwörter übereinstimmen 
						
						// Nutzer Registrierung in Tabbelle speichern
						PreparedStatement pStmtNutzer = con.prepareStatement(sqlSt.getRegistrierungNutzerSql(bAnrede, vorname, nachname, email, password1));
						pStmtNutzer.execute();
						
						// Statement für userid
						String stUserid = sqlSt.getNutzerId(email);
						PreparedStatement eins = con.prepareStatement(stUserid);
						// Statement für uniid
						String stUniid = sqlSt.getUniId(uni);
						PreparedStatement zwei = con.prepareStatement(stUniid);
						// Statement für studiengangid
						String stStudiengangid = sqlSt.getStudiengangId(studiengang);
						PreparedStatement drei = con.prepareStatement(stStudiengangid);
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
						// Studentendaten aus Nutzerregistrieung in Tablle speichern
						PreparedStatement psTmtStudent = con.prepareStatement(sqlSt.getRegistrierungStudentSql(userid, uniid, studiengangid));
						System.out.println(pStmtNutzer.toString());
						
						psTmtStudent.execute();
						HttpSession userSession = request.getSession();
						userSession.setAttribute("UserID", sqlSt.getNutzerId(email));

						request.getRequestDispatcher("Startseite.jsp").forward(request, response);
					} else {
						// Wenn die Passwörter nich übereinstimmen
						
						// Meldung bei gleicher E-Mail´
						meldung = "Keine übereinstimmung - Geben das Passwort erneut ein";
						request.setAttribute("meldung",meldung);
						updateStudiengaenge(request, response, con, sqlSt, uni);
					}
		
				} catch (Exception e) {
					// Wenn die E-Mail schon vorhanden ist
					
					// Meldung bei gleicher E-Mail´
					meldung = "E-Mail wird schon verwendet.";
					request.setAttribute("meldung",meldung);
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
				request.setAttribute("meldung",meldung);
				updateStudiengaenge(request, response, con, sqlSt, uni);
			}
		}
	}
	
	private void killConnection (Connection con) throws Exception {
		try {
			if (con!=null) {
				con.close();
				System.out.println("Die Verbindung wurde erfolgreich beendet!");
			}
		} catch (SQLException ignored) {}
	}
	
	private void updateStudiengaenge(HttpServletRequest request, HttpServletResponse response, Connection con, AnmeldeSql sqlSt, String uni) throws ServletException, IOException {
		List<String> studiengaenge = new ArrayList<String>();
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getStudiengaenge(uni));
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
			System.out.println("SQL Fehler - AnmeldeSQL.getStudiengaenge(uni)");
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
