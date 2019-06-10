package exam;

import java.util.*;
import java.lang.Math;
import org.springframework.stereotype.Component;

@Component
public class PharmacyService {

	private static Vector<Pharmacy> pharmacies;

	public static Vector<Pharmacy> getPharmacies() {
		return pharmacies;
	}

	public static void setPharmacies(Vector<Pharmacy> pharmacies) {
		PharmacyService.pharmacies = pharmacies;
	}

	public Vector<Pharmacy> retrieveAllPharmacies() {
		return pharmacies;
	}

	public Vector<Pharmacy> searchName(String text) {
		Vector<Pharmacy> temp = new Vector<Pharmacy>();
		for (Pharmacy item : pharmacies) {
			if (item.getName().contentEquals(text)) {
				temp.add(item);
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
