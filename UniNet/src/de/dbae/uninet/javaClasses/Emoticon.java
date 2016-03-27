package de.dbae.uninet.javaClasses;

/**
 * Diese Klasse stellt den Unicode f&uuml;r die Emoticons und ihr jeweiliges K&uuml;rzel, 
 * um sie in einem Text zu verwenden, zur Verf&uuml;gung.<br>
 * Verwendet wird sie nur im EmoticonFilter, im EmoticonTag und im EmoticonServlet.
 * @author Christian Ackermann
 */
public class Emoticon {

	/**
	 * Das K&uuml;rzel, um das Emoticon zu verwenden.
	 */
	private String code;
	
	/**
	 * Der Unicode f&uuml;r das jeweilige Emoticon.
	 */
	private String bild;
	
	/**
	 * Erstellt ein neues Emoticon.<br>
	 * Einziger Konstruktor dieser Klasse.
	 * @param code Das K&uuml;rzel
	 * @param bild Der Unicode
	 */
	public Emoticon(String code, String bild) {
		this.setCode(code);
		this.setBild(bild);
	}

	/**
	 * Getter f&uuml;r den Unicode.
	 * @return Der Unicode
	 */
	public String getBild() {
		return bild;
	}

	/**
	 * Setter f&uuml;r den Unicode.
	 * @param bild Der Unicode
	 */
	public void setBild(String bild) {
		this.bild = bild;
	}

	/** 
	 * Getter f&uuml;r das K&uuml;rzel.
	 * @return Das K&uuml;rzel
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Setter f&uuml;r das K&uuml;rzel.
	 * @param code Das K&uuml;rzel
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
