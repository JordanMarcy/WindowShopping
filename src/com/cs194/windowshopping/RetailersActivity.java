package com.cs194.windowshopping;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RetailersActivity extends Activity {

	private ArrayList<Retailer> retailers = null;
	private String name, brandName;
	public WishlistDataSource wds = new WishlistDataSource(this);
	private Typeface robotoFont;

	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailers);
		Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		retailers = (ArrayList<Retailer>) getIntent().getSerializableExtra("retailer");
		name = getIntent().getExtras().getString("name");
		brandName = getIntent().getExtras().getString("brand");
		createHeader();
		populateListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.retailers, menu);
		return true;
	}

	private void createHeader() {
		TextView nameText = (TextView) this.findViewById(R.id.name);
		nameText.setText(name);
		TextView brandText = (TextView) this.findViewById(R.id.brand);
		brandText.setText(brandName);
	}
	
	private void populateListView() {
		ArrayAdapter<Retailer> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.results);
		list.setAdapter(adapter);
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
			System.out.print(position);
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
					wds.addItem(name, brandName, retailers.get(position));
					wds.close();
				}	
			});

			return itemView;
		}
		
	}
	
}
