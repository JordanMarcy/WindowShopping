package com.cs194.windowshopping;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	public WishlistDataSource wds = new WishlistDataSource(this);
	public List<ProductSearchHit> results;
	private ProductSearch ps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		String photoFile = getIntent().getExtras().getString("photoFile");
		new PopulateResultList().execute(photoFile);
		
		//registerClickCallback();
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
			TextView title = (TextView) itemView.findViewById(R.id.textView1);
			title.setText(currentItem.getProductName());
			TextView price = (TextView) itemView.findViewById(R.id.textView2);
			price.setText(currentItem.getPrice());
			TextView rating = (TextView) itemView.findViewById(R.id.textView3);
			rating.setText(currentItem.getRating());
			TextView brand = (TextView) itemView.findViewById(R.id.textView4);
			brand.setText(currentItem.getBrandName());
			TextView retailer = (TextView) itemView.findViewById(R.id.textView5);
			retailer.setText(currentItem.getRetailerName());
			Button save = (Button) itemView.findViewById(R.id.button1);
			save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					wds.open();
					wds.addItem(results.get(position));
					wds.close();
				}	
			});

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
				String url = clickedItem.getProductName();
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			
		});
	}
	
	private class PopulateResultList extends AsyncTask<String, Void, ArrayList<ProductSearchHit>> {
		
		@Override
		protected ArrayList<ProductSearchHit> doInBackground(String... arg0) {
			ArrayList<ProductSearchHit> products = new ArrayList<ProductSearchHit>();
			ProductSearch ps = new ProductSearch();
			ps.queryByPicture(arg0[0]);
			
			for (int i = 0; i < ps.getNumberOfHits(); i++) {
				products.add(ps.getHit(i));
			}
			return products;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ProductSearchHit> products) {
			populateListView(products);
		}
	}

	
	
}
