package exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		JSONparseranddownload.setFileName("dataset.csv");
		JSONparseranddownload.setUrl( "https://www.dati.gov.it/api/3/action/package_show?id=3c68b286-09fd-447a-b8e3-1b8430f70969");
		//SpringApplication.run(Application.class, args);
	}

}
