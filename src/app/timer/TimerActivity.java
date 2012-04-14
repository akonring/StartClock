package app.timer;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Chronometer;

public class TimerActivity extends Activity {

	Chronometer chrono;
	Calendar rightNow;
	boolean isStopped;
	TimerApplication timer;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		timer = ((TimerApplication)getApplication());
		
		timer.global_chrono = (Chronometer) findViewById(R.id.chrono);
		timer.initiateClock();		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public void onResume() {
		super.onResume();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if(timer.prefs.getBoolean("screen_dim", false)) {			
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		
		if(timer.prefs.getBoolean("indigo", false)) {
			lp.screenBrightness = 100 / 100.0f;
			getWindow().setAttributes(lp);
		}
		else {
			lp.screenBrightness = LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
			getWindow().setAttributes(lp);
		}
		timer.editListener();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    if(item.getItemId() == R.id.prefs) {
			startActivityForResult(new Intent(this, app.timer.PrefsActivity.class), 0);
	    }
	    else if(item.getItemId() == R.id.exit) {
	    	android.os.Process.killProcess(android.os.Process.myPid());
	    }
	    	return true;
	}	
}
	