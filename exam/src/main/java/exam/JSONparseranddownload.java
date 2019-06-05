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

public class JSONparseranddownload  {
	private static String fileName;
	private static String url;

	

	public JSONparseranddownload(String fileName, String url) {
		super();
		this.fileName = fileName;
		this.url = url;
	}
	
	

	public static String getFileName() {
		return fileName;
	}



	public static void setFileName(String fileName) {
		JSONparseranddownload.fileName = fileName;
	}



	public static String getUrl() {
		return url;
	}



	public static void setUrl(String url) {
		JSONparseranddownload.url = url;
	}



	public static void parser(){
		
		try {
			
			URLConnection openConnection = new URL(url).openConnection();
			openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			InputStream in = openConnection.getInputStream();
			
			 String data = "";
			 String line = "";
			 try {
			   InputStreamReader inR = new InputStreamReader( in );
			   BufferedReader buf = new BufferedReader( inR );
			  
			   while ( ( line = buf.readLine() ) != null ) {
				   data+= line;
				   //System.out.println( line );
			   }
			 } finally {
			   in.close();
			 }
			JSONObject obj = (JSONObject) JSONValue.parseWithException(data); 
			JSONObject objI = (JSONObject) (obj.get("result"));
			JSONArray objA = (JSONArray) (objI.get("resources"));
			
			for(Object o: objA){
			    if ( o instanceof JSONObject ) {
			        JSONObject o1 = (JSONObject)o; 
			        String format = (String)o1.get("format");
			        String urlDataset = (String)o1.get("url");
			        System.out.println(format + " url : " + urlDataset);
			        if(format.equals("csv")) {
						System.out.println( "Dataset found!" );
			        	download(urlDataset, fileName);
			        }
			    }
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void download(String url, String fileName) throws Exception {
	    try (InputStream in = URI.create(url).toURL().openStream()) {
		        Files.copy(in, Paths.get(fileName));
				System.out.println( "Dataset downloaded!" );
	    }
		catch (FileAlreadyExistsException e) {
			System.out.println ("File already exists!");
		}
	}
}
