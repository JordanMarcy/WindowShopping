package com.cs194.windowshopping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity {
	public WishlistDataSource wds = new WishlistDataSource(this);
	public ProductSearch ps;
	public List<ProductSearchHit> results;
	private Typeface robotoFont = Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		String source = getIntent().getExtras().getString("source");
		if (source.equals("text")) {
			String searchTerm = getIntent().getExtras().getString("searchTerm");
			new PopulateResultListText().execute(searchTerm);
		} else {
			String photoFile = getIntent().getExtras().getString("photoFile");
			new PopulateResultList().execute(photoFile);
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}
	
	private void populateListView(ArrayList<ProductSearchHit> products) {
		results = products;
		ArrayAdapter<ProductSearchHit> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.results);
		list.setAdapter(adapter);
		Toast.makeText(this, ps.getKeyword(), Toast.LENGTH_SHORT).show();
	}
	

	private class MyListAdapter extends ArrayAdapter<ProductSearchHit> {
		public MyListAdapter() {
			super(ResultsActivity.this, R.layout.item_view, results);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//Make sure we have a view to work with (may have been null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
			}
			//Find the ShoppingItem to work with.
			final ProductSearchHit currentItem = results.get(position);
			
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
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				ProductSearchHit clickedItem = results.get(position);
				Intent intent = new Intent(ResultsActivity.this, RetailersActivity.class);
				intent.putExtra("retailer", clickedItem.getRetailer());
				intent.putExtra("name", clickedItem.getProductName());
				intent.putExtra("brand", clickedItem.getBrandName());
				startActivity(intent);
			}
			
		});
	}
	
	private class PopulateResultList extends AsyncTask<String, Void, ArrayList<ProductSearchHit>> {
		
		@Override
		protected ArrayList<ProductSearchHit> doInBackground(String... arg0) {
			ArrayList<ProductSearchHit> products = new ArrayList<ProductSearchHit>();
			ps = new ProductSearch();
			ps.queryByPicture(arg0[0]);
			
			for (int i = 0; i < ps.getNumberOfHits(); i++) {
				products.add(ps.getHit(i));
			}
			new File(arg0[0]).delete(); //Delete photo from storage after search is over.
			
			return products;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ProductSearchHit> products) {
			populateListView(products);
			registerClickCallback();			
		}
	}
	
	private class PopulateResultListText extends AsyncTask<String, Void, ArrayList<ProductSearchHit>> {
		
		@Override
		protected ArrayList<ProductSearchHit> doInBackground(String... arg0) {
			ArrayList<ProductSearchHit> products = new ArrayList<ProductSearchHit>();
			ps = new ProductSearch();
			ps.queryByText(arg0[0]);
			
			for (int i = 0; i < ps.getNumberOfHits(); i++) {
				products.add(ps.getHit(i));
			}
			new File(arg0[0]).delete(); //Delete photo from storage after search is over.
			
			return products;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ProductSearchHit> products) {
			populateListView(products);
			registerClickCallback();			
		}
	}

	
	
}
