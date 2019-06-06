package exam;

import java.util.*;

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
}
