package net.lapasa.alarmlib.ui;

import net.lapasa.alarmlib.R;
import net.lapasa.alarmlib.models.AlarmsModel;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Display a list of Alarms
 * 
 * @author admin
 *
 */
public class AlarmsFragmentActivity extends Activity
{
	private AlarmsListFragment alarmsFrag;
	private AlarmsModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Load the AlarmsListFragment via xml
		setContentView(R.layout.alarms);
		alarmsFrag = (AlarmsListFragment) getFragmentManager().findFragmentById(R.id.alarmsListFragment);
		model = AlarmsModel.getInstance(this);
	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return alarmsFrag.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return alarmsFrag.onOptionsItemSelected(item);
	}
}