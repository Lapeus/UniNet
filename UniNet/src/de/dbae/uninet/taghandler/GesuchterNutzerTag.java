package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Student;

public class GesuchterNutzerTag extends TagSupport {
	/**
	 * SERIAL ID
	 */
	private static final long serialVersionUID = -4082131407797643462L;

	private Student student;

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

	public Student getUser() {
		return student;
	}

	public void setUser(Student student) {
		this.student = student;
	}
	
	private String getUserJSPCode() {
		String jsp = "<article class='search-result row'>"
				+ "<div class='col-xs-12 col-sm-12 col-md-3'>"
				+ "<a href='#' title='Lorem ipsum' class='thumbnail'><img alt='Profilbild' src='LadeProfilbildServlet?userID="+ student.getUserID() +"'/></a>"
				+ "</div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-2'><ul class='meta-search'><li><i class='glyphicon glyphicon-calendar'></i> <span>02/15/2014</span></li><li><i class='glyphicon glyphicon-time'></i> <span>4:28 pm</span></li><li><i class='glyphicon glyphicon-tags'></i> <span>People</span></li></ul></div>"
				+ "<div class='col-xs-12 col-sm-12 col-md-7 excerpet'>"
				+ "<h3><a href='ProfilServlet?userID="+ student.getUserID() +"' title=''>"+ student.getVorname() + " "  + student.getNachname() +"</a></h3>"
				+ "<p>" + student.getUserID() + "</p>"
				+ "</div>"
				+ "<span class='clearfix borda'></span>"
				+ "</article>";
		return jsp;
	}
}
