package de.dbae.uninet.javaClasses;

public class Emoticon {

	private String code;
	private String bild;
	
	public Emoticon(String code, String bild) {
		this.setCode(code);
		this.setBild(bild);
	}

	public String getBild() {
		return bild;
	}

	public void setBild(String bild) {
		this.bild = bild;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
