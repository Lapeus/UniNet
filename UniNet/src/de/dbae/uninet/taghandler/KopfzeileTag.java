package de.dbae.uninet.taghandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

public class KopfzeileTag extends TagSupport {

	private static final long serialVersionUID = -5650080610199722432L;
	
	public int doStartTag() {
		Writer out = pageContext.getOut();
		String kopfzeile = "";
		FileReader fr;
		try {
			fr = new FileReader(pageContext.getServletContext().getRealPath("DesignHTML//Kopfzeile.txt"));
			BufferedReader br = new BufferedReader(fr);
			String zeile;
			do {
				zeile = br.readLine();
				kopfzeile += zeile;
			} while (zeile != null);
			br.close();
			out.append(kopfzeile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

}
