package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class RechteSpalteTag extends TagSupport {

	private static final long serialVersionUID = 2224529992245088071L;
	private String userID;
	
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode(userID));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getHtmlCode(String userID) {
		String erg = "<div class='rechteSpalte'>";
		erg += "<ul class='nav nav-stacked chatSpalte'>";
		erg += "<li role='presentation'><H4 class='mittig'><b>Chat-Fenster</b></H4></li><br>";
		erg += "<li><ul class='nav nav-pills nav-stacked'>";
			// Hier folgen jetzt alle Chat-Partner ueber userID jeweils in dieser Form:
			//<li role="presentation"><a href="#">Freund1</a></li>
		erg += "</ul></ul></div>";
		return erg;
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
