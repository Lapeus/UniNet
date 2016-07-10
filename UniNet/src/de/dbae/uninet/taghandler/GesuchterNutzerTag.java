package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.GesuchterNutzer;

public class GesuchterNutzerTag extends TagSupport {
	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private GesuchterNutzer student;

	@Override
	public int doStartTag() throws JspException {
		Writer page = pageContext.getOut();
		try {
			page.append(getUserJSPCode());
		} catch (IOException e) {
			System.out.println("GesucheterNutzerTag - doStartTag");
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public GesuchterNutzer getUser() {
		return student;
	}

	public void setUser(GesuchterNutzer student) {
		this.student = student;
	}
	
	private String getUserJSPCode() {
		// Online Status	
		String onlineStatus = "";
		if (student.isOnline()) {
			onlineStatus = "#00aa00;'>online";
		}
		else {
			onlineStatus = "red;'>offline";
		}
		
		// Geburtsdatum
		String sGeburt = "Keine Angaben";
		if (student.getGeburtsdatum() != null) {
			SimpleDateFormat tst = new SimpleDateFormat("dd.MM.yyyy");
			Date tst2 = student.getGeburtsdatum();
			sGeburt = tst.format(tst2);
		}
		
		// Freunschaftsanfrage
		String sFreundschaft = "FILLER";
		if (student.isFreund()) {
			sFreundschaft = "Ihr seid befreundet";
		}
		else {
			sFreundschaft = "<p>Ihr seid noch nicht befreundet</p>"
					+ "<span>"
					+ "<button class='button btn-danger' type='submit' name='freundID' value='"+ student.getUserID() +"'>Freunschaftsanfrage senden</button>"
					+ "</span>";
		}
		
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ "<a href='ProfilServlet?userID=" + student.getUserID() + "' title='Profil' class='thumbnail'><img alt='Profilbild' src='LadeProfilbildServlet?userID="+ student.getUserID() +"'/></a>"
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'>"
				+ "<ul class='meta-search'>"
				+ "<li><i class='glyphicon glyphicon-calendar'></i> <span>"+ sGeburt +"</span></li>"
				+ "<li><i class='glyphicon glyphicon-off'></i> <span style='color:"+ onlineStatus +"</span</li>"
				+ "<li><i class='glyphicon glyphicon-tags'></i> <span>Nutzer</span></li>"
				+ "</ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<h3><a href='ProfilServlet?userID="+ student.getUserID() +"' title=''>"+ student.getVorname() + " "  + student.getNachname() +"</a></h3>"
				+ sFreundschaft
				+ "</div>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";
		return jsp;
	}
}
