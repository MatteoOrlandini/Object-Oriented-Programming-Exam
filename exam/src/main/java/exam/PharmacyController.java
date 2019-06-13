package exam;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PharmacyController {
	@Autowired
	private PharmacyService pharmacyService;

	@GetMapping("/data")
	public Vector<Pharmacy> retrieveAllPharmacies() {
		return pharmacyService.getPharmacies();
	}

	@GetMapping("/metadata")
	public Vector<Metadata> retrieveMetadata() {
		return pharmacyService.getMetadata();
	}

	@GetMapping("/search/{attribute}")
	public Vector<Pharmacy> searchName(@PathVariable String attribute, @RequestParam(value = "value") String text) {
		return pharmacyService.search(attribute, text);
	}

	// localizza
	@PostMapping(value = "/localize")
	public Vector<Pharmacy> localize(@RequestBody String param) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) JSONValue.parseWithException(param);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double latitude = ((Number) obj.get("latitude")).doubleValue();
		double longitude = ((Number) obj.get("longitude")).doubleValue();
		double range = ((Number) obj.get("range")).doubleValue();

		return pharmacyService.localize(latitude, longitude, range);
	}

	@PostMapping(value = "/filter")
	public Vector<Pharmacy> filter(@RequestBody String param) {
		JSONArray objA = null;
		try {
			objA = (JSONArray) JSONValue.parseWithException(param);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector<Pharmacy> temp = pharmacyService.getPharmacies();
		
		for (Object obj : objA) {
			if (obj instanceof JSONObject) {
				JSONObject o = (JSONObject) obj;

				String fieldName = (String) o.get("fieldName");
				String operator = (String) o.get("operator");
				Object value = o.get("value");
				temp = pharmacyService.filter(temp, fieldName, operator, value);
			}
		}
		return temp;
	}
}
