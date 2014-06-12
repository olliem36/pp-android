package dialogs;

import interfaces.DatePickerInterface;

import java.util.Calendar;
import java.util.Date;

import com.theteachermate.app.observation.ObservationActivity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class ppDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

@Override
public Dialog onCreateDialog(Bundle savedInstanceSateate) {

    final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);

        Date selectedDate = calendar.getTime();

    	((DatePickerInterface)getActivity()).dateSelected(selectedDate, getTag());
	}
}
