package exam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * This Class takes as attributes a file name and a url and is used to parse the
 * initial url in order to find another url that downloads the CSV file
 *
 */
public class JSONParser {
	private String fileName;
	private String url;

	public JSONParser(String fileName, String url) {
		super();
		this.fileName = fileName;
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * This method parse the url and search in the path result/resources/url to get
	 * the url that downloads the dataset
	 */
	public void openConnection() {
		// check if the file doesn't exist
		if (!Files.exists(Paths.get(fileName))) {
			try {
				// if the file doesn't exist open a connection with "url"
				URLConnection openConnection = new URL(url).openConnection();
				openConnection.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
				InputStream inputStream = openConnection.getInputStream();

				String data = "";
				String line = "";
				try {
					BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));

					while ((line = buffer.readLine()) != null) {
						//create a data String where the JSON is stored 
						data += line;
					}
					parser(data);
				} finally {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		else {
			System.out.println("File already exists!");
		}
	}

	public void parser(String str) {
		try {
			//parse the str String
			JSONObject JSONobj = (JSONObject) JSONValue.parseWithException(str);
			JSONObject JSONobj1 = (JSONObject) (JSONobj.get("result"));
			JSONArray JSONarray = (JSONArray) (JSONobj1.get("resources"));

			for (Object obj : JSONarray) {
				if (obj instanceof JSONObject) {
					JSONObject obj1 = (JSONObject) obj;
					String format = (String) obj1.get("format");
					String urlDataset = (String) obj1.get("url");
					if (format.equals("csv")) {
						System.out.println("Dataset url found!");
						System.out.println(format + " url : " + urlDataset);
						download(urlDataset, fileName);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method download the dataset if it doesn't exist.
	 * 
	 * @param url      not the attribute of the class but the one found by the
	 *                 parser
	 * @param fileName destination of the dataset
	 * @throws Exception checks the presence of the file name
	 */
	public static void download(String url, String fileName) throws Exception {
		try {
			//create input stream of bytes from the url
			InputStream inputStream = URI.create(url).toURL().openStream();
			//copy bytes from inputStream to fileName
			Files.copy(inputStream, Paths.get(fileName));
			System.out.println("CSV file downloaded!");
		} catch (FileAlreadyExistsException e) {
			System.out.println("File already exists!");
		}
	}
}
