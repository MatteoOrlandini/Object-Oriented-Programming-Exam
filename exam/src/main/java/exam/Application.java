package exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		//MyDate oggi=new MyDate();
		//oggi=oggi.stringToDate("20/06/2019");
		//System.out.println(oggi);
		BufferedReader bufferedReader = null;
		FileManager fileManager = new FileManager("config.txt", bufferedReader);
		fileManager.openBufferStream();
		String url = fileManager.readOneLine();
		String CSVfilename = fileManager.readOneLine();
		fileManager.fileClose();
		
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
