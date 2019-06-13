package exam;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * used to read a CSV file and fill a {@link Pharmacy} and a {@link Metadata} vectors
 */
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
	/**
	 * This method read the file through the field fileName 
	 * and parse it in order to fill the vectors pharmacies and metadata
	 * @throws NumberFormatException thrown to indicate that the application 
	 * has attempted to convert a string to one of the numeric types,
	 * but that the string does not have the appropriate format.
	 * @throws FileNotFoundException if the file is not found
	 * @throws IOException exception produced by failed or interrupted I/O operations
	 */
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

			while ((line = br.readLine()) != null) {
				line = lineCorrection(line);
				test = line.split(cvsSplitBy);
				test = coordinateCorrection(test);

				pharmacies.add(new Pharmacy(test[0], test[1], test[2], test[3], test[4], test[5], test[6], test[7],
						test[8], test[9], test[10], test[11], test[12], test[13], Double.parseDouble(test[14]),
						Double.parseDouble(test[15]), Integer.parseInt(test[16])));
			}
		}

		catch (NumberFormatException ex) {
			System.err.println("Illegal input");

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		finally {
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
	
	/**
	 * Function to corrects the misspelled fields in the CSV
	 * There are 2 cases in which there are semicolons instead of commas that the function reader
	 * can't read properly. The other case is an apex in front of a number that makes 
	 * impossible the parsing from String to double.
	 * @param str line read by the {@link reader}
	 * @return str2 corrected line
	 */
	public String lineCorrection(String str) {
		String str2 = str;
		if (str.contains("\"Via Cappuccini ;163\"")) {
			str2 = str.replace("\"Via Cappuccini ;163\"", "\"Via Cappuccini ,163\"");
		}
		if (str.contains("\"via vespucci;26\"")) {
			str2 = str.replace("\"via vespucci;26\"", "\"via vespucci,26\"");
		}
		if (str.contains("\'9,258444")) {
			str2 = str.replace("\'9,258444", "9.258444");
		}
		return str2;
	}
	/**
	 * This function corrects the latitude and longitude fiels by replacing
	 * the comma with a point and the dash with a zero.
	 * @param str line read by the {@link reader}
	 * @return str2 corrected line
	 */
	public String[] coordinateCorrection(String[] str) {
		String[] str2 = str;
		if (str[14].contains(",") || str[14].contains("-")) {
			for (int i = 14; i <= 15; i++) {
				str2[i] = str[i].replace(',', '.');
				str2[i] = str[i].replace('-', '0');
			}
		}
		return str2;
	}
}