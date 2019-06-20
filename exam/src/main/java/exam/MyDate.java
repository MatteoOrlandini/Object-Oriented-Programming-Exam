package exam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {
	Date date;
	private static final long serialVersionUID = -3576328492723421034L;

	public MyDate (Date date) {
		this.date=date;
	}

	public String toString (Date date) {
		return (date.getDay());
	}
	public MyDate stringToDate(String str) {
		Date date = new MyDate();
		if (date instanceof MyDate) {
			System.out.println("valido");
		}
		MyDate mia=null;
		if (!str.equals("-")) {
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
			} catch (ParseException e) {
				System.out.println("Parse error from string " + str + " to date");
				e.printStackTrace();
			}
		}
		date=(MyDate) date;
		if (date instanceof MyDate) {
			System.out.println("valido2");
			mia=(MyDate)date;
		}
		return mia;
	}
	
	public String toString() {
		
	}
}
