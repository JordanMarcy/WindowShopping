package com.cs194.windowshopping;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity {
	public WishlistDataSource wds = new WishlistDataSource(this);
	public List<ShoppingItem> results = new ArrayList<ShoppingItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		populateResultList();
		populateListView();
		registerClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}
	
	private void populateResultList() {
		results.add(new ShoppingItem("Hemnes", "$119.00", "http://www.ikea.com/us/en/catalog/products/80176284/#"));
		results.add(new ShoppingItem("Lack Coffee Table", "$49.89", "http://www.amazon.com/IKEA-Lack-Coffee-Table-Black/dp/B004ZHEB0O/ref=lp_3733631_1_5?s=furniture&ie=UTF8&qid=1399561337&sr=1-5"));
		results.add(new ShoppingItem("Madera Coffee Table", "$249.99", "http://www.worldmarket.com/product/madera-coffee-table.do"));
	}
	
	private void populateListView() {
		ArrayAdapter<ShoppingItem> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.results);
		list.setAdapter(adapter);
	}
	

	private class MyListAdapter extends ArrayAdapter<ShoppingItem> {
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
			final ShoppingItem currentItem = results.get(position);
			
			//Fill the view
			TextView title = (TextView) itemView.findViewById(R.id.textView1);
			title.setText(currentItem.getName());
			TextView price = (TextView) itemView.findViewById(R.id.textView2);
			price.setText(currentItem.getPrice());
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
				ShoppingItem clickedItem = results.get(position);
				String url = clickedItem.getUrl();
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			
		});
	}
	
}
