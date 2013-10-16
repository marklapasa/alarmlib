package net.lapasa.alarmlib.ui;

import java.util.List;

import net.lapasa.alarmlib.models.Alarm;
import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AlarmsListAdapter extends BaseAdapter
{

	private List<Alarm> alarms;
	private List<Alarm> mSelectedItemsIds;
	private SparseBooleanArray selectedItemsIds;
	private LayoutInflater inflator;
	private Context context;

	public AlarmsListAdapter(Context context, List<Alarm> alarms)
	{
		super();
		this.context = context;
		this.alarms = alarms;
		this.selectedItemsIds = new SparseBooleanArray();
		this.inflator = ((Activity) context).getLayoutInflater();
	}


	@Override
	public int getCount()
	{
		return alarms.size();
	}

	@Override
	public Object getItem(int position)
	{
		return alarms.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Alarm a = alarms.get(position);
		AlarmEntryComponent v = null;
		if (convertView != null)
		{
			v = (AlarmEntryComponent) convertView;
			v.init(a);
		}
		else
		{
			v = new AlarmEntryComponent(context, a);
		}
		
		return v;
	}



	public void toggleSelection(int position)
	{
		selectView(position, !selectedItemsIds.get(position));
	}

	public void removeSelection()
	{
		selectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value)
	{
		if (value)
		{
			selectedItemsIds.put(position, value);
		}
		else
		{
			selectedItemsIds.delete(position);
		}

		notifyDataSetChanged();
	}

	public int getSelectedCount()
	{
		return selectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds()
	{
		return selectedItemsIds;
	}

}
