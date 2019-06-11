package exam;

import java.util.*;
import java.lang.Math;
import java.lang.reflect.*;
import org.springframework.stereotype.Component;

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
					if (m.invoke(item).equals(text)) {
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
}
