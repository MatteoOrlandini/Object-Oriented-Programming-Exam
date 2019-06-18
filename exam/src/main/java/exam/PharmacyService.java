package exam;

import java.util.*;
import java.lang.Math;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class contains the methods used by the controller or the service itself
 * to manage the filters.
 */
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
	
	/**
	 * Search for the pharmacy attribute "fieldName" that is
	 * equal to the given value and counts the times it occurred.
	 * @param fieldName     name of the field attribute to search
	 * @param value     	value to find
	 * @return count number of the unique strings
	 */
	public int getStats(String fieldName, String value) {
		int count = 0;
		for (Pharmacy item : pharmacies) {
			try {
				Method m = item.getClass()
						.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
				try {
					Object pharmacyValue = m.invoke(item);
					if (PharmacyService.check(pharmacyValue, "==", value))
						count++;
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
		return count;
	}

	/**
	 * Filters the pharmacies returning only the ones within a certain range from a
	 * specified point described by x and y.<br>
	 * <strong>Note:</strong> latitude and longitude are GPS coordinates so it is needed
	 * a specific formula to calculate the distance between them.
	 * 
	 * @param x     longitude of the centre
	 * @param y     latitude of the centre
	 * @param range radius of the area searched
	 * @return temp vector made of all the pharmacies in the specified area
	 */
	public Vector<Pharmacy> localize(double x, double y, double range) {
		Vector<Pharmacy> temp = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			double dist = Math.acos(Math.sin(Math.toRadians(x)) * Math.sin(Math.toRadians(item.getLatitude()))
					+ Math.cos(Math.toRadians(x)) * Math.cos(Math.toRadians(item.getLatitude()))
							* Math.cos(Math.toRadians(item.getLongitude()) - Math.toRadians(y)))
					* 6371;
			if (dist <= range) {
				temp.add(item);
			}
		}
		return temp;
	}

	/**
	 * Method used by the main filter that checks whether the value in the dataset
	 * respects the conditions imposed by the operator and the input value.
	 * 
	 * @param pharmacyValue value in the dataset
	 * @param operator      it can be (==,>,<)
	 * @param inputValue    value in the input JSON
	 * @return false or true depending of the result of the comparison
	 */
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
		return false;
	}

	/**
	 * Filters the pharmacies and returns only those that meet the conditions of the
	 * input value and the operator. It uses the method check to verify the
	 * conditions.
	 * 
	 * @param pharmacies the whole dataset. It is needed in order to have the possibility to
	 * iterate the process giving another Vector already filtered as input.
	 * @param fieldName  the field to compare
	 * @param operator   the arithmetical operator
	 * @param inputValue the value wrote by the user in the JSON body
	 * @return temp vector made by the parmacies that met the conditions
	 */
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


	/**
	 * Converts a String in a Date Object if it's not a dash
	 * 
	 * @param str
	 * @return Date equivalent
	 */
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

	/**
	 * Checks if the input String is a valid Date that respects the format
	 * 
	 * @param inDate
	 * @return true or false
	 */
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
