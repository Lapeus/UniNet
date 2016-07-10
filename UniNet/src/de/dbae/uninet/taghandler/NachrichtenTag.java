package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.text.*;
import java.util.Date;

import de.dbae.uninet.javaClasses.Nachricht;

/**
 * Dieser Tag dient der Darstellung einer Nachricht.
 * Er wird auf der Nachrichten.jsp verwendet.
 * @author Marvin Wolf
 */
public class NachrichtenTag extends TagSupport {

	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private Nachricht nachricht;

	@Override
	public int doStartTag() throws JspException {
		Writer page = pageContext.getOut();
		try {
			page.append(getNachrichtenJSPCode());
		} catch (IOException e) {
			System.out.println("Da ist was bei der Nachricht kaputt");
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public Nachricht getNachricht() {
		return nachricht;
	}

	public void setNachricht(Nachricht nachricht) {
		this.nachricht = nachricht;
	}
	
	private String getNachrichtenJSPCode() {
		// Zeit aus Date holen TODO null wegen alten Testdaten darf eig nicht mehr vorkommen
		SimpleDateFormat f_Time = new SimpleDateFormat("HH:mm");
		// TODO Wenn Datum über einen Tag alt dann Datum Hinschreiben (vllt. auch Wochentag / "Gestern")
		SimpleDateFormat f_Date = new SimpleDateFormat("dd.MM.yy HH:mm");
		String zeitpunkt = "";
		// Falls keine Zeit angegeben ist
		Date messageDate = nachricht.getDate();
		if (messageDate != null) {
			// Milliseconds * Seconds * Minutes * Hours / Hier ein Tag
			long time24Hours = 1000 * 60 * 60 * 24;
			Date currentDate = new Date(System.currentTimeMillis());
			if (currentDate.getTime() - messageDate.getTime() >= time24Hours) {
				zeitpunkt = f_Date.format(messageDate);
			}
			else {
				zeitpunkt = f_Time.format(messageDate);
			}
		}
		String jsp = "";
		jsp += "<div class='media msg'>";
		jsp += "<a class='pull-left' href='#'>";
		jsp += "<img class='img-responsive profilbild' style='width: 32px; height: 32px;' alt='Profilbild' src='LadeProfilbildServlet?userID="+ nachricht.getSenderId() +"'>";
		jsp += "</a>";
		jsp += "<div class='media-body'>";
		jsp += "<small class='pull-right time'><i class='fa fa-clock-o'></i>"+ zeitpunkt +"</small>";
		jsp += "<h5 class='media-heading'>" + nachricht.getName() + "</h5>";
		jsp += "<small class='col-lg-10'>"+ nachricht.getNachrichtenText() +"</small>";
		jsp += "</div></div>";
		return jsp;
	}
}
