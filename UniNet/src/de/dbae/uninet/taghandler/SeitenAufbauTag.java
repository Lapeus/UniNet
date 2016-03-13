package de.dbae.uninet.taghandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class SeitenAufbauTag extends TagSupport {
	
	private static final long serialVersionUID = 929940257312046566L;

	public int doStartTag() {
		Writer out = pageContext.getOut();
		String kopfzeile = "";
		FileReader fr;
		try {
			fr = new FileReader(pageContext.getServletContext().getRealPath("DesignHTML//SeitenAufbau1.txt"));
			BufferedReader br = new BufferedReader(fr);
			String zeile = br.readLine();
			while (zeile != null) {
				kopfzeile += zeile;
				zeile = br.readLine();
			}
			br.close();
			out.append(kopfzeile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() {
		Writer out = pageContext.getOut();
		String kopfzeile = "";
		FileReader fr;
		try {
			fr = new FileReader(pageContext.getServletContext().getRealPath("DesignHTML//SeitenAufbau2.txt"));
			BufferedReader br = new BufferedReader(fr);
			String zeile = br.readLine();
			while (zeile != null) {
				kopfzeile += zeile;
				zeile = br.readLine();
			}
			br.close();
			out.append(kopfzeile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
