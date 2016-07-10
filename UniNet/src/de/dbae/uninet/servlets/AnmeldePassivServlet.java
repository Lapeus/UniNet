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

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.sqlClasses.AnmeldeSql;

/**
 * Servlet stellt die passiven Daten fuer die Anmeldeseite zur Verfuegung.
 * @author Marvin Wolf
 */
@WebServlet("/AnmeldePassivServlet")
public class AnmeldePassivServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection dbcon;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnmeldePassivServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Stellt die Unis fuer die Auswahl der Unis bereit
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dbcon = new DBConnection();
		Connection con = dbcon.getCon();
		AnmeldeSql sqlSt = new AnmeldeSql();
		
		String aUni = request.getParameter("uni");
		List<String> unis = new ArrayList<>();
		try {
			PreparedStatement pStmt = con.prepareStatement(sqlSt.getUniList());
			ResultSet result = pStmt.executeQuery();
			ResultSetMetaData rsMetaData = result.getMetaData();
			while (result.next()) {
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					unis.add(result.getString(i));
				} 
			}
			unis.remove(aUni);
			unis.add(0, aUni);

			request.setAttribute("unis", unis);
		} catch (SQLException e) {
			System.out.println("Fehler - AnmmeldePassivServlet.doGet()");
		} catch (NullPointerException npe) {
			request.setAttribute("meldung", "Der Server ist nicht erreichbar");
		} finally {
			dbcon.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
