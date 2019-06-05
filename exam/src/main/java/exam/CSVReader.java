package exam;

import java.io.*;
import java.util.Vector;

public class CSVReader {
	private static String fileName;

	public CSVReader(String fileName) {
		fileName = this.fileName;
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		CSVReader.fileName = fileName;
	}

	public static void reader() {

		String csvFile = fileName;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		Vector<Pharmacy> pharmacies = new Vector<Pharmacy>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {

				String[] test = line.split(cvsSplitBy);
				// for (String s : test) {
				///////////////////////////////////////
				/*
				 * try{ float i = Float.valueOf(str); //Float.parseFloat(str);
				 * 
				 * System.out.println("Value parsed :"+i); }catch(NumberFormatException ex){
				 * System.err.println("Ilegal input"); // Discard input or request new input ...
				 * // clean up if necessary }
				 */
				////////////////////////////////////////
				for (int i = 0; i < test.length; i++) {

					if (test[i].contains(",")) {
						test[i] = test[i].replace(',', '.');
						System.out.println(test[i]);
					}
					if (test[i].contains(";")) {
						test[i] = test[i].replace(';', '.');
						System.out.println(test[i]);
					}

				}

				pharmacies.add(new Pharmacy(Double.parseDouble(test[0]), test[1], test[2], Double.parseDouble(test[3]),
						Double.parseDouble(test[4]), Double.parseDouble(test[5]), test[6], Double.parseDouble(test[7]),
						test[8], test[9], Double.parseDouble(test[10]), test[11], test[12], test[13],  Double.parseDouble(test[14]),  Double.parseDouble(test[15]),
						Double.parseDouble(test[16])));
			//for (Pharmacy item : pharmacies) {
			//	System.out.println(pharmacies.toString());

			//	}
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