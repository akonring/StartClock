package app.timer;

import java.util.Calendar;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;

public class TimerApplication extends Application implements OnSharedPreferenceChangeListener  {

	int beepTime;
	private int [] beepTimeArray = {15,30,60,120,180,300};
	SharedPreferences prefs;
	private Chronometer global_chrono;
	private int formats;
	private boolean sound;
	private boolean realTime;

	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		realTime = true;
		global_chrono = new Chronometer(getBaseContext());
		initiateClock();
	}
		
	@SuppressWarnings("deprecation")
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		realTime = prefs.getBoolean("real_time", true);
		
		if(!realTime && ((key.equals("synchronize.hour")) || key.equals("synchronize.minute"))) {
			global_chrono.setBase(SystemClock.elapsedRealtime() - 
					(prefs.getInt("synchronize.hour", 0)*3600000 + prefs.getInt("synchronize.minute", 0)*60000));
		}
		else if(realTime) {
			initiateClock();
		}
	}
	
	public Chronometer getGlobalChronometer() {
		return global_chrono;
	}
	
	public void editListener() {
		beepTime = beepTimeArray[Integer.parseInt(prefs.getString("beep_interval", "2"))];
		formats = Integer.parseInt(prefs.getString("format", "2"));
		sound = !prefs.getBoolean("mute" , false);
		SpecificChronometerTickListener.releaseMedia();
		global_chrono.setOnChronometerTickListener(new SpecificChronometerTickListener(getBaseContext(), beepTime, sound, formats));
	}
	
	public boolean getRealTimeMode() {
		return realTime;
	}
		
	public void initiateClock() {
		Calendar rightNow = Calendar.getInstance();
		rightNow = Calendar.getInstance();
		global_chrono.setBase(SystemClock.elapsedRealtime() - 
				(rightNow.get(Calendar.HOUR_OF_DAY)*3600000 + rightNow.get(Calendar.MINUTE)*60000 + rightNow.get(Calendar.SECOND)*1000));		
		global_chrono.start();
	}
}
