package de.dbae.uninet.taghandler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Emoticon;

public class EmoticonTag extends TagSupport {

	private static final long serialVersionUID = -803336778178995908L;
	private List<Emoticon> listLinks;
	private List<Emoticon> listRechts;

	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	private String getHtmlCode() {
		String erg = "";
		erg += "<div class='row' style='background-color: white;'>";
		erg += "<div class='col-md-6'>";
		erg += "<table style='background-color: white;'>";
		erg += "<tr><th style='padding: 12px; color: white;'>Emoticon</th><th style='padding: 12px; color: white;'>Kürzel</th></tr>";
		for (Emoticon emo : listLinks) {
			erg += "<tr><td style='font-size: 18px; padding-left: 12px;'>" + emo.getBild() + "</td>";
			erg += "<td style='padding-left: 12px'>" + emo.getCode() + "</td></tr>";
		}
		erg += "</table></div>";		
		erg += "<div class='col-md-6'>";
		erg += "<table style='background-color: white;'>";
		erg += "<tr><th style='padding: 12px; color: white;'>Emoticon</th><th style='padding: 12px; color: white;'>Kürzel</th></tr>";
		for (Emoticon emo : listRechts) {
			erg += "<tr><td style='font-size: 18px; padding-left: 12px;'>" + emo.getBild() + "</td>";
			erg += "<td style='padding-left: 12px'>" + emo.getCode() + "</td></tr>";
		}
		erg += "</table></div>";				
		erg += "</div>";
		return erg;
	}

	public List<Emoticon> getListLinks() {
		return listLinks;
	}

	public void setListLinks(List<Emoticon> listLinks) {
		this.listLinks = listLinks;
	}

	public List<Emoticon> getListRechts() {
		return listRechts;
	}

	public void setListRechts(List<Emoticon> listRechts) {
		this.listRechts = listRechts;
	}
}
