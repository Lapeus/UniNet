package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class SeitenAufbauTag extends TagSupport {
	
	private static final long serialVersionUID = 929940257312046566L;
	private String userID;
	private Writer out;

	public int doStartTag() {
		out = pageContext.getOut();
		String erg = "";
		erg += new KopfzeileTag().getHtmlCode(pageContext.getServletContext());
		erg += "<div class='mainPart'>";
		erg += new LinkeSpalteTag().getHtmlCode();
		erg += "<div class='mittlereSpalte'><div class='row'><div class='col-md-1'></div><div class='col-md-10'>";
		try {
			out.append(erg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() {
		String erg = "";
		erg += "</div><div class='col-md-1'></div></div></div>";
		erg += new RechteSpalteTag().getHtmlCode(userID);
		try {
			out.append(erg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
