package net.lapasa.alarmlib.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.lapasa.alarmlib.broadcastreceiver.SimpleBroadcastReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import za.co.neilson.alarm.alert.AlarmAlertBroadcastReciever;

public class Alarm implements Comparable<Alarm>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Class ALARM_BROADCAST_RECEIVER = SimpleBroadcastReceiver.class;
	long id;
	public int hourOfDay;
	public int minute;
	public String repeatDayOfWeek;
	public boolean isEnabled;
	public String label;

	// Repeat flags
	public boolean SUN;
	public boolean MON;
	public boolean TUE;
	public boolean WED;
	public boolean THU;
	public boolean FRI;
	public boolean SAT;

	private PendingIntent pendingIntent;


	/**
	 * Used for sorting
	 * 
	 * @return
	 */
	public int getInMinutes()
	{
		return (hourOfDay * 60) + minute;
	}

	/**
	 * a negative integer if this instance is less than another; a positive
	 * integer if this instance is greater than another; 0 if this instance has
	 * the same order as another.
	 * 
	 * @param another
	 * @return
	 */
	@Override
	public int compareTo(Alarm another)
	{
		if (this.getInMinutes() < another.getInMinutes())
		{
			return -1;
		}
		else if (this.getInMinutes() > another.getInMinutes())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public String toString()
	{
		String s = hourOfDay + ":" + minute;
		SimpleDateFormat sdf24Hrs = new SimpleDateFormat("HH:mm");
		Date parsedDate = null;
		try
		{
			parsedDate = sdf24Hrs.parse(s);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		SimpleDateFormat sdf12hrs = new SimpleDateFormat("h:mmaa");

		return sdf12hrs.format(parsedDate);
	}

	public void setIsEnabled(String str)
	{
		isEnabled = str.equals("true");
	}

	public void decodeRepeat(String repeatStr)
	{
		String[] daysAsCodeStr = repeatStr.split("");

		resetRepeatFlags();

		for (int i = 0; i < daysAsCodeStr.length; i++)
		{
			int dayCode = Integer.valueOf(daysAsCodeStr[i]);
			if (dayCode == 1)
			{
				SUN = true;
			}
			else if (dayCode == 2)
			{
				MON = true;
			}
			else if (dayCode == 3)
			{
				TUE = true;
			}
			else if (dayCode == 4)
			{
				WED = true;
			}
			else if (dayCode == 5)
			{
				THU = true;
			}
			else if (dayCode == 6)
			{
				FRI = true;
			}
			else if (dayCode == 7)
			{
				SAT = true;
			}
		}
	}

	public String encodeRepat()
	{
		String repeatStr = "";
		if (SUN)
		{
			repeatStr += "1";
		}
		else if (MON)
		{
			repeatStr += "2";
		}
		else if (TUE)
		{
			repeatStr += "3";
		}
		else if (WED)
		{
			repeatStr += "4";
		}
		else if (THU)
		{
			repeatStr += "5";
		}
		else if (FRI)
		{
			repeatStr += "6";
		}
		else if (SAT)
		{
			repeatStr += "7";
		}
		return repeatStr;
	}

	private void resetRepeatFlags()
	{
		SUN = MON = TUE = WED = THU = FRI = SAT = false;
	}


	public void schedule(Context context)
	{
		isEnabled = true;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime().getTimeInMillis(), getPendingIntent(context));
	}

	private PendingIntent getPendingIntent(Context context)
	{
		if (pendingIntent == null)
		{
			Intent i = new Intent(context, ALARM_BROADCAST_RECEIVER);
			i.putExtra("alarm", this);
			pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		}
		return pendingIntent; 
	}

	public Calendar getAlarmTime()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		
		Calendar now = Calendar.getInstance();
		if (c.before(now))
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		// What day of the week will this alarm trigger? Is it Monday, Tuesday, etc
		int alarmDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		// Get the list of days this alarm will
		List<Integer> daysToRepeat = getDaysToRepeat();
	
		if (daysToRepeat.size() > 0)
		{
			while(!daysToRepeat.contains(alarmDayOfWeek))
			{
				c.add(Calendar.DAY_OF_MONTH,1);
			}
		}
		
		
		return c;
	}

	/**
	 * Look at the flag on this alarm; if they are set to true, add them to the list of
	 * days to repeat
	 * 
	 * @return
	 */
	private List<Integer> getDaysToRepeat()
	{
		ArrayList<Integer> daysToRepeat = new ArrayList<Integer>();
		if (SUN) daysToRepeat.add(1);
		if (MON) daysToRepeat.add(2);
		if (TUE) daysToRepeat.add(3);
		if (WED) daysToRepeat.add(4);
		if (THU) daysToRepeat.add(5);
		if (FRI) daysToRepeat.add(6);
		if (SAT) daysToRepeat.add(7);
		return daysToRepeat;
	}
	
	
	public boolean isRepeat()
	{
		boolean isRepeat = SUN || MON || TUE || WED || THU || FRI || SAT;
		return isRepeat;
	}

	public void deschedule(Context context)
	{
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(getPendingIntent(context));		
	}
}
