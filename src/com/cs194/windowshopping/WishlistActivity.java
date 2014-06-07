package com.cs194.windowshopping;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WishlistActivity extends Activity {
	public WishlistDataSource wds = new WishlistDataSource(this);
	public List<ShoppingItem> wishlist = new ArrayList<ShoppingItem>();
	private Typeface robotoFont = Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wishlist);
		populateWishList();
		populateListView();
		registerClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wishlist, menu);
		return true;
	}
	
	
	private void populateWishList() {
		wds.open();
		wishlist = wds.getAllItems();
		wds.close();
	}
	
	private void populateListView() {
		ArrayAdapter<ShoppingItem> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.results);
		list.setAdapter(adapter);
	}
	

	private class MyListAdapter extends ArrayAdapter<ShoppingItem> {
		public MyListAdapter() {
			super(WishlistActivity.this, R.layout.item_view, wishlist);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//Make sure we have a view to work with (may have been null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.retailer_item_view, parent, false);
			}
			//Find the ShoppingItem to work with.
			final ShoppingItem currentItem = wishlist.get(position);
			
			//Fill the view
			TextView title = (TextView) itemView.findViewById(R.id.textView1);
			title.setText(currentItem.getName());
			TextView price = (TextView) itemView.findViewById(R.id.textView2);
			price.setText(currentItem.getPrice());
			Button remove = (Button) itemView.findViewById(R.id.button1);
			remove.setText("Remove from Wishlist");
			remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					wds.open();
					wds.deleteItem(wishlist.get(position));
					wds.close();
					finish();
					startActivity(getIntent());
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
				ShoppingItem clickedItem = wishlist.get(position);
				String url = clickedItem.getUrl();
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			
		});
	}

}
