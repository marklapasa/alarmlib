package net.lapasa.alarmlib.ui;

import android.app.TimePickerDialog;
import android.content.Context;

public class HackedTimePickerDialog extends TimePickerDialog
{
	public HackedTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourTime)
	{
		super(context, listener, hourOfDay, minute, is24HourTime);
		this.setTitle("Enter alarm time:");
	}
	

	@Override
	protected void onStop()
	{
		// Do nothing because TimePicker gets called twice, once onClick() and onStop()
		// http://stackoverflow.com/questions/13748988/android-4-1-2-dialogs-are-called-twice/18083173
	}
	

}
