package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Admin;

/**
 * Dieser Tag zeigt die Informationen aus dem Admin-Objekt auf der jsp an.
 * Er wird auf den Admin-Seiten des Projekts verwendet.
 * @author Leon Schaffert
 */
public class AdminAnzeigeTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	/**
	 * Der Admin mit allen wichtigen Eigenschaften.
	 * @see Admin
	 */
	private Admin admin;


	/**
	 * Aktionen zu Beginn des Tags.<br>
	 * H&auml;ngt den HTML-Code an die jsp an und &uuml;berspringt den Body.
	 */
	public int doStartTag() {
		Writer out = pageContext.getOut();
		try {
			out.append(getHtmlCode());
		} catch (Exception e) {
			System.out.println("Fehler beim Anhängen!");
			// TODO Fehler
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r den Admin.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String code = "";
		code += "<tr><td>" + admin.getAdminID() + "</td>";
		code +=	"<td>" + admin.getAdminVorname() + "</td>";
		code += "<td>" + admin.getAdminNachname() + "</td>";
		code += "<td>" + admin.getZugehoerigeUni() + "<a class='pull-right' ";
		code += "href='AdminServlet?loeschen="+ admin.getAdminID() + "' title='Admin l&ouml;schen'><span ";
		code += "class='glyphicon glyphicon glyphicon-trash' style='color: #3b5998;'></span></a>";
		code += "</td></tr>";
		return code;
	}
	
	/**
	 * Getter f&uuml;r den Admin.
	 * @return Das Adminobjekt
	 * @see Admin
	 */
	public Admin getAdmin() {
		return admin;
	}

	/**
	 * Setter f&uuml;r den Admin.
	 * @param admin Der Admin
	 * @see Admin
	 */
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}
