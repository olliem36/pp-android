package adapters;

import webservice.DownloadImageTask;

import com.theteachermate.app.R;

import data.SQLiteHelper;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildCursorAdapter extends CursorAdapter {

	private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SQLiteHelper sqliteHelper;
    
    public ChildCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context); 
        sqliteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.child_row, parent, false);
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) 
    {
        String firstname = c.getString(c.getColumnIndexOrThrow("firstname"));
        String surname = c.getString(c.getColumnIndexOrThrow("surname"));
        String dob = c.getString(c.getColumnIndexOrThrow("dob"));
        String fullname = firstname + " " + surname;
        int onlineUserID = c.getInt(c.getColumnIndexOrThrow("onlineUserID"));

        TextView title_text = (TextView) v.findViewById(R.id.item_text);
        if (title_text != null) {
            title_text.setText(fullname);
        }

        TextView date_text = (TextView) v.findViewById(R.id.item_date);
        if (date_text != null) {
            date_text.setText(dob);
        }       

        // set profile picture
        ImageView imageview = (ImageView) v.findViewById(R.id.img_profile);
        
        sqliteHelper.getProfilePicture(onlineUserID, imageview);
    }
}
