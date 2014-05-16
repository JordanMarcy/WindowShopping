
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.semantics3.api.Products;

/**
 * Given an image or bar code query, searches for matching or similar products
 * and stores their information.
 * To use, first, create a new instance of the class.  Then, call <code>queryByBarcode</code>
 * or <code>queryByPicture</code> to populate it search results.  Call <code>getHit</code>
 * to get information on a specific result.
 * 
 * @author Stephen
 */
public class ProductSearch {
	
	//the hits returned from the query
	private ArrayList<ProductSearchHit> hits = new ArrayList<ProductSearchHit>();
	
	static private final String semantics3_apikey = "SEM35B45EE6B0FA9A38325DE96331FAF9274";
	static private final String semantics3_apisecret = "M2Q5YmRhYTNhMmQ1YWQ3ZjE4NDE2Yjc5MDM0NjExMTk";
	
	/**
	 * class constructor
	 */
	ProductSearch() {}
	
	/**
	 * Executes a query for a product, finds retailers offering it, and stores 
	 * the results.
	 * @param 	filename	the file location of the image containing a barcode		
	 */
	public void queryByBarcode(String filename) {
		String barcode = GetBarcodeFromImage(filename);
		findBarcodeSearchResults(barcode);
	}
	
	/**
	 * Executes a query for similar looking products and stores the results.
	 * @param 	filename	the file location of an image of a product
	 */
	public void queryByPicture(String filename) {
		String keyword = GetKeywordFromImage(filename);
		findKeywordSearchResults(keyword);
	}
	
	/**
	 * Returns the number of hits found after calling <code>queryByBarcode</code>
	 * or <code>queryByPicture</code>.
	 * @return
	 */
	public int getNumberOfHits() {
		//TODO
		return hits.size();
	}
	
	/**
	 * Get a specific result after making a query.
	 * @param 	i	Returns the <code>i</code>th hit. Call <code>getNumberOfHits()</code>
	 * 				to check prevent an out of bounds exception.
	 * @throws	ArrayOutofBoundsException
	 * @return
	 */
	public ProductSearchHit getHit(int i) {
		return hits.get(i);
	}
	
	/**
	 * Averages the prices for all of the search results.
	 * @return	the average price (with a dollar sign)
	 */
	public String getAveragePrice() {
		//TODO
		return hits.get(0).getPrice();
	}
	
	/**
	 * Sorts the query results from highest to lowest rating.
	 */
	public void sortHitsByRating() {
		//TODO
	}
	
	/**
	 * Sorts the query results from lowest to highest price.
	 */
	public void sortHitsByPrice() {
		//TODO
	}
	
	static private String GetBarcodeFromImage(String filename) {
		return "049000056976";
	}
	
	private void findBarcodeSearchResults(String barcode) {
		
	}
	
	static private String GetKeywordFromImage(String filename) {
		HttpClient client = HttpClients.createDefault();
		String token = getCamFindToken(client, filename);
		return getCamFindKeyword(client, token);
	}
	
	private void findKeywordSearchResults(String keyword) {
		Products products = new Products(semantics3_apikey, semantics3_apisecret);
		products.productsField("search", keyword);		
		try {
			JSONObject results;
			results = products.getProducts();
			parseJSONSearchResults(results);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void parseJSONSearchResults(JSONObject results) {
		try {
			JSONArray products = results.getJSONArray("results");
			for(int i = 0; i < products.length(); ++i) {
				JSONObject product = products.getJSONObject(i);
				JSONArray retailers = product.getJSONArray("sitedetails");
				String retailer = retailers.getJSONObject(0).getString("name");
				hits.add(new ProductSearchHit(
					product.getString("price"),
					"2.0",
					product.getString("name"),
					product.getString("brand"),
					retailer,
					product.getString("upc")
				));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	static private String getCamFindToken(HttpClient client, String imageFilename) {
		HttpPost httppost = new HttpPost("https://camfind.p.mashape.com/image_requests");
		httppost.addHeader("X-Mashape-Authorization", "Xe27RaykrrRyUydb20Ckw02sBLfMvRQZ");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("image_request[locale]", "en_US");
		builder.addBinaryBody("image_request[image]", new File(imageFilename));
		httppost.setEntity(builder.build());
		
		try {
			org.apache.http.HttpResponse response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			
			if(entity != null) {
				String str = ReadFileFromStream(entity.getContent());
				
				JSONObject jsonObj = new JSONObject(str);
				return jsonObj.getString("token");
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}		
		return "";
	}
	
	static private String getCamFindKeyword(HttpClient client, String token) {
		String url = "https://camfind.p.mashape.com/image_responses/" + token;
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("X-Mashape-Authorization", "Xe27RaykrrRyUydb20Ckw02sBLfMvRQZ");
		
		try {
			while(true) {
				Thread.sleep(5000);
				
				org.apache.http.HttpResponse response = client.execute(httpget);
				HttpEntity entity = response.getEntity();
							
				if(entity != null) {
					String str = ReadFileFromStream(entity.getContent());
					System.out.println(str);
					JSONObject jsonObj = new JSONObject(str);
					if(jsonObj.getString("status").equals("completed")) 
						return jsonObj.getString("name");
				}
			}
		} catch (IOException | JSONException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	static private String ReadFileFromStream(InputStream istream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
		
		String line;
		StringBuffer strbuf = new StringBuffer();
		while((line = reader.readLine()) != null) {
			strbuf.append(line);
		}
		
		reader.close();
		return strbuf.toString();
	}
}