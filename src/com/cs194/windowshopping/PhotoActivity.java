package com.cs194.windowshopping;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoActivity extends Activity {

	private static final int CAMERA_PIC_REQUEST = 1;
	private static final int BARCODE_SCAN_REQUEST = 2;
	private String mCurrentPhotoPath;
	private File imageFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo, menu);
		return true;
	}
	
	public void takePhoto(View view) {
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
	
	public void searchImage(View view) {
		Intent intent = new Intent(view.getContext(), ResultsActivity.class);
		intent.putExtra("photoFile", mCurrentPhotoPath);
		startActivity(intent);
	}
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
			if (bitmap != null) {
				ImageView imageView = (ImageView) findViewById(R.id.photoView);
				imageView.setImageBitmap(bitmap);
			} else {
				Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show();
			}
			

		}
    }

}
