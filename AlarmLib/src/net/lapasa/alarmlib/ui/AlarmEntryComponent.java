package net.lapasa.alarmlib.ui;

import java.util.Calendar;

import net.lapasa.alarmlib.R;
import net.lapasa.alarmlib.commands.SetAlarmCommand;
import net.lapasa.alarmlib.models.Alarm;
import net.lapasa.alarmlib.models.AlarmsModel;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmEntryComponent extends RelativeLayout implements OnClickListener, OnCheckedChangeListener, OnTimeSetListener
{
	private Alarm alarm;
	private ViewGroup view;
	private AlarmsModel model;
	private TextView timeLabel;
	private CompoundButton toggleButton;
	private View footer;
	private View expandIcon;
	private View collapseIcon;
	private boolean isDetailsOpen;
	private View detailsView;
	private View occuranceLabel;
	private TextView alarmLabel;
	private View daysOfWeek;

	public AlarmEntryComponent(Context context, Alarm alarm)
	{
		super(context);
		this.alarm = alarm;
		this.model = AlarmsModel.getInstance(context);

	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		view = (ViewGroup) view.inflate(getContext(), R.layout.alarm_entry, null);
		addView(view);

		init(alarm);
	}

	public void init(Alarm alarm)
	{
		this.alarm = alarm;

		initHeader(view);
		initDetailsView(view);
		initFooter(view);
	}

	private void initHeader(View v)
	{
		// Label
		timeLabel = (TextView) v.findViewById(R.id.time);
		timeLabel.setOnClickListener(this);
		timeLabel.setText(alarm.toString());

		// Toggle Button
		toggleButton = (CompoundButton) v.findViewById(R.id.enableToggle);
		toggleButton.setChecked(alarm.isEnabled);
		toggleButton.setOnCheckedChangeListener(this);
	}

	private void initDetailsView(View v)
	{
		detailsView = v.findViewById(R.id.details);
		alarmLabel = (TextView) v.findViewById(R.id.label);
		alarmLabel.setText(alarm.label);
		alarmLabel.setOnClickListener(this);
		
		daysOfWeek = v.findViewById(R.id.daysOfWeek);
		
		setToggle(v.findViewById(R.id.repeatCheckBox), alarm.isRepeat());
		
		setToggle(v.findViewById(R.id.sunToggle), alarm.SUN);
		setToggle(v.findViewById(R.id.monToggle), alarm.MON);
		setToggle(v.findViewById(R.id.tueToggle), alarm.TUE);
		setToggle(v.findViewById(R.id.wedToggle), alarm.WED);
		setToggle(v.findViewById(R.id.thuToggle), alarm.THU);
		setToggle(v.findViewById(R.id.friToggle), alarm.FRI);
		setToggle(v.findViewById(R.id.satToggle), alarm.SAT);
		
	}

	private void setToggle(View v, boolean flag)
	{
		CompoundButton toggle = (CompoundButton) v;
		toggle.setChecked(flag);
		toggle.setOnCheckedChangeListener(this);
		
	}

	private void initFooter(View v)
	{
		footer = v.findViewById(R.id.footer);
		footer.setOnClickListener(this);
		expandIcon = v.findViewById(R.id.expandIcon);
		collapseIcon = v.findViewById(R.id.collapseIcon);
		occuranceLabel = v.findViewById(R.id.occuranceLabel);
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();

		if (id == R.id.time)
		{
			updateTime();
		}
		else if (id == R.id.footer)
		{
			if (isDetailsOpen)
			{
				collapseIcon.setVisibility(View.GONE);
				expandIcon.setVisibility(View.VISIBLE);
				detailsView.setVisibility(View.GONE);
				isDetailsOpen = false;
			}
			else
			{
				// Expand
				collapseIcon.setVisibility(View.VISIBLE);
				expandIcon.setVisibility(View.GONE);
				detailsView.setVisibility(View.VISIBLE);
				isDetailsOpen = true;
			}
		}
		else if(id == R.id.label)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			final EditText editTextView = (EditText) inflate(getContext(), R.layout.label_edit, null);
			builder.setView(editTextView);
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// Do nothing
				}
			});
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// Take 
					alarm.label = editTextView.getText().toString();
					model.update(alarm);
				}
			});
			builder.show();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		int id = buttonView.getId();
		if (id == R.id.enableToggle)
		{
			alarm.isEnabled = isChecked;
			if (!alarm.isEnabled)
			{
				// Disable this alarm
				alarm.deschedule(getContext());
			}
			else
			{
				alarm.schedule(getContext());
			}
				
		}
		else if (id == R.id.sunToggle)
		{
			alarm.SUN = isChecked;
		}
		else if (id == R.id.monToggle)
		{
			alarm.MON = isChecked;
		}
		else if (id == R.id.tueToggle)
		{
			alarm.TUE = isChecked;
		}
		else if (id == R.id.wedToggle)
		{
			alarm.WED = isChecked;
		}		
		else if (id == R.id.thuToggle)
		{
			alarm.THU = isChecked;	
		}		
		else if (id == R.id.friToggle)
		{
			alarm.FRI = isChecked;
		}		
		else if (id == R.id.satToggle)
		{
			alarm.SAT = isChecked;
		}		
		else if (id == R.id.repeatCheckBox)
		{
			if (isChecked)
			{
				alarm.SUN = alarm.MON = alarm.TUE = alarm.WED = alarm.THU = alarm.FRI = alarm.SAT = true;
				enableDaysOfWeek(true);
			}
			else
			{
				alarm.SUN = alarm.MON = alarm.TUE = alarm.WED = alarm.THU = alarm.FRI = alarm.SAT = false;
				enableDaysOfWeek(false);
			}
		}
		model.update(alarm);
	}

	public void updateTime()
	{
		Calendar c = Calendar.getInstance();
		int hourOfDay = alarm.hourOfDay;
		int minute = alarm.minute;
		TimePickerDialog d = new HackedTimePickerDialog(getContext(), this, hourOfDay, minute, false);
		d.show();
	}

	/**
	 * This gets fired when user updates an EXISTING alarm
	 */
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		alarm.hourOfDay = hourOfDay;
		alarm.minute = minute;
		model.update(alarm);
		
		SetAlarmCommand cmd = new SetAlarmCommand(alarm, getContext(), false);
		cmd.execute();
	}
	
	
	private void enableDaysOfWeek(boolean flag)
	{
		if (flag)
		{
			daysOfWeek.setVisibility(View.VISIBLE);
		}
		else
		{
			daysOfWeek.setVisibility(View.GONE);
		}
	}

}
