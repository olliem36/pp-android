package views;

import com.theteachermate.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UserCheckable extends RelativeLayout implements Checkable {

	private boolean checked;
	
	public UserCheckable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	@Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked; 

        ImageView iv = (ImageView) findViewById(R.id.img_checkbox);
        iv.setImageResource(checked ? R.drawable.checked_48x48 : R.drawable.unchecked_48x48);
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
    }

}
