package exam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is used by both application and CSVParser. In the first case it
 * opens config file to get the url and the data-set destination. In the other
 * case it reads the CSV.
 *
 */
public class FileManager {
	private String fileName;
	private BufferedReader bufferedReader;

	public FileManager(String fileName, BufferedReader bufferedReader) {
		this.fileName = fileName;
		this.bufferedReader = bufferedReader;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	/**
	 * Method used to read text from a character-input stream, buffering characters.
	 * 
	 * @throws FileNotFoundException checks the presence of the file name
	 * @return bufferedReader the input stream buffer
	 */
	public BufferedReader openBufferStream() {
		try {
			bufferedReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bufferedReader;
	}

	/**
	 * Method used to read text from a character-input stream, buffering characters.
	 * 
	 * @throws General exception produced by failed or interrupted I/O operations.
	 * @return String str the read line
	 */
	public String readOneLine() {
		String str = "";
		try {
			str = bufferedReader.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * Method used to close an input stream.
	 * 
	 * @throws General exception produced by failed or interrupted I/O operations.
	 */
	public void fileClose() {
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}