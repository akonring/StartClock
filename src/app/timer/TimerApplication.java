package app.timer;

import java.util.Calendar;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;

public class TimerApplication extends Application implements OnSharedPreferenceChangeListener  {

	int beepTime;
	OnChronometerTickListener chronoListener;
	int [] beepTimeArray = {15,30,60,120,180,300};
	static MediaPlayer soundPlayer;
	SharedPreferences prefs;
	Chronometer global_chrono;
	private int formats;
	private boolean sound;

	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		global_chrono = new Chronometer(getBaseContext());
			
	}
		
	@SuppressWarnings("deprecation")
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		editListener();
	}
	
	public void editListener() {
		beepTime = beepTimeArray[Integer.parseInt(prefs.getString("beep_interval", "2"))];
		formats = Integer.parseInt(prefs.getString("format", "2"));
		sound = !prefs.getBoolean("sound" , false);
		SpecificChronometerTickListener.releaseMedia();
		global_chrono.setOnChronometerTickListener(new SpecificChronometerTickListener(getBaseContext(), beepTime, sound, formats));
	}
		
	public void initiateClock() {
		Calendar rightNow = Calendar.getInstance();
		rightNow = Calendar.getInstance();
		global_chrono.setBase(SystemClock.elapsedRealtime() - 
				(rightNow.get(Calendar.HOUR_OF_DAY)*3600000 + rightNow.get(Calendar.MINUTE)*60000 + rightNow.get(Calendar.SECOND)*1000));
		
		global_chrono.setOnChronometerTickListener(new SpecificChronometerTickListener(getBaseContext(), beepTime, sound, formats));
		global_chrono.start();
	}
}
