package webservice;

import java.io.InputStream;

import data.SQLiteHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> 
{
	  ImageView bmImage;
	  private SQLiteHelper sqliteHelper;
	  private Integer userID;

	  public DownloadImageTask(ImageView bmImage, Context context, Integer userID) {
	      this.bmImage = bmImage;
	      sqliteHelper = SQLiteHelper.getInstance(context);
	      this.userID = userID;
	  }

	  protected Bitmap doInBackground(String... urls) {
	      String urldisplay = urls[0];
	      Bitmap mIcon11 = null;
	      try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	          Log.e("Error", e.getMessage());
	          e.printStackTrace();
	      }
	      
	      sqliteHelper.imageCache.put(userID, mIcon11);
	      return mIcon11;
	  }

	  protected void onPostExecute(Bitmap result) {
	      bmImage.setImageBitmap(result);
	  }
}