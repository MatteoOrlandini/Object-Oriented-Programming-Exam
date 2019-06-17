package exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Vector;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		String dataset = new String("dataset.csv");
		String url = new String(
				"https://www.dati.gov.it/api/3/action/package_show?id=3c68b286-09fd-447a-b8e3-1b8430f70969");
		JSONparser jsonParser = new JSONparser(dataset, url);
		jsonParser.parser();

		Vector<Pharmacy> pharmacies = new Vector<Pharmacy>();
		Vector<Metadata> metadata = new Vector<Metadata>();
		CSVParser csvParser = new CSVParser("dataset.csv", pharmacies, metadata);
		csvParser.reader();

		PharmacyService.setPharmacies(pharmacies);
		PharmacyService.setMetadata(metadata);

		SpringApplication.run(Application.class, args);
	}

}
