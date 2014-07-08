package adapters;

import java.util.ArrayList;

import classes.File;
import classes.Folder;
import classes.User;

import com.theteachermate.app.R;

import data.SQLiteHelper;
import adapters.ChildArrayAdapter.UserHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FolderArrayAdapter extends ArrayAdapter {
	Context context; 
    int layoutResourceId;    
    public ArrayList<Folder> folders;
    public ArrayList<File> files;
    private SQLiteHelper sqliteHelper;
    
    public FolderArrayAdapter(Context context, int layoutResourceId, ArrayList<Folder> folders, ArrayList<File> files) {
        super(context, layoutResourceId, folders);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.folders = folders;
        this.files = files;
        sqliteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public int getCount(){
          return folders.size() + files.size();
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
        
        if(position >= folders.size()){
        	File file = files.get(position-folders.size());
        	holder.txtTitle.setText(file.name);
        	Drawable res = context.getResources().getDrawable(R.drawable.file_icon);
        	holder.imgIcon.setImageDrawable(res);
        	
        }else{
        	Folder folder = folders.get(position);
        	holder.txtTitle.setText(folder.name);
        	Drawable res = context.getResources().getDrawable(R.drawable.folder_icon);
        	holder.imgIcon.setImageDrawable(res);
        }
        //foldersqliteHelper.getProfilePicture(child.onlineUserID, holder.imgIcon);
                
        return row;
    }
    
    static class FolderHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
