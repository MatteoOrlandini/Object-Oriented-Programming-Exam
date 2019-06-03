package hello;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class Download {

	public Download(String[] args) throws MalformedURLException, IOException {
		String FILE_URL = "https://www.dati.gov.it/api/3/action/package_show?id=3c68b286-09fd-447a-b8e3-1b8430f70969";
		String FILE_NAME = "Prova.txt";
		URLConnection connection = new URL(FILE_URL).openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.connect();

		BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME)));

		String line;
		while ((line = r.readLine()) != null) {
			out.println(line);
		}
		
		// JSONObject 
		out.close();
		System.out.println("closed");
	}

}
