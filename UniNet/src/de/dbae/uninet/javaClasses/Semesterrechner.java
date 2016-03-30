package de.dbae.uninet.javaClasses;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Semesterrechner {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static Calendar cal = new GregorianCalendar();

	public long getStudienbeginn(int semester) {
		long beginn = 0L;
		Date beginnLetztesSemester = getBeginnLetztesSemester();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginnLetztesSemester);
		int abgeschlosseneSemester = semester - 1;
		while (abgeschlosseneSemester > 0) {
			countCalendar(calendar, false);
			abgeschlosseneSemester--;
		}
		beginn = calendar.getTimeInMillis();
		return beginn;
	}
	
	public int getSemester(long studienbeginn) {
		int semester = 1;
		Date beginnLetztesSemester = getBeginnLetztesSemester();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginnLetztesSemester);
		while (beginnLetztesSemester.getTime() > studienbeginn) {
			countCalendar(calendar, false);
			beginnLetztesSemester = calendar.getTime();
			semester++;
		}
		return semester;
	}
	
	private Date getBeginnLetztesSemester() {
		Date beginnLetztesSemester = null;
		try {
			Date fruehling = sdf.parse("01.04." + cal.get(Calendar.YEAR));
			Date herbst = sdf.parse("01.10." + cal.get(Calendar.YEAR));
			long jetzt = System.currentTimeMillis();
			if (fruehling.getTime() > jetzt) {
				beginnLetztesSemester = sdf.parse("01.10." + (cal.get(Calendar.YEAR) - 1));
			} else if (herbst.getTime() > jetzt) {
				beginnLetztesSemester = fruehling;
			} else {
				beginnLetztesSemester = herbst;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Fehler
		}
		return beginnLetztesSemester;
	}
	
	private void countCalendar(Calendar calendar, boolean up) {
		if (calendar.get(Calendar.MONTH) == 3) {
			calendar.set(Calendar.MONTH, 9);
			if (!up)
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
		} else {
			calendar.set(Calendar.MONTH, 3);
			if (up)
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		}
	}
}
