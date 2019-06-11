package exam;

import java.util.*;
import java.lang.Math;
import java.lang.reflect.*;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

	public double degToRad(double deg) {
		return deg * Math.PI / 180;
	}
	
	////////////////////////////////////////////////////////////////////////// filtro generico
	
	public static boolean check(Object value, String operator, Object th) {
		if (th instanceof Number && value instanceof Number) {	
			Double thC = ((Number)th).doubleValue();
			Double valuec = ((Number)value).doubleValue();
			if (operator.equals("==")) 
				return value.equals(th);
			else if (operator.equals(">"))
				return valuec > thC;
			else if (operator.equals("<"))
				return valuec < thC;
		}else if(th instanceof String && value instanceof String)
			return value.equals(th);
		return false;		
	}
	
	public Vector<Pharmacy> select (Vector<Pharmacy> src, String fieldName, String operator, Object value) {
		Vector<Pharmacy> out = new Vector<Pharmacy>();
		for(Pharmacy item:src) {
			try {
				Method m = item.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1));
				try {
					Object tmp = m.invoke(item);
					if(PharmacyService.check(tmp, operator, value))
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
}
