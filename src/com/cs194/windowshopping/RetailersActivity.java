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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class RetailersActivity extends Activity {

	private ProductSearchHit psh;
	private ArrayList<Retailer> retailers;
	private ArrayList<ProductReview> reviews;
	public WishlistDataSource wds = new WishlistDataSource(this);
	private Typeface robotoFont;

	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_retailers);
		Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		retailers = (ArrayList<Retailer>) getIntent().getSerializableExtra("retailers");
		reviews = (ArrayList<ProductReview>) getIntent().getSerializableExtra("reviews");
		psh = new ProductSearchHit(extras.getString("name"), extras.getString("brand"),
				retailers, reviews, extras.getString("upc"));
		createHeader();
		populateListView();
		if (reviews == null) {
			System.out.println("reviews are null");
			new GetAmazonReviews().execute(psh);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.retailers, menu);
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
			onBackPressed();
			return true;
		case R.id.action_home:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		
		return false;
	}

	private void createHeader() {
		TextView nameText = (TextView) this.findViewById(R.id.name);
		nameText.setText(psh.getProductName());
		TextView brandText = (TextView) this.findViewById(R.id.brand);
		brandText.setText(psh.getBrandName());
	}
	
	private void populateListView() {
		ArrayAdapter<Retailer> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.results);
		list.setAdapter(adapter);
	}
	
	private void populateReviewListView() {
		if (psh.getNumberOfReviews() > 0) {
			reviews = psh.getReviews();
			ArrayAdapter<ProductReview> adapter = new MyReviewListAdapter();
			ListView list = (ListView) findViewById(R.id.reviews);
			list.setAdapter(adapter);
		}
		
	}
	
	private class MyReviewListAdapter extends ArrayAdapter<ProductReview> {
		public MyReviewListAdapter() {
			super(RetailersActivity.this, R.layout.item_view, reviews);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.review_item_view, parent, false);
			}
			final ProductReview currentReview = psh.getReviews().get(position);
			TextView review = (TextView) itemView.findViewById(R.id.review_text);
			review.setText(currentReview.getText());
			return itemView;
					
					
		}
	}

	private class MyListAdapter extends ArrayAdapter<Retailer> {
		public MyListAdapter() {
			super(RetailersActivity.this, R.layout.item_view, retailers);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//Make sure we have a view to work with (may have been null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.retailer_item_view, parent, false);
			}
			//Find the ShoppingItem to work with.
			final Retailer currentItem = retailers.get(position);
			
			//Fill the view
			TextView retailer = (TextView) itemView.findViewById(R.id.textView1);
			retailer.setText(currentItem.getRetailer());
			TextView price = (TextView) itemView.findViewById(R.id.textView2);
			price.setText(currentItem.getPrice());
			Button save = (Button) itemView.findViewById(R.id.button1);
			save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					wds.open();
					wds.addItem(psh.getProductName(), psh.getBrandName(), retailers.get(position));
					wds.close();
				}	
			});

			return itemView;
		}
		
	}
	
	private class GetAmazonReviews extends AsyncTask<ProductSearchHit, Void, ProductSearchHit> {

		@Override
		protected ProductSearchHit doInBackground(ProductSearchHit... params) {
			params[0].findAmazonProductDetails(false);
			return params[0];
		}
		
		@Override
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected void onPostExecute(ProductSearchHit psh) {
			setProgressBarIndeterminateVisibility(false);
			populateReviewListView();
		}


	}
	
}
