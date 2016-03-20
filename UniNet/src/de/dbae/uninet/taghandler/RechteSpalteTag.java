package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.ChatFreund;

public class RechteSpalteTag extends TagSupport {

	private static final long serialVersionUID = 2224529992245088071L;
	private List<ChatFreund> chatfreunde;
	
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode(chatfreunde));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getHtmlCode(List<ChatFreund> chatfreunde) {
		String erg = "<div class='rechteSpalte'>";
		erg += "<ul class='nav nav-stacked chatSpalte'>";
		erg += "<li role='presentation'><H4 class='mittig'><b>Chat-Fenster (" + chatfreunde.size() + ")</b></H4></li><br>";
		erg += "<li><ul class='nav nav-pills nav-stacked'>";
		for (ChatFreund freund : chatfreunde) {
			String online = "";
			if (freund.isOnline()) {
				online = "<span class='glyphicon glyphicon-record pull-right' style='color: #00df00;'></span>";
			}
			erg += "<li role='presentation' class='chatfreunde'><a href='NachrichtenServlet?userID=" + freund.getUserID()+ "'>" + freund.getVorname() + " " + freund.getNachname() + online + "</a></li>";
		}
		erg += "</ul></ul></div>";
		return erg;
	}
	
	public List<ChatFreund> getChatfreunde() {
		return chatfreunde;
	}

	public void setChatfreunde(List<ChatFreund> chatfreunde) {
		this.chatfreunde = chatfreunde;
	}


}
