package exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Vector;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		JSONparseranddownload.setFileName("dataset.csv");
		JSONparseranddownload
				.setUrl("https://www.dati.gov.it/api/3/action/package_show?id=3c68b286-09fd-447a-b8e3-1b8430f70969");
		JSONparseranddownload.parser();
		Vector<Pharmacy> pharmacies = new Vector<Pharmacy>();
		CSVReader csv = new CSVReader("dataset.csv", pharmacies);
		csv.reader();
		PharmacyService.setPharmacies(pharmacies);
		SpringApplication.run(Application.class, args);
	}

}
