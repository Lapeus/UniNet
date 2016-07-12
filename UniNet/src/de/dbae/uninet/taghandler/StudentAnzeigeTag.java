package de.dbae.uninet.taghandler;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import de.dbae.uninet.javaClasses.Student;

/**
 * Dieser Tag zeigt die Informationen aus dem Student-Objekt auf der jsp an.
 * Er wird auf den Admin-Seiten des Projekts verwendet.
 * @author Leon Schaffert
 */
public class StudentAnzeigeTag extends TagSupport {

	private static final long serialVersionUID = 4129407379981290381L;
	
	/**
	 * Der Student mit allen wichtigen Eigenschaften.
	 * @see Student
	 */
	private Student student;


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
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Generiert den auf der jsp angezeigten Code f&uuml;r den Studenten.<br>
	 * @return Den entsprechenden Html-Code
	 */
	private String getHtmlCode() {
		String code = "";
		code += "<tr><td>" + student.getUserID() + "</td>";
		code +=	"<td>" + student.getVorname() + "</td>";
		code += "<td>" + student.getNachname() + "</td>";
		code += "<td>" + student.getEmail() + "<a class='pull-right' ";
		code += "href='AdminUserVerwaltenServlet?loeschen="+ student.getUserID() + "' title='Student aus UniNet entfernen'><span ";
		code += "class='glyphicon glyphicon glyphicon-trash' style='color: #3b5998;'></span></a>";
		code += "</td></tr>";
		return code;
	}
	
	/**
	 * Getter f&uuml;r den Studenten.
	 * @return Das Studentobjekt
	 * @see Student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Setter f&uuml;r den Studenten.
	 * @param student Der Student
	 * @see Student
	 */
	public void setStudent(Student student) {
		this.student = student;
	}
}
