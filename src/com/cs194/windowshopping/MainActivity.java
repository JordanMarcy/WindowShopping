package com.cs194.windowshopping;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /*Called when a user clicks the Take Photo button*/
    public void gotoCamera(View view) {
    	Intent intent = new Intent(this, PhotoActivity.class);
    	startActivity(intent);
    }
    
    
    
    public void onPhotoTaken() {
    	
    }
    
    /*Called when a user clicks the View Wishlist button*/
    public void gotoWishlist(View view) {
    	Intent intent = new Intent(this, WishlistActivity.class);
    	startActivity(intent);
    }
    
    /*Called when a user clicks the Scan Barcode button*/
    public void gotoScanner(View view) {
    	
    }
    
}
