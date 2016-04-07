package de.dbae.uninet.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FehlerServlet
 */
@WebServlet("/FehlerServlet")
public class FehlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FehlerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fehler = request.getParameter("fehler");
		if (fehler == null) {
			fehler = "";
		}
		String meldung = "Es ist ein unerwarteter Fehler aufgetreten.";
		String page = "Anmeldung.jsp";
		switch (fehler) {
		case "DBCon":
			meldung = "Leider konnte keine Verbindung zur Datenbank aufgebaut werden. Bitte versuchen Sie es später erneut.";
			break;
		case "Session":
			meldung = "Ihre aktuelle Sitzung ist abgelaufen. Aus Sicherheitsgründen wurden Sie ausgeloggt.";
			break;
		default:
			break;
		}
		request.setAttribute("page", page);
		request.setAttribute("meldung", meldung);
		request.getRequestDispatcher("Fehler.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
