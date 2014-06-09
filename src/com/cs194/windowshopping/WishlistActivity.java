package com.cs194.windowshopping;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class WishlistActivity extends Activity {
	public WishlistDataSource wds = new WishlistDataSource(this);
	public List<ShoppingItem> wishlist = new ArrayList<ShoppingItem>();
	private Typeface robotoFont;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wishlist);
		robotoFont = Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		populateWishList();
		populateListView();
		registerClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wishlist, menu);
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
			Button buy = (Button) itemView.findViewById(R.id.buy_button);
			buy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wishlist.get(position).getUrl()));
					startActivity(browserIntent);
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
