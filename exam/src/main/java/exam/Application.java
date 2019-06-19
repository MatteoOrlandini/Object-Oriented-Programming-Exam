package exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

@SpringBootApplication
public class Application {
	private static String CSVfilename;
	private static String url;
	private static final String configFilename = "config.txt";

	public static void main(String[] args) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(configFilename));
			url = bufferedReader.readLine();
			CSVfilename = bufferedReader.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		JSONParser jsonParser = new JSONParser(CSVfilename, url);
		// download the CSV file from the url
		jsonParser.openConnection();
		Vector<Pharmacy> pharmacies = new Vector<Pharmacy>();
		Vector<Metadata> metadata = new Vector<Metadata>();
		CSVParser csvParser = new CSVParser(CSVfilename, pharmacies, metadata);
		// read the CSV file and parse it to the metadata and a pharmacy vector
		csvParser.reader();

		PharmacyService.setPharmacies(pharmacies);
		PharmacyService.setMetadata(metadata);

		SpringApplication.run(Application.class, args);
	}

}
