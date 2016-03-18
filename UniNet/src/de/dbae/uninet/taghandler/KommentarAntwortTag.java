package de.dbae.uninet.taghandler;

import javax.servlet.jsp.tagext.TagSupport;

public class KommentarAntwortTag extends TagSupport {

	private static final long serialVersionUID = 8265706895063597544L;
	private boolean anzeigen;

	public int doStartTag() {
		if (anzeigen) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	public boolean isAnzeigen() {
		return anzeigen;
	}

	public void setAnzeigen(boolean anzeigen) {
		this.anzeigen = anzeigen;
	}
}
