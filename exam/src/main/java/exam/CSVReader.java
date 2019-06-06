package exam;

import java.io.*;
import java.util.Vector;

public class CSVReader {
	private static String fileName;
	private static Vector<Pharmacy> pharmacies;

	public CSVReader(String fileName, Vector<Pharmacy> pharmacies) {
		super();
		this.fileName = fileName;
		this.pharmacies = pharmacies;
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		CSVReader.fileName = fileName;
	}

	public Vector<Pharmacy> getPharmacies() {
		return pharmacies;
	}

	public void setPharmacies(Vector<Pharmacy> pharmacies) {
		this.pharmacies = pharmacies;
	}

	public static void reader() {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		try {

			br = new BufferedReader(new FileReader(fileName));
			br.readLine();
			while ((line = br.readLine()) != null) {
				if (line.contains("\"Via Cappuccini ;163\"")) {
					line = line.replace("\"Via Cappuccini ;163\"", "\"Via Cappuccini ,163\"");
				}

				if (line.contains("\"via vespucci;26\"")) {
					line = line.replace("\"via vespucci;26\"", "\"via vespucci,26\"");
				}
				if (line.contains("\'9,258444")) {
					line = line.replace("\'9,258444", "9.258444");
				}

				String[] test = line.split(cvsSplitBy);

				if (test[14].contains(",")) {
					test[14] = test[14].replace(',', '.');
					// System.out.println(test[14]);
				}
				if (test[14].contains("-")) {
					test[14] = test[14].replace('-', '0');
					// System.out.println(test[15]);
				}
				if (test[15].contains(",")) {
					test[15] = test[15].replace(',', '.');
					// System.out.println(test[15]);
				}
				if (test[15].contains("-")) {
					test[15] = test[15].replace('-', '0');
					// System.out.println(test[15]);
				}

				pharmacies.add(new Pharmacy(Double.parseDouble(test[0]), test[1], test[2], Double.parseDouble(test[3]),
						Double.parseDouble(test[4]), Double.parseDouble(test[5]), test[6], Double.parseDouble(test[7]),
						test[8], test[9], Double.parseDouble(test[10]), test[11], test[12], test[13],
						Double.parseDouble(test[14]), Double.parseDouble(test[15]), Double.parseDouble(test[16])));

			}

		} catch (NumberFormatException ex) {
			// Thrown to indicate that the application has attempted to convert a string to
			// one of the numeric types, but that the string does not have the appropriate
			// format.
			System.err.println("Illegal input");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					System.out.println("\nCLOSED\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		for (Pharmacy item : pharmacies) {
			System.out.println(item.toString() + "\n");

		}
	}

}