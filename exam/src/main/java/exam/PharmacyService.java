package exam;

import java.util.*;
import java.lang.Math;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Contains the methods used by the controller or the service itself to manage
 * the filters.
 * 
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
	 * Gives statistics on numbers based on {@link NumberStats}.
	 * 
	 * @param fieldName fields accepted for this data-set are latitude and longitude
	 * @param sample    the pharmacy vector to study
	 * @return various statistics: average, minimum, maximum, standard deviation and
	 *         sum
	 */
	public NumberStats stats(String fieldName, Vector<Pharmacy> sample) {
		Method m = null;
		Vector<Double> store = new Vector<Double>();
		int count;
		double avg = 0, min = 0, max = 0, std = 0, sum = 0;
		try {
			for (Pharmacy item : sample) {

				m = item.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
				Object pharmacyValue = m.invoke(item);
				double pharmacyValuedouble = (double) pharmacyValue;
				if (pharmacyValuedouble != -360)
					store.add((Double) pharmacyValue);
			}
			count = store.size();
			min = store.get(0);
			max = store.get(0);
			for (Double item : store) {
				avg += item;
				if (item < min)
					min = item;
				if (item > max)
					max = item;
				sum += item;
				std += item * item;
			}
			avg = avg / count;
			std = Math.sqrt((count * std - sum * sum) / (count * count));
		} catch (IllegalAccessException e) {
			System.out.println("The method " + m + " does not have access to the definition of the specified field");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal access method:" + m);
		} catch (InvocationTargetException e) {
			System.out.println("InvocationTargetException");
		} catch (ClassCastException e) {
			System.out.println("String cannot be cast to class Double");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "String cannot be cast to class Double");
		}

		catch (NoSuchMethodException e) {
			System.out.println("The method get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)
					+ " cannot be found");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The method get"
					+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + " cannot be found");
		} catch (SecurityException e) {
			System.out.println("Security violation");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Security violation");
		}
		NumberStats stats = new NumberStats(avg, min, max, std, sum);
		return stats;
	}

	/**
	 * Filters the pharmacies returning only the ones within a certain range from a
	 * specified point described by x and y.<br>
	 * <strong>Note:</strong> latitude and longitude are GPS coordinates so it is
	 * needed a specific formula to calculate the distance between them.
	 * 
	 * @param x     longitude of the center
	 * @param y     latitude of the center
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
	 * Method used by the main filter that checks whether the value in the data-set
	 * respects the conditions imposed by the operator and the input value.
	 * 
	 * @param pharmacyValue value in the data-set
	 * @param operator      it can be (==,>,<,<=,>=)
	 * @param inputValue    value in the input JSON
	 * @throws ResponseStatusException to the Spring application if the request is
	 *                                 not correct
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
				else
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal operator");
			}
		} else if (inputValue instanceof String && pharmacyValue instanceof String) {
			String inputString = (String) inputValue;
			String pharmacyString = (String) pharmacyValue;
			if (operator.equals("=="))
				return inputString.equals(pharmacyString);
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal operator");

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
				else
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal operator");
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal request");
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
	 * @param param      an object that contains operator, value and field name
	 * @throws IllegalAccessException    when the currently executing method does
	 *                                   not have access to the definition of the
	 *                                   specified field
	 * @throws IllegalArgumentException  when an illegal or inappropriate argument
	 *                                   has been passed to a method
	 * @throws InvocationTargetException wraps an exception thrown by an invoked
	 *                                   method or constructor
	 * @throws NoSuchMethodException     when a particular method cannot be found
	 * @throws SecurityException         indicate a security violation
	 * @throws ResponseStatusException to the Spring application if the request is
	 *                                 not correct
	 * @return temp vector made by the parmacies that met the conditions
	 */
	public Vector<Pharmacy> filter(Vector<Pharmacy> pharmacies, FilterParameters param) {
		Vector<Pharmacy> out = new Vector<Pharmacy>();
		Method m = null;
		try {
			for (Pharmacy item : pharmacies) {

				m = item.getClass().getMethod(
						"get" + param.getFieldName().substring(0, 1).toUpperCase() + param.getFieldName().substring(1));

				Object pharmacyValue = m.invoke(item);
				if (PharmacyService.check(pharmacyValue, param.getOperator(), param.getValue()))
					out.add(item);
			}
		} catch (IllegalAccessException e) {
			System.out.println("The method " + m + " does not have access to the definition of the specified field");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal access method:" + m);
		} catch (IllegalArgumentException e) {
			System.out.println(
					"An illegal or inappropriate argument " + param.getValue() + " has been passed to a method");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal argument");
		} catch (InvocationTargetException e) {
			System.out.println();
		} catch (NullPointerException e) {
			System.out.println("Incorrect JSON body");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON body");
		} catch (NoSuchMethodException e) {
			System.out.println("The method get" + param.getFieldName().substring(0, 1).toUpperCase()
					+ param.getFieldName().substring(1) + " cannot be found");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"The method get" + param.getFieldName().substring(0, 1).toUpperCase()
							+ param.getFieldName().substring(1) + " cannot be found");
		} catch (SecurityException e) {
			System.out.println("Security violation");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Security violation");
		}
		return out;
	}

	/**
	 * Checks if the input String is a valid Date that respects the format.
	 * 
	 * @param str
	 * @throws ParseException signals an error while parsing
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

	/**
	 * Converts a String in a Date Object if it's not a dash.
	 * 
	 * @param str represent the date with a string
	 * @throws ParseException signals an error while parsing
	 * @return Date equivalent of str
	 */
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
