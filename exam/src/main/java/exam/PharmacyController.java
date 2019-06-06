package exam;

import java.net.URI;
import java.util.List;
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
	
	@GetMapping("/search/{name}")
	public Vector <Pharmacy> searchName(@PathVariable String name) {
		return pharmacyService.searchName(name);
	}
	
	@GetMapping("/si")
	public String si() {
		return "si";
	}
}
