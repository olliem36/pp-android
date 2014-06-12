package dialogs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import interfaces.NewFolderInterface;

import com.theteachermate.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewFileDialog extends DialogFragment {

	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private static final int IMPORT_GALLERY_ACTIVITY_REQUEST_CODE = 300;
	
	private Uri fileUri;
	public String mediaGUID;
	
	private Button btnCaptureVideo;
	private Button btnCapturePhoto;
	private Button btnImportGallery;

	//public NewFolderInterface callback;
	
	public String getMediaGUID()
	{
		if(mediaGUID == null) Log.e("pp", "ERROR, media GUID for new file is null");
		return mediaGUID;
	}
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
        View view = inflater.inflate(R.layout.dialog_new_file, container);
        
        btnCaptureVideo = (Button) view.findViewById(R.id.btnCaptureVideo);
        btnCapturePhoto = (Button) view.findViewById(R.id.btnCapturePhoto);
        btnImportGallery = (Button) view.findViewById(R.id.btnImportGallery);
		
        getDialog().setTitle("New File");
        		
		SharedPreferences prefs = this.getActivity().getApplicationContext().getSharedPreferences("com.theteachermate.app", Context.MODE_PRIVATE);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		btnCapturePhoto.setOnClickListener(BtnCapturePhotoClickListener);
		btnCaptureVideo.setOnClickListener(BtnCaptureVideoClickListener);
		btnImportGallery.setOnClickListener(BtnImportGalleryClickListener);

		
        return view;
    }
	
	
	private OnClickListener BtnCapturePhotoClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {

	    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
	    	
	    	intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
	    	
	        getActivity().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    }
	};
	
	private OnClickListener BtnCaptureVideoClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {

	    	Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

	        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
	        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
	        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

	        // start the Video Capture Intent
	        getActivity().startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
	    }
	};
	
	private OnClickListener BtnImportGalleryClickListener = new OnClickListener() {
	    @Override
	    public void onClick(final View v) {

	    	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	    	photoPickerIntent.setType("image/*,video/*");
	    	getActivity().startActivityForResult(photoPickerIntent, IMPORT_GALLERY_ACTIVITY_REQUEST_CODE);
	    }
	    
	};
	
	private Uri getImageUri() {
	    File file = new File(Environment.getExternalStorageDirectory() + "/.PP", mediaGUID);
	    Uri imgUri = Uri.fromFile(file);

	    return imgUri;
	}
}
