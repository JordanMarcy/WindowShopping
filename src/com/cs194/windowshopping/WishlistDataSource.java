package com.cs194.windowshopping;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;

public class WishlistDataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PRICE, 
			MySQLiteHelper.COLUMN_URL};
	
	
	public WishlistDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void addItem(String name, String brandName, Retailer retailer) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_PRICE, retailer.getPrice());
		values.put(MySQLiteHelper.COLUMN_URL, brandName);
		
		long insertID = database.insert(MySQLiteHelper.TABLE_WISHLIST, null, values);
	}
	
	public void deleteItem(ShoppingItem item) {
		String url = item.getUrl();
		UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
		sanitizer.setAllowUnregisteredParamaters(true);
		sanitizer.parseUrl(url);
		database.delete(MySQLiteHelper.TABLE_WISHLIST, MySQLiteHelper.COLUMN_URL + " = \'" + url + "\'", null);
	}
	
	public List<ShoppingItem> getAllItems() {
		List<ShoppingItem> items = new ArrayList<ShoppingItem>();
		Cursor cursor = database.rawQuery("select distinct * from " + MySQLiteHelper.TABLE_WISHLIST, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			items.add(new ShoppingItem(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
			cursor.moveToNext();
		}
		cursor.close();
		return items;
		
	}
	
}
