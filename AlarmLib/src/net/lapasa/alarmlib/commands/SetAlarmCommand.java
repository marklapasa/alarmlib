package net.lapasa.alarmlib.commands;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.text.format.DateUtils;
import android.widget.Toast;
import net.lapasa.alarmlib.models.Alarm;

public class SetAlarmCommand
{
	private Alarm alarm;
	private Context context;
	private boolean isSilent = false;

	public SetAlarmCommand(Alarm alarm, Context context, boolean isSilent)
	{
		this.alarm = alarm;
		this.context = context;
		this.isSilent  = isSilent;
	}
	
	public void execute()
	{		
		alarm.schedule(context);		
		String timeStr = 
				DateUtils.getRelativeTimeSpanString(
					alarm.getAlarmTime().getTimeInMillis(), 
					Calendar.getInstance().getTimeInMillis(), 
					DateUtils.MINUTE_IN_MILLIS).toString();
		
		if (!isSilent)
		{
			Toast.makeText(context, "Alarm set for " + timeStr, Toast.LENGTH_LONG).show();
		}
	}
}
