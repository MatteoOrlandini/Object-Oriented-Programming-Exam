package exam;

import java.util.Vector;

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

	public Vector <Pharmacy> retrieveAllPharmacies(){
		return pharmacies;
	}
}
