package exam;

import java.io.*;
import java.util.Vector;

public class CSVReader {
	private static String csvFile;
	private static Vector<Pharmacy> pharmacies;

	public CSVReader(String csvFile, Vector<Pharmacy> pharmacies) {
		super();
		this.csvFile = csvFile;
		this.pharmacies = pharmacies;
	}

	public static String getCsvFile() {
		return csvFile;
	}

	public static void setCsvFile(String csvFile) {
		CSVReader.csvFile = csvFile;
	}

	public static Vector<Pharmacy> getPharmacies() {
		return pharmacies;
	}

	public static void setPharmacies(Vector<Pharmacy> pharmacies) {
		CSVReader.pharmacies = pharmacies;
	}

	// String csvFile = "dataset.csv";
	public static void popolate() {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		// Vector <Pharmacy> pharmacies=new Vector <Pharmacy>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {

				String[] test = line.split(cvsSplitBy);
				pharmacies.add(new Pharmacy(Double.parseDouble(test[0]), test[1], test[2], Double.parseDouble(test[3]),
						Double.parseDouble(test[4]), Double.parseDouble(test[5]), test[6], Double.parseDouble(test[7]),
						test[8], test[9], Double.parseDouble(test[10]), test[11], test[12], test[13], 14, 15,
						Double.parseDouble(test[16])));
				System.out.println("sonoqui");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}