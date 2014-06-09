package com.cs194.windowshopping;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class BarcodeResultsActivity extends Activity {
	private WishlistDataSource wds = new WishlistDataSource(this);
	private Typeface robotoFont;
	private ArrayList<ProductSearchHit> similarResults;
	private ProductSearch ps;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_barcode_results);
		Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String barcode = getIntent().getExtras().getString("barcode");
		new GetBarcodeResult().execute(barcode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barcode_results, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        MenuItem homeItem = menu.findItem(R.id.action_home);
		homeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_home:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		
		return false;
	}

	private void populateListView(ArrayList<ProductSearchHit> products) {
		similarResults = products;
		ArrayAdapter<ProductSearchHit> adapter = new MyBarcodeListAdapter();
		ListView list = (ListView) findViewById(R.id.results);
		list.setAdapter(adapter);
		Toast.makeText(this, ps.getKeyword(), Toast.LENGTH_SHORT).show();
	}
	

	private class MyBarcodeListAdapter extends ArrayAdapter<ProductSearchHit> {
		public MyBarcodeListAdapter() {
			super(BarcodeResultsActivity.this, R.layout.item_view, similarResults);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//Make sure we have a view to work with (may have been null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
			}
			//Find the ShoppingItem to work with.
			final ProductSearchHit currentItem = similarResults.get(position);
			
			//Fill the view
			ImageView img = (ImageView) itemView.findViewById(R.id.picture);
			img.setImageBitmap(currentItem.getImage());
			TextView title = (TextView) itemView.findViewById(R.id.textView1);
			title.setText(currentItem.getProductName());
			TextView price = (TextView) itemView.findViewById(R.id.textView2);
			price.setText(currentItem.getPrice());
			TextView brand = (TextView) itemView.findViewById(R.id.textView4);
			brand.setText(currentItem.getBrandName());
			TextView rating = (TextView) itemView.findViewById(R.id.textView5);
			rating.setText(currentItem.getRating());
			
			return itemView;
		}
		
	}
	
	private void registerClickCallback() {
		ListView list = (ListView) findViewById(R.id.results);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				ProductSearchHit clickedItem = similarResults.get(position);
				Intent intent = new Intent(BarcodeResultsActivity.this, RetailersActivity.class);
				intent.putExtra("name", clickedItem.getProductName());
				intent.putExtra("brand", clickedItem.getBrandName());
				intent.putExtra("retailers", clickedItem.getRetailer());
				intent.putExtra("reviews", clickedItem.getReviews());
				intent.putExtra("upc", clickedItem.getUPC());
				startActivity(intent);
			}
			
		});
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
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
		}
		
		@Override
		protected void onPostExecute(ProductSearchHit product) {
			setProgressBarIndeterminateVisibility(false);
			addProductToView(product);
			new GetSimilarResult().execute(product);
		}
	}
	
	private class GetSimilarResult extends AsyncTask<ProductSearchHit, Void, ArrayList<ProductSearchHit>> {
		
		@Override
		protected ArrayList<ProductSearchHit> doInBackground(ProductSearchHit... arg0) {
			ps = new ProductSearch();
			ps.findSimilarProducts(arg0[0]);
			ArrayList<ProductSearchHit> products = new ArrayList<ProductSearchHit>();
			for (int i = 0; i < ps.getNumberOfHits(); i++) {
				products.add(ps.getHit(i));
			}
		
			return products;
		}
		
		@Override
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
		}
		
		@Override
		protected void onPostExecute(ArrayList<ProductSearchHit> products) {
			populateListView(products);
			setProgressBarIndeterminateVisibility(false);
			registerClickCallback();		
		}
	}

}
