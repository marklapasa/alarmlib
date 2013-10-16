package net.lapasa.alarmlib.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Layer before database
 * 
 * @author mlapasa
 *
 */
public class AlarmsDTO
{
	private SQLiteDatabase database;
	private AlarmsSQLHelper dbHelper;

	public AlarmsDTO(Context context)
	{
		dbHelper = new AlarmsSQLHelper(context);
	}	
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}	
	
	public Alarm create(int hourOfDay, int minute)
	{
		open();
		
		ContentValues cv = new ContentValues();
		cv.put(AlarmsSQLHelper.HOUR_OF_DAY, hourOfDay);
		cv.put(AlarmsSQLHelper.MINUTE, minute);
		cv.put(AlarmsSQLHelper.REPEATS_DAY_OF_WEEK, "");
		cv.put(AlarmsSQLHelper.IS_ENABLED, 1);
		cv.put(AlarmsSQLHelper.LABEL, "Label");
		
		long id = database.insert(AlarmsSQLHelper.TABLE_ALARMS, null, cv);
		
		Cursor cursor = database.query(
				AlarmsSQLHelper.TABLE_ALARMS, 
				AlarmsSQLHelper.getAllColumns(), 
				AlarmsSQLHelper.ID + " = " + id, null, null, null, null);
		
		cursor.moveToFirst();
		
		Alarm alarm = cursorToAlarm(cursor);
		cursor.close();
		
		
		close();
		return alarm;
	}

	private Alarm cursorToAlarm(Cursor c)
	{
		Alarm a = new Alarm();
		
		a.id = c.getLong(0);
		a.hourOfDay = c.getInt(1);
		a.minute = c.getInt(2);
		a.repeatDayOfWeek = c.getString(3);
		int isEnabled = c.getInt(4);
		a.isEnabled = (isEnabled == 1) ? true : false;
		a.label = c.getString(5);
		
		return a;
	}
	
	
	/**
	 * Returns all alarms in the DB sorted by time
	 * @return
	 */
	public List<Alarm> getAll()
	{
		open();
		List<Alarm> alarms = new ArrayList<Alarm>();
		
		Cursor cursor = database.query(
				AlarmsSQLHelper.TABLE_ALARMS, 
				AlarmsSQLHelper.getAllColumns(), 
				null, null, null, null, AlarmsSQLHelper.HOUR_OF_DAY);
		
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			Alarm audioRec = cursorToAlarm(cursor);
			alarms.add(audioRec);
			cursor.moveToNext();
		}		
		cursor.close();
		
		Collections.sort(alarms);
		
		
		close();
		
		return alarms;
	}
	
	public void delete(Alarm a)
	{
		database.delete(
			AlarmsSQLHelper.TABLE_ALARMS, 
			AlarmsSQLHelper.ID + " = " + a.id, 
			null);
	}
	
	public Alarm update(Alarm a)
	{
		open();
		ContentValues cv = new ContentValues();
		cv.put(AlarmsSQLHelper.HOUR_OF_DAY, a.hourOfDay);
		cv.put(AlarmsSQLHelper.MINUTE, a.minute);
		cv.put(AlarmsSQLHelper.REPEATS_DAY_OF_WEEK, "");
		cv.put(AlarmsSQLHelper.IS_ENABLED, (a.isEnabled) ? 1 : 0);
		cv.put(AlarmsSQLHelper.LABEL, a.label);
		
		
		long updatedId = database.update(AlarmsSQLHelper.TABLE_ALARMS, cv, AlarmsSQLHelper.ID + " = " + a.id, null);
		
		Cursor cursor = database.query(
				AlarmsSQLHelper.TABLE_ALARMS, 
				AlarmsSQLHelper.getAllColumns(), 
				AlarmsSQLHelper.ID + " = " + updatedId, null, null, null, null);
		
		cursor.moveToFirst();
		
		Alarm alarm = cursorToAlarm(cursor);
		cursor.close();
		
		close();
		return alarm;
		
	}
}

