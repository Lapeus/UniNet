package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class MittlereSpalteTag extends TagSupport {

	private static final long serialVersionUID = 9133244508825478468L;
	private Writer out;

	public int doStartTag() {
		out = pageContext.getOut();
		String erg = "";
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
		try {
			out.append(erg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
