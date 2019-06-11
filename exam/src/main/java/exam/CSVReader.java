package exam;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Vector;

public class CSVReader {
	private String fileName;
	private Vector<Pharmacy> pharmacies;
	private Vector<Metadata> metadata;

	public CSVReader(String fileName, Vector<Pharmacy> pharmacies, Vector<Metadata> metadata) {
		super();
		this.fileName = fileName;
		this.pharmacies = pharmacies;
		this.metadata = metadata;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Vector<Pharmacy> getPharmacies() {
		return pharmacies;
	}

	public void setPharmacies(Vector<Pharmacy> pharmacies) {
		this.pharmacies = pharmacies;
	}

	public Vector<Metadata> getMetadata() {
		return metadata;
	}

	public void setMetadata(Vector<Metadata> metadata) {
		this.metadata = metadata;
	}

	public void reader() {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		try {
			br = new BufferedReader(new FileReader(fileName));
			/* get metadata */
			line = br.readLine();
			String[] test = line.split(cvsSplitBy);

			Pharmacy pharmacyobj = new Pharmacy();
			Class<?> pharmacy = pharmacyobj.getClass();
			int i = 0;
			for (Field field : pharmacy.getDeclaredFields()) {
				// you can also use .toGenericString() instead of .getName(). This will
				// give you the type information as well.
				metadata.add(new Metadata(field.getName(), test[i], field.getType().toString()));
				i++;
			}
			/* end get metadata */
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

				test = line.split(cvsSplitBy);

				if (test[14].contains(",")) {
					test[14] = test[14].replace(',', '.');
				}
				if (test[14].contains("-")) {
					test[14] = test[14].replace('-', '0');
				}
				if (test[15].contains(",")) {
					test[15] = test[15].replace(',', '.');
				}
				if (test[15].contains("-")) {
					test[15] = test[15].replace('-', '0');
				}

				pharmacies.add(new Pharmacy(test[0], test[1], test[2], test[3], test[4], test[5], test[6], test[7],
						test[8], test[9], test[10], test[11], test[12], test[13], Double.parseDouble(test[14]),
						Double.parseDouble(test[15]), Double.parseDouble(test[16])));

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
					System.out.println("File closed!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}