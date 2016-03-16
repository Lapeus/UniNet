package de.dbae.uninet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.org.apache.xml.internal.utils.NSInfo;

import de.dbae.uninet.dbConnections.DBConnection;
import de.dbae.uninet.javaClasses.Nachricht;
import de.dbae.uninet.sqlClasses.NachrichtenSql;

/**
 * Servlet implementation class NachrichtenServlet
 */
@WebServlet("/NachrichtenServlet")
public class NachrichtenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private HttpSession session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NachrichtenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		// Attribute setzen
		request.setAttribute("nachrichten", getNachrichten());
		request.getRequestDispatcher("Nachrichten.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		Connection con = null;
		NachrichtenSql nSql = new NachrichtenSql();
		if(request.getParameter("senden") != null){
			try {
				con = new DBConnection().getCon();
				System.out.println("Die Verbindung wurde aufgebaut (NachrichenServletPOST)");
				PreparedStatement pStmt = con.prepareStatement(nSql.nachrichtSenden());
				// WIEDER NUR FILLER ZUM TESTEN Integer.parseInt(session.getAttribute("UserID").toString())
				pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
				pStmt.setInt(2, 3);
				pStmt.setString(3, request.getParameter("nachricht"));
				pStmt.execute();
				doGet(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (con != null) {
						con.close();
						System.out.println("Die Verbindung wurde erfolgreich beendet! (NachrichtenServletPOST)");
					}
				} catch (Exception ignored) {
					ignored.printStackTrace();
				}
			}
		}
	}
	
	private List<Nachricht> getNachrichten() {
		Connection con = null;
		NachrichtenSql nSql = new NachrichtenSql();
		List<Nachricht> nachrichten = new ArrayList<>();
		
		try {
			con = new DBConnection().getCon();
			System.out.println("Verbindung wurde geöffnet (NachrichtenServletGET)");
			PreparedStatement pStmt = con.prepareStatement(nSql.getNachrichtenListe());
			// ONLY FOR TESTING FIX Integer.parseInt(session.getAttribute("UserID").toString())
			pStmt.setInt(1, Integer.parseInt(session.getAttribute("UserID").toString()));
			pStmt.setInt(2, 2);
			ResultSet result = pStmt.executeQuery();
			while (result.next()) {
				String name = result.getString(1) +" "+ result.getString(2);
				String nachricht = result.getString(3);
				nachrichten.add(new Nachricht(name, nachricht));
			}
			for (Nachricht nachricht : nachrichten) {
				nachricht.getName();
				nachricht.getNachrichtenText();
			}
		} catch (Exception e) {
			System.out.println("Fehler - getNachrichten");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
					System.out.println("Die Verbindung wurde erfolgreich beendet! (NachrichtenServletGET)");
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		return nachrichten;
	}

}
