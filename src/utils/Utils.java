package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class Utils {

	public static Date dateFromString(String dateString)
	{
		Date date = new Date();
		try {
			date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(dateString);
		} catch (ParseException e) {
			Log.i("pp", "WARNING: date couldn't be converted to string, using current date");
			e.printStackTrace();
		}

		return date;
	}
}
