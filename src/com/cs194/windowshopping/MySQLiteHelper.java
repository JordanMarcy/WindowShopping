package com.cs194.windowshopping;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_WISHLIST = "wishlist";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_URL = "url";
	
	private static final String DATABASE_NAME = "wishlist.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_WISHLIST + "(" 
			+ COLUMN_NAME + " text not null, " 
			+ COLUMN_PRICE + " text not null, " 
			+ COLUMN_URL + " text not null);";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
	    onCreate(db);
	}

}
