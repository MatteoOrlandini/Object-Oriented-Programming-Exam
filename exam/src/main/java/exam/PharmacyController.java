package exam;

import java.net.URI;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PharmacyController {
	@Autowired
	private PharmacyService pharmacyService;

	@GetMapping("/pharmacy")
	public Vector <Pharmacy> retrieveAllPharmacies() {
		return pharmacyService.retrieveAllPharmacies();
	}
	
	@GetMapping("/si")
	public String si() {
		return "si";
	}
}
