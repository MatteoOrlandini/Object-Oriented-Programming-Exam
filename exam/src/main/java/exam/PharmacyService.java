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
	 * Filters the pharmacies returning only the ones within a certain range from a
	 * specified point described by x and y.<br>
	 * <strong>Note:</strong> latitude and longitude are GPS coordinates so it is
	 * needed a specific formula to calculate the distance between them.
	 * 
	 * @param x     longitude of the centre
	 * @param y     latitude of the centre
	 * @param range radius of the area searched
	 * @return temp vector made of all the pharmacies in the specified area
	 */
	public Vector<Pharmacy> localize(double x, double y, double range) {
		Vector<Pharmacy> temp = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			// formula to get distance from two latitude and longitude coordinates
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
	 * @param operator      it can be (==,>,<,<=,>=)
	 * @param inputValue    value in the input JSON
	 * @return false or true depending of the result of the comparison
	 */
	public static boolean check(Object pharmacyValue, String operator, Object inputValue) {
		if (inputValue instanceof Number && pharmacyValue instanceof Number) {
			Double doubleInputValue = ((Number) inputValue).doubleValue();
			Double doublePharmacyValue = ((Number) pharmacyValue).doubleValue();
			// only latitude and longitude can be double
			Double warningValue = -360.0;
			if (!doublePharmacyValue.equals(warningValue)) {
				if (operator.equals("=="))
					return doublePharmacyValue.equals(doubleInputValue);
				else if (operator.equals(">"))
					return doublePharmacyValue > doubleInputValue;
				else if (operator.equals(">="))
					return doublePharmacyValue >= doubleInputValue;
				else if (operator.equals("<"))
					return doublePharmacyValue < doubleInputValue;
				else if (operator.equals("<="))
					return doublePharmacyValue <= doubleInputValue;
			}
		} else if (inputValue instanceof String && pharmacyValue instanceof String) {
			String inputString = (String) inputValue;
			String pharmacyString = (String) pharmacyValue;
			if (operator.equals("=="))
				return inputString.equals(pharmacyString);

		} else if (inputValue instanceof String && pharmacyValue instanceof Date) {
			
			String inputString = (String) inputValue;
			Date pharmacyDate = (Date) pharmacyValue;
			
			if (isValidDate(inputString)) {
			Date inputDate = stringToDate(inputString);
			if (operator.equals("=="))
				return inputDate.equals(pharmacyDate);
			else if (operator.equals(">"))
				return inputDate.before(pharmacyDate);
			else if (operator.equals("<"))
				return inputDate.after(pharmacyDate);
		}
		}
		return false;
	}

	/**
	 * Filters the pharmacies and returns only those that meet the conditions of the
	 * input value and the operator. It uses the method check to verify the
	 * conditions.
	 * 
	 * @param pharmacies the whole dataset. It is needed in order to have the
	 *                   possibility to iterate the process giving another Vector
	 *                   already filtered as input.
	 * @param fieldName  the field to compare
	 * @param operator   the arithmetical operator
	 * @param inputValue the value wrote by the user in the JSON body
	 * @return temp vector made by the parmacies that met the conditions
	 */
	public Vector<Pharmacy> filter(Vector<Pharmacy> pharmacies, FilterParameters param) {
		Vector<Pharmacy> out = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			try {
				Method m = item.getClass().getMethod(
						"get" + param.getFieldName().substring(0, 1).toUpperCase() + param.getFieldName().substring(1));
				try {
					Object pharmacyValue = m.invoke(item);
					if (PharmacyService.check(pharmacyValue, param.getOperator(), param.getValue()))
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
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return out;
	}

	/**
	 * Checks if the input String is a valid Date that respects the format.
	 * 
	 * @param str
	 * @return true if the string is a valid date with "dd/MM/yyyy" format,
	 *         otherwise it returns false
	 */
	public static boolean isValidDate(String str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// with a strict parsing, inputs must match the format "dd/MM/yyyy"
		dateFormat.setLenient(false);
		try {
			// parse the string inDate (with removed space) to a SimpleDateFormat object
			dateFormat.parse(str.trim());
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	public static Date stringToDate(String str) {
		Date date = null;
		if (!str.equals("-")) {
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
			} catch (ParseException e) {
				System.out.println("Parse error from string " + str + " to date");
				e.printStackTrace();
			}
		}
		return date;
	}
	
}
