package com.cs194.windowshopping;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class BarcodeResultsActivity extends Activity {
	private WishlistDataSource wds = new WishlistDataSource(this);
	private Typeface robotoFont = Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_results);
		String barcode = getIntent().getExtras().getString("barcode");
		new GetBarcodeResult().execute(barcode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barcode_results, menu);
		return true;
	}
	
	private void addProductToView(ProductSearchHit product) {
		if (product == null) return;
		ImageView image = (ImageView) findViewById(R.id.productImage);
		image.setImageBitmap(product.getPicture());
		TextView name = (TextView) findViewById(R.id.productName);
		name.setText(product.getProductName());
		TextView price = (TextView) findViewById(R.id.productPrice);
		price.setText("$" + product.getPrice());
		TextView brand = (TextView) findViewById(R.id.productBrand);
		brand.setText(product.getBrandName());
		TextView rating = (TextView) findViewById(R.id.productRating);
		rating.setText(product.getRating());
		TextView review = (TextView) findViewById(R.id.productReview);
		if (product.getNumberOfReviews() > 0) {
			review.setText(product.getReview(0));
		}
		
		
	}
	
	private class GetBarcodeResult extends AsyncTask<String, Void, ProductSearchHit> {
		
		@Override
		protected ProductSearchHit doInBackground(String... arg0) {
			ProductSearchHit product = null;
			ProductSearch ps = new ProductSearch();
			ps.queryByBarcode(arg0[0]);
			if (ps.getNumberOfHits() > 0) {
				product = ps.getHit(0);
			}
		
			return product;
		}
		
		@Override
		protected void onPostExecute(ProductSearchHit product) {
			addProductToView(product);
			
		}
	}

}
