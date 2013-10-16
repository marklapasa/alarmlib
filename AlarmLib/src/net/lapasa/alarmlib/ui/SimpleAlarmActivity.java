package net.lapasa.alarmlib.ui;

import net.lapasa.alarmlib.R;
import android.app.Activity;
import android.os.Bundle;

public class SimpleAlarmActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_alarm);
	}
}
