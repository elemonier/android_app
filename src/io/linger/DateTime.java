package io.linger;

import java.util.Calendar;

import android.util.Log;

/**
 * Handles date and time values, as well as parsing datetime values from
 * the database. Also allows translating one form of date or time to
 * the other.
 * @author Emily Pakulski
 */

public class DateTime
{
	
	public DateTime() {}

	public static String getCurrentDateTime()
	{
		final Calendar calendar = Calendar.getInstance();
		
		// create date string
		String stringDate;
		int todaysYear = calendar.get(Calendar.YEAR);
		int todaysMonth = calendar.get(Calendar.MONTH);
		int todaysDay = calendar.get(Calendar.DAY_OF_MONTH);
		stringDate = parseMonth(todaysMonth) + " " + todaysDay + ", " + todaysYear;
		
		// create time string
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String stringTime = parseTime(hourOfDay + ":" + minute);
		
		Log.v("Testing", stringDate);
		Log.v("Testing", stringTime);
		
		return stringDate + " " + stringTime;
		
	}
	
	public static String parseMonth(int originalMonth)
	{
		// turn month into full word (e.g. "03" to "March")
		if (originalMonth == 0)
			return "January";
		else if (originalMonth == 1)
			return "February";
		else if (originalMonth == 2)
			return "March";
		else if (originalMonth == 3)
			return "April";
		else if (originalMonth == 4)
			return "May";
		else if (originalMonth == 5)
			return "June";
		else if (originalMonth == 6)
			return "July";
		else if (originalMonth == 7)
			return "August";
		else if (originalMonth == 8)
			return "September";
		else if (originalMonth == 9)
			return "October";
		else if (originalMonth == 10)
			return "November";
		else if (originalMonth == 11)
			return "December";
		return "February";
		
	}
	
	/** Parse time in form HH:MM to form HH:MM AM/PM */
	public static String parseTime(String timeString)
	{
		String[] timeArray = timeString.split("(?!\")\\p{Punct}");
		int militaryHour = Integer.parseInt(timeArray[0]);
		int oneDigitMinutes = Integer.parseInt(timeArray[1]);
		
		return parseTime(militaryHour, oneDigitMinutes);
	}
	
	public static String parseTime(int militaryHour, int oneDigitMinutes)
	{
		// set hour to non-military time
		String stringHour = Integer.toString(militaryHour);
		String AMorPM = "AM";
		if (militaryHour > 12)
			stringHour = Integer.toString(militaryHour - 12);
		if (militaryHour >= 12)
			AMorPM = "PM";
		// make sure minutes are always 2 digits (eg. not 12:1 PM, but 12:01 PM)
		String stringMinute  = Integer.toString(oneDigitMinutes);
		if (oneDigitMinutes < 10)
			stringMinute = 0 + stringMinute; // string addition
		return (stringHour + ":" + stringMinute + " " + AMorPM);
	}	
}