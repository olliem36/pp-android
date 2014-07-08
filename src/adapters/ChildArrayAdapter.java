package adapters;

import java.util.ArrayList;

import com.theteachermate.app.R;

import classes.Observation;
import classes.User;
import data.SQLiteHelper;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildArrayAdapter extends ArrayAdapter {
 	Context context; 
    int layoutResourceId;    
    public ArrayList<User> children;
    private SQLiteHelper sqliteHelper;
    
    public ChildArrayAdapter(Context context, int layoutResourceId, ArrayList<User> children) {
        super(context, layoutResourceId, children);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.children = children;
        sqliteHelper = SQLiteHelper.getInstance(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new UserHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.img_profile);
            holder.txtTitle = (TextView)row.findViewById(R.id.text_child_name);
            
            row.setTag(holder);
        }
        else
        {
            holder = (UserHolder)row.getTag();
        }
        
        User child = children.get(position);
        holder.txtTitle.setText(child.fullname());
        
        sqliteHelper.getProfilePicture(child.onlineUserID, holder.imgIcon);
                
        return row;
    }
    
    static class UserHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
