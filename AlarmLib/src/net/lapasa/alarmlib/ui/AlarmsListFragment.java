package net.lapasa.alarmlib.ui;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import net.lapasa.alarmlib.R;
import net.lapasa.alarmlib.commands.SetAlarmCommand;
import net.lapasa.alarmlib.models.Alarm;
import net.lapasa.alarmlib.models.AlarmsModel;
import android.app.Activity;
import android.app.ListFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TimePicker;

/**
 * UI for Alarm list, also handles controls for triggering deletion of alarm
 * records
 * 
 * @author mlapasa
 * 
 */
public class AlarmsListFragment extends ListFragment implements ActionMode.Callback, Observer, OnTimeSetListener
{
	private static final String TAG = AlarmsListFragment.class.getName();
	private static final String PENDING_INTENT = "Pending Intent";
	private Object actionMode;
	private AlarmsModel model;
	private AlarmsListAdapter listAdapter;
	private ListView listView;
	private String duration;


	/**
	 * Factory method
	 */
	public static AlarmsListFragment create(PendingIntent pi)
	{
		AlarmsListFragment frag = new AlarmsListFragment();
		Bundle args = new Bundle();
		args.putParcelable(PENDING_INTENT, pi);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}


	/**
	 * [Constructor] Called when the fragment's activity has been created and
	 * this fragment's view hierarchy instantiated.
	 */
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		model = AlarmsModel.getInstance(getActivity());
		model.addObserver(this);
		listAdapter = new AlarmsListAdapter(getActivity(), model.getAlarms());
		setListAdapter(listAdapter);
		listView = getListView(); 
		listView.setOnItemLongClickListener(getOnItemLongClickListener());
		listView.setDividerHeight(15);
		
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) listView.getLayoutParams();
		layoutParams.setMargins(25, 25, 25, 25);
		listView.setLayoutParams(layoutParams);
		
		
	}

	private OnItemLongClickListener getOnItemLongClickListener()
	{
		return new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (actionMode != null)
				{
					return false;
				}

				actionMode = getActivity().startActionMode(AlarmsListFragment.this);
				view.setSelected(true);
				return true;
			}
		};
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item)
	{
		if (item.getItemId() == R.id.deleteAlarms)
		{

			SparseBooleanArray selected = listAdapter.getSelectedIds();
			for (int i = (selected.size() - 1); i >= 0; i--)
			{
				if (selected.valueAt(i))
				{
					Alarm selectedItem = (Alarm) listAdapter.getItem(selected.keyAt(i));
					model.delete(selectedItem);
				}
			}

			mode.finish();
			return true;

		}
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu)
	{
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode)
	{
		actionMode = null;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu)
	{
		return false;
	}

	/**
	 * Whenever the model changes, force the UI to be updated
	 * 
	 * @param observable
	 * @param data
	 */
	@Override
	public void update(Observable observable, Object data)
	{
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.action_addAlarm)
		{
			Calendar c = Calendar.getInstance();
			int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			TimePickerDialog d = new HackedTimePickerDialog(getActivity(), this, hourOfDay, minute, false);
			d.show();
		}

		return super.onOptionsItemSelected(item);

	}
	
	/**
	 * Triggered when a "NEW" alarm is added; Check view component for when the user updates an EXISTING alarm
	 * 
	 */
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		Alarm newAlarm = model.create(hourOfDay, minute);
		SetAlarmCommand cmd = new SetAlarmCommand(newAlarm, getActivity(), false);
		cmd.execute();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		Activity activity = getActivity();
		if (activity != null)
		{
			activity.getMenuInflater().inflate(R.menu.alarms, menu);
		}
		return true;
	}

}
