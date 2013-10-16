package net.lapasa.alarmlib.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.app.PendingIntent;
import android.content.Context;

public class AlarmsModel extends Observable
{
	private static AlarmsModel _instance;
	private AlarmsDTO dto;
	private List<Alarm> alarms;
	
	/**
	 * Private constructor
	 * @param context 
	 */
	private AlarmsModel(Context context)
	{
		this.dto = new AlarmsDTO(context);
		this.alarms = new ArrayList<Alarm>();
		alarms.addAll(this.dto.getAll());
	}
	
	/**
	 * Singleton getter
	 */
	public static AlarmsModel getInstance(Context context)
	{
		if (_instance == null)
		{
			_instance = new AlarmsModel(context);
		}
		return _instance;
	}
	
	
	
	/**
	 * Create a basic alarm
	 * 
	 * @param hourOfDay 24 hours
	 * @param minute 0-60
	 * @param duration 
	 */
	public Alarm create(int hourOfDay, int minute)
	{
		Alarm newAlarm = dto.create(hourOfDay, minute);
		alarms.add(newAlarm);
		setChanged();
		notifyObservers();
		return newAlarm;
	}

	public List<Alarm> getAlarms()
	{
		return alarms;
	}
	
	public Alarm update(Alarm a)
	{
		Alarm updatedAlarm = dto.update(a);
		setChanged();
		notifyObservers(a);
		return updatedAlarm;
	}
	
	public void delete(Alarm a)
	{
		dto.delete(a);
		setChanged();
		notifyObservers();		
	}
}
