package exam;

import java.util.*;
import java.lang.Math;
import java.lang.reflect.*;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class PharmacyService {

	private static Vector<Pharmacy> pharmacies;
	private static Vector<Metadata> metadata;

	public Vector<Pharmacy> getPharmacies() {
		return pharmacies;
	}

	public static void setPharmacies(Vector<Pharmacy> pharmacies) {
		PharmacyService.pharmacies = pharmacies;
	}

	public Vector<Metadata> getMetadata() {
		return metadata;
	}

	public static void setMetadata(Vector<Metadata> metadata) {
		PharmacyService.metadata = metadata;
	}

	public Vector<Pharmacy> search(String attribute, String text) {
		Vector<Pharmacy> temp = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			try {
				Method m = item.getClass()
						.getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1));
				try {
					if (m.invoke(item).toString().equals(text)) {
						temp.add(item);
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return temp;
	}

	public Vector<Pharmacy> localize(double x, double y, double range) {
		Vector<Pharmacy> temp = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			double dist = Math.acos(Math.sin(degToRad(x)) * Math.sin(degToRad(item.getLatitude()))
					+ Math.cos(degToRad(x)) * Math.cos(degToRad(item.getLatitude()))
							* Math.cos(degToRad(item.getLongitude()) - degToRad(y)))
					* 6371;
			if (dist <= range) {
				temp.add(item);
			}
		}
		return temp;
	}

	public static boolean check(Object pharmacyValue, String operator, Object inputValue) {
		if (inputValue instanceof Number && pharmacyValue instanceof Number) {
			Double doubleInputValue = ((Number) inputValue).doubleValue();
			Double doublePharmacyValue = ((Number) pharmacyValue).doubleValue();
			if (operator.equals("=="))
				return pharmacyValue.equals(inputValue);
			else if (operator.equals(">"))
				return doublePharmacyValue > doubleInputValue;
			else if (operator.equals("<"))
				return doublePharmacyValue < doubleInputValue;
		} else if (inputValue instanceof String && pharmacyValue instanceof String) {
			String inputString = (String) inputValue;
			String pharmacyString = (String) pharmacyValue;
			if (isValidDate(inputString) && isValidDate(pharmacyString)) {
				Date inputDate = stringTodate(inputString);
				Date pharmacyDate = stringTodate(pharmacyString);
				 if (operator.equals("==")) 
					 return inputDate.equals(pharmacyDate);
				 else if (operator.equals(">")) 
					 return inputDate.before(pharmacyDate);			 
				 else if (operator.equals("<"))
					 return inputDate.after(pharmacyDate);
			}

			return pharmacyValue.equals(inputValue);
		}

		/*
		 * else if(inputValue instanceof Object && pharmacyValue instanceof Date) { Date
		 * datePharmacyValue = (Date)pharmacyValue; String stringInputValue = (String)
		 * inputValue; Date dateInputValue = null; try { dateInputValue = new
		 * SimpleDateFormat("dd/MM/yyyy").parse(stringInputValue); } catch
		 * (ParseException e) { // TODO Auto-generated catch block e.printStackTrace();
		 * } if (operator.equals("==")) return dateInputValue.equals(datePharmacyValue);
		 * else if (operator.equals(">")) return
		 * dateInputValue.before(datePharmacyValue); else if (operator.equals("<"))
		 * return dateInputValue.after(datePharmacyValue);; }
		 */

		return false;

	}

	public Vector<Pharmacy> filter(Vector<Pharmacy> pharmacies, String fieldName, String operator, Object inputValue) {
		Vector<Pharmacy> out = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			try {
				Method m = item.getClass()
						.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
				try {
					Object pharmacyValue = m.invoke(item);
					if (PharmacyService.check(pharmacyValue, operator, inputValue))
						out.add(item);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return out;
	}

	public double degToRad(double deg) {
		return deg * Math.PI / 180;
	}

	public static Date stringTodate(String str) {
		Date date = null;
		if (!str.equals("-")) {
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("errore str->date");
				e.printStackTrace();
			}
		}
		return date;
	}

	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
}
