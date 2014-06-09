package com.cs194.windowshopping;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends Activity {
	
	private static final int CAMERA_PIC_REQUEST = 1;
	private static final int BARCODE_SCAN_REQUEST = 2;
	private String mCurrentPhotoPath;
	private Typeface robotoFont;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface.createFromAsset(getAssets(), "fonts/robotolight.ttf");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));        

        return true;
    }
    
    /*Called when a user clicks the Take Photo button*/
    public void gotoCamera(View view) {
    	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	if (cameraIntent.resolveActivity(getPackageManager()) != null) {
    		File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
    	}
    }
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String imageFileName = "WindowShopping";
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(imageFileName, ".jpg", storageDir);

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
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
    	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
    	startActivityForResult(intent, BARCODE_SCAN_REQUEST);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
    		Intent resultsIntent = new Intent(this, ResultsActivity.class);
    		resultsIntent.putExtra("photoFile", mCurrentPhotoPath);
    		resultsIntent.putExtra("source", "photo");
    		startActivity(resultsIntent);
    	} else if (requestCode == BARCODE_SCAN_REQUEST && resultCode == RESULT_OK) {
   			String contents = intent.getStringExtra("SCAN_RESULT");
   			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
   			System.out.println(format);
  			Intent resultsIntent = new Intent(this, BarcodeResultsActivity.class);
   			resultsIntent.putExtra("barcode", contents);
   			startActivity(resultsIntent);
    	}
    }
    
}
