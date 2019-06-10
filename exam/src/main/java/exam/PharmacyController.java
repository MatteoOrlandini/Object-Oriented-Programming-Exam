package exam;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PharmacyController {
	@Autowired
	private PharmacyService pharmacyService;

	@GetMapping("/data")
	public Vector<Pharmacy> retrieveAllPharmacies() {
		return pharmacyService.retrieveAllPharmacies();
	}

	@GetMapping("/search/{name}")
	public Vector<Pharmacy> searchName(@PathVariable String name) {
		return pharmacyService.searchName(name);
	}

	@GetMapping("/si")
	public String si() {
		return "si";
	}

	// da correggere
	@PostMapping(value = "/filter")
	public String filter(@RequestBody String param) {
		try {
			JsonParser jsonParser = new BasicJsonParser();
			Map<String, Object> jsonMap = null;
			jsonMap = jsonParser.parseMap(param);
		} catch (Exception e) {
			throw new RuntimeException("Filed", e);
		}

		JSONObject obj = null;
		try {
			obj = (JSONObject) JSONValue.parseWithException(param);

		// esempio JSON { "metadata": "id", "operator": "in", "value": ["2000","5000"] }
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String objO = (String) (obj.get("metadata"));
		System.out.println("metadata" + objO);
		String objO2 = (String) (obj.get("operator"));
		System.out.println("operator" + objO2);
		JSONArray objA = (JSONArray) (obj.get("value"));
		System.out.println("value" + objA);
		return new String("filter. Body: " + param);
	}
}
