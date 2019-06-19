package exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Vector;

@SpringBootApplication
public class Application {
	private static String filename = "dataset.csv";
	private static String url = "https://www.dati.gov.it/api/3/action/package_show?id=3c68b286-09fd-447a-b8e3-1b8430f70969";

	public static void main(String[] args) {
		JSONParser jsonParser = new JSONParser(filename, url);
		//download the CSV file from the url 
		jsonParser.openConnection();
		Vector<Pharmacy> pharmacies = new Vector<Pharmacy>();
		Vector<Metadata> metadata = new Vector<Metadata>();
		CSVParser csvParser = new CSVParser(filename, pharmacies, metadata);
		//read the CSV file and parse it to the metadata and a pharmacy vector
		csvParser.reader();

		PharmacyService.setPharmacies(pharmacies);
		PharmacyService.setMetadata(metadata);

		SpringApplication.run(Application.class, args);
	}

}
