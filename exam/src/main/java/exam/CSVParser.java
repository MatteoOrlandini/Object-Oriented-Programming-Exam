package exam;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * used to read a CSV file and fill a {@link Pharmacy} and a {@link Metadata}
 * vectors
 */
public class CSVParser {
	private String fileName;
	private Vector<Pharmacy> pharmacies;
	private Vector<Metadata> metadata;

	public CSVParser(String fileName, Vector<Pharmacy> pharmacies, Vector<Metadata> metadata) {
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
	 * This method read the file through the field fileName and parse it in order to
	 * fill the vectors pharmacies and metadata
	 * 
	 * @throws NumberFormatException thrown to indicate that the application has
	 *                               attempted to convert a string to one of the
	 *                               numeric types, but that the string does not
	 *                               have the appropriate format.
	 * @throws FileNotFoundException if the file is not found
	 * @throws IOException           exception produced by failed or interrupted I/O
	 *                               operations
	 */
	public void reader() {

		BufferedReader bufferedReader = null;
		String line = "";
		String semicolon = ";";

		FileManager fileManager = new FileManager(fileName, bufferedReader);
		fileManager.openBufferStream();
		System.out.println("File opened!");
		line = fileManager.readOneLine();
		String[] splittedLine = line.split(semicolon);

		Pharmacy pharmacyobj = new Pharmacy();
		Class<?> pharmacy = pharmacyobj.getClass();
		
		int i = 0;
		for (Field field : pharmacy.getDeclaredFields()) {
			// you can also use .toGenericString() instead of .getName(). This will
			// give you the type information as well.
			try {
				metadata.add(new Metadata(field.getName(), splittedLine[i], field.getType().toString()));
				i++;
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Index out of bound");
			}
			while ((line = fileManager.readOneLine()) != null) {
				line = lineCorrection(line);
				splittedLine = line.split(semicolon);
				splittedLine = coordinateCorrection(splittedLine);
				try {
					pharmacies.add(new Pharmacy(splittedLine[0], splittedLine[1], splittedLine[2], splittedLine[3],
							splittedLine[4], splittedLine[5], splittedLine[6], splittedLine[7], splittedLine[8],
							splittedLine[9], splittedLine[10], splittedLine[11],stringToDate(splittedLine[12]), stringToDate(splittedLine[13]),
							Double.parseDouble(splittedLine[14]), Double.parseDouble(splittedLine[15]),
							Integer.parseInt(splittedLine[16])));
				} catch (NumberFormatException ex) {
					System.err.println("Illegal input");

				}
			}
			System.out.println("Dataset loaded!");
		}
	}


	/**
	 * Function to corrects the misspelled fields in the CSV There are 2 cases in
	 * which there are semicolons instead of commas that the function reader can't
	 * read properly. The other case is an apex in front of a number that makes
	 * impossible the parsing from String to double.
	 * 
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
	 * This function corrects the latitude and longitude fiels by replacing the
	 * comma with a point and the dash with a value non allowed like -360.
	 * 
	 * @param str line read by the {@link reader}
	 * @return str2 corrected line
	 */
	public String[] coordinateCorrection(String[] str) {
		String[] str2 = str;

		for (int i = 14; i <= 15; i++) {
			if (str[i].contains(","))
				str2[i] = str[i].replace(',', '.');

			if (str[i].equals("-"))
				str2[i] = str[i].replace("-", "-360");
			if (str[i].equals("0"))
				str2[i] = str[i].replace("0", "-360");
		}
		return str2;
	}
	/**
	 * Converts a String in a Date Object if it's not a dash.
	 * 
	 * @param str
	 * @return Date equivalent
	 */
	public Date stringToDate(String str) {
		Date date = null;
		if (!str.equals("-")) {
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
			} catch (ParseException e) {
				System.out.println("Parse error from string " + str + " to date");
				e.printStackTrace();
			}
		}
		return date;
	}
}