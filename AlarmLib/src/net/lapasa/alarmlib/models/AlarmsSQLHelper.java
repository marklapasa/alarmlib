package net.lapasa.alarmlib.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmsSQLHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "alarms.db";
	private static final int DATABASE_VERSION = 5;
	public static final String TABLE_ALARMS = "alarms";
	
	static String ID = "_id";	
	static String HOUR_OF_DAY = "hourOfDay";
	static String MINUTE = "minute";
	static final String REPEATS_DAY_OF_WEEK = "repeatsDayOfWeek";
	static String IS_ENABLED = "isEnabled";
	static String LABEL = "label";
	
	public static String[] getAllColumns()
	{
		return new String[]
		{
			ID,
			HOUR_OF_DAY,
			MINUTE,
			REPEATS_DAY_OF_WEEK,
			IS_ENABLED,
			LABEL
		};
	}
	
	
	private static final String DATABASE_CREATE = 
			"create table " + TABLE_ALARMS 
				+ "(" 
				+ ID + " integer primary key autoincrement, "
				+ HOUR_OF_DAY + " integer, " 
				+ MINUTE + " integer, "
				+ REPEATS_DAY_OF_WEEK + " text,"
				+ IS_ENABLED + " integer,"
				+ LABEL + " text"
				+");";	

	public AlarmsSQLHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(AlarmsSQLHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
		onCreate(db);
	}


}
