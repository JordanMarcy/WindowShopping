package com.cs194.windowshopping;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//import com.ECS.client.jax.AWSECommerceService;
//import com.ECS.client.jax.AWSECommerceServicePortType;
//import com.ECS.client.jax.Item;
//import com.ECS.client.jax.ItemLookup;
//import com.ECS.client.jax.ItemLookupRequest;
//import com.ECS.client.jax.ItemLookupResponse;


/**
 * Contains product details for an query result from <code>ProductSearch</code>.
 * @author Stephen
 * @see	   ProductSearch
 */
public class ProductSearchHit {
	
	private String price, rating, name, brand, upc;
	private ArrayList<Retailer> retailers;
	private ArrayList<ProductReview> reviews = new ArrayList<ProductReview>();
	
	private Bitmap picture = null;
	
	/**
	 * 
	 * @param price
	 * @param rating
	 * @param name
	 * @param brand
	 * @param retailer
	 */
	ProductSearchHit(String price, String name, String brand,
			ArrayList<Retailer> retailers, String upc) {
		this.price = price;
		this.name = name;
		this.brand = brand;
		this.retailers = retailers;
		this.upc = upc;
		findAmazonProductDetails();
	}
	
	/**
	 * Returns the price of the product.
	 * @return price (including dollar sign)
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * Returns the rating of a product.
	 * @return	rating in the range [1,5]
	 */
	public String getRating() {
		return rating;
	}
	
	public int getNumberOfReviews() {
		return reviews.size();
	}
	
	public String getReview(int i) {
		return reviews.get(i).getText();
	}
	
	/**
	 * REturns the name of the product
	 * @return product name
	 */
	public String getProductName() {
		return name;
	}
	
	/**
	 * Returns the name of the brand of the product
	 * @return brand's name
	 */
	public String getBrandName() {
		return brand;
	}
	
	/**
	 * Returns the name of the retailer selling the product
	 * @return retailer's name
	 */
	public ArrayList<Retailer> getRetailer() {
		return retailers;
	}
	
	public Retailer getRetailerByIndex(int i) {
		return retailers.get(i);
	}
	
	
	/**
	 * Returns an image of the product.
	 * @return a bitmap or null if no image is available
	 */
	public Bitmap getImage() {
		return getPicture();
	}
	
	private void saveImage(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = client.execute(getRequest);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		if(entity != null) {
			try {
				setPicture(BitmapFactory.decodeStream(entity.getContent()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	private String findAmazonProductDetails() {
		final String ENDPOINT = "ecs.amazonaws.com";
		final String AWS_ASSOCIATE_TAG = "windows0a2-20";
		final String AWS_ACCESS_KEY_ID = "AKIAIZ3HLCPC3NDHUNDA";
		final String AWS_SECRET_KEY = "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";

		try {
			SignedRequestsHelper helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);

			Map<String, String> params 	= new HashMap<String, String>();
			params.put("Service", "AWSECommerceService");
			params.put("Operation", "ItemLookup");
			params.put("IdType", "UPC");
			params.put("SearchIndex", "All");
			params.put("ItemId", upc);
			params.put("AssociateTag", AWS_ASSOCIATE_TAG);
		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			findImageFromAmazon(helper, params, db);
			findReviewsFromAmazon(helper, params, db);

		} catch (InvalidKeyException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private void findReviewsFromAmazon(SignedRequestsHelper helper,
			Map<String, String> params, DocumentBuilder db) {
		params.put("ResponseGroup", "Reviews");
		String requestURL = helper.sign(params);
		try {
			org.w3c.dom.Document doc = db.parse(requestURL);
			org.jsoup.nodes.Document reviewsDoc;
			if(doc.getElementsByTagName("HasReviews").item(0).getTextContent().equals("true")) {
				reviewsDoc = Jsoup.connect(doc.getElementsByTagName("IFrameURL").item(0).getTextContent()).get();
				rating = reviewsDoc.getElementsByClass("asinReviewsSummary").get(0).child(0).child(0).attr("alt");
				
				//Elements reviewNodes = reviewsDoc.getElementsByClass("crlFrameReviewList").get(0).child(0).child(0).child(0).children();
				Elements reviewNodes = reviewsDoc.getElementsByClass("crlFrameReviewList");

				for(int i = 0; i < reviewNodes.size(); ++i) {
					Element element = reviewNodes.get(i);
					if(element.tagName() != "div") continue;
					String reviewText = element.child(3).text();
					String reviewRating = element.child(1).child(0).child(0).attr("alt");
					reviews.add(new ProductReview(reviewText, reviewRating));
				}
			} else
				rating = "";
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void findImageFromAmazon(SignedRequestsHelper helper, Map<String,String> params, DocumentBuilder db) {
		params.put("ResponseGroup", "Images");
		String requestURL = helper.sign(params);
		try {
			Document doc = db.parse(requestURL);			
			saveImage(doc.getElementsByTagName("MediumImage").item(0).getFirstChild().getTextContent());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
	
}

