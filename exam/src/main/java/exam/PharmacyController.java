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

/**
 * This class manages the Spring application and define the filters using GET or
 * POST
 *
 */
@RestController
public class PharmacyController {
	@Autowired
	private PharmacyService pharmacyService;

	/**
	 * Returns all the pharmacies using a GET
	 * 
	 * @return all the dataset
	 */
	@GetMapping("/data")
	public Vector<Pharmacy> retrievePharmacies() {
		return pharmacyService.getPharmacies();
	}

	/**
	 * Returns all the metadata using a GET
	 * 
	 * @return the metadata
	 */
	@GetMapping("/metadata")
	public Vector<Metadata> retrieveMetadata() {
		return pharmacyService.getMetadata();
	}

	/**
	 * Returns string statistics using a POST
	 * 
	 * @return the number of unique items
	 */
	@PostMapping("/stats")
	public String retrieveStats(@RequestBody String param) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) JSONValue.parseWithException(param);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fieldName = (String) obj.get("fieldName");
		String value = (String) obj.get("value");
		return "count : " + pharmacyService.getStats(fieldName, value);
	}

	/**
	 * Using a POST it search in the body for a latitude, a longitude and a range
	 * and through the {@link PharmacyService} returns those that are in the
	 * specified area.
	 * 
	 * @param param a body that contains the filter parameters
	 * @return the pharmacies in a given radius
	 */
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

	/**
	 * Generic filter using a POST that search in the body for a field, an operator
	 * and an input value. It can get in the body multiple objects with the
	 * information described above to recursively apply a filter at the remaining
	 * pharmacies.
	 * 
	 * @param param JSON array with object composed by a field,an operator and an
	 *              input value
	 * @return filtered pharmacies
	 */
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
