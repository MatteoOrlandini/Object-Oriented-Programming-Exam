package exam;

import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
// spring libraries
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Manages the Spring application and define the filters using GET or POST
 * requests.
 */
@RestController
public class PharmacyController {
	@Autowired
	private PharmacyService pharmacyService;

	/**
	 * Returns all the pharmacies using a GET request.
	 * 
	 * @return the entire dataset
	 */
	@GetMapping("/data")
	public Vector<Pharmacy> retrievePharmacies() {
		return pharmacyService.getPharmacies();
	}

	/**
	 * Returns all the metadata using a GET request.
	 * 
	 * @return the metadata
	 */
	@GetMapping("/metadata")
	public Vector<Metadata> retrieveMetadata() {
		return pharmacyService.getMetadata();
	}

	/**
	 * Gives stats based on the class {@link NumberStats}. <strong>Note:</strong>
	 * the allowed fields are latitude and longitude.
	 * 
	 * @param fieldName allowed: latitude or longitude
	 * @return average, minimum, maximum, standard deviation and sum
	 */
	@GetMapping("/stats/{fieldName}")
	public NumberStats stats(@PathVariable String fieldName) {
		return pharmacyService.stats(fieldName, pharmacyService.getPharmacies());
	}

	/**
	 * Returns the number of times the string of the specified field repeats itself.
	 * 
	 * @param fieldName the {@link Pharmacy} attribute
	 * @param value     the string to count
	 * @return the number of unique items
	 */
	@GetMapping("/count/{fieldName}")
	public String count(@PathVariable String fieldName, @RequestParam(value = "value") String value) {
		Vector<Pharmacy> pharmacies = pharmacyService.getPharmacies();
		FilterParameters filterParam = new FilterParameters(fieldName, "==", value);
		return "count : " + pharmacyService.filter(pharmacies, filterParam).size();
	}

	/**
	 * Using a POST request it looks in the body for a latitude, a longitude and a
	 * range and through the {@link PharmacyService} returns those that are in the
	 * specified area.
	 * 
	 * @param param a body that contains the filter parameters
	 * @return the pharmacies in a given range (km)
	 */
	@PostMapping(value = "/localize")
	public Vector<Pharmacy> localize(@RequestBody String param) {
		JSONObject obj = null;
		double latitude = 0, longitude = 0, range = 0;
		try {
			obj = (JSONObject) JSONValue.parseWithException(param);

			latitude = ((Number) obj.get("latitude")).doubleValue();
			longitude = ((Number) obj.get("longitude")).doubleValue();
			range = ((Number) obj.get("range")).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Incorrect JSON body");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON body");
		}
		return pharmacyService.localize(latitude, longitude, range);
	}

	/**
	 * Generic filter using a POST. If the body of the JSON is a single object it
	 * searches for a field, an operator and an input value and returns the filtered
	 * dataset. If it is found an attribute called "$or" or "$and" it applies
	 * multiple filters, using the following array of objects, based on the
	 * attribute. The "$or" filter does a filter for each object and then unites
	 * them without considering multiple elements. The "$and" filter just
	 * recursively filter the result of the previous decimation.
	 * 
	 * @param param JSON array with objects composed by a field, an operator and an
	 *              input value
	 * @return filtered pharmacies
	 */
	@PostMapping(value = "/filter")
	public Vector<Pharmacy> filter(@RequestBody String param) {
		JSONObject jsonObject = null;
		boolean correctRequest = false;
		try {
			jsonObject = (JSONObject) JSONValue.parseWithException(param);
		} catch (ParseException e) {
			System.out.println("Error while parsing JSON" + jsonObject);
		}
		Vector<Pharmacy> temp = pharmacyService.getPharmacies();
		FilterParameters filterParam = new FilterParameters(null, null, null);
		// single filter case
		filterParam.readFields(jsonObject);
		if (filterParam.getFieldName() != null && filterParam.getOperator() != null && filterParam.getValue() != null) {
			correctRequest = true;
			temp = pharmacyService.filter(temp, filterParam);
		}

		// multiple filter cases: or - and

		JSONArray jsonArray = (JSONArray) jsonObject.get("$or");
		if (jsonArray instanceof JSONArray) {
			correctRequest = true;
			// tempOr is used to store the partial filters
			Vector<Pharmacy> tempOr = new Vector<Pharmacy>();
			// the temp vector is emptied
			temp = new Vector<Pharmacy>();

			for (Object obj : jsonArray) {
				filterParam.readFields(obj);
				tempOr = pharmacyService.filter(pharmacyService.getPharmacies(), filterParam);
				for (Pharmacy item : tempOr) {
					if (!temp.contains(item))
						temp.add(item);
				}
			}
		}

		jsonArray = (JSONArray) jsonObject.get("$and");
		if (jsonArray instanceof JSONArray) {
			correctRequest = true;
			for (Object obj : jsonArray) {
				filterParam.readFields(obj);
				// iteration on .filter
				temp = pharmacyService.filter(temp, filterParam);
			}
		}

		if (!correctRequest) {
			System.out.println("Incorrect JSON body");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect JSON body");
		}

		return temp;
	}

	/**
	 * Gives stats based on {@link NumberStats} on the sample made of the filtered
	 * pharmacies.
	 * 
	 * @param param     JSON array with objects composed by a field, an operator and
	 *                  an input value
	 * @param fieldName allowed: latitude or longitude
	 * @return statistics on the filtered list
	 */
	@PostMapping(value = "/filter/stats/{fieldName}")
	public NumberStats filterStats(@RequestBody String param, @PathVariable String fieldName) {
		Vector<Pharmacy> sample = new Vector<Pharmacy>();
		sample = filter(param);
		return pharmacyService.stats(fieldName, sample);
	}

}
