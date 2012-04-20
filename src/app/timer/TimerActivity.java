package app.timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

/** 
 * This class is controlling the main view of the application.
 * The responsibilities of the class are mainly screen-related
 * A chronometer reference is given to display the chronometer from
 * TimerApplication
 * @author ako
 *
 */
public class TimerActivity extends Activity {

	//Getting a reference to the TimerApplication
	TimerApplication timer;
	
	/**
	 * This method gets called when starting the application (see manifest)
	 * The use of getGlobalChronometer makes a loose coupling to the app.
	 * Surprisingly also called when rotating the screen which is the reason
	 * for the ugly null check.
	 * GlobalChronometer cannot be a child to two parents and that is why we have
	 * to remove the child if it there.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		timer = ((TimerApplication)getApplication());
		RelativeLayout layout = new RelativeLayout(this);
		
		if(timer.getGlobalChronometer().getParent() != null) {
			((ViewGroup) timer.getGlobalChronometer().getParent()).removeView(timer.getGlobalChronometer());
		}
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		timer.getGlobalChronometer().setLayoutParams(layoutParams);
		layout.addView(timer.getGlobalChronometer());
		setContentView(layout);
	}
	
	/**
	 * Used to inflate the menu using the menu.xml
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	/**
	 * All the screen related preferences are dealt with.
	 * calls editListener to change Chronometer preferences
	 */
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
	
	/**
	 * Call the preference menu or exit. Exit is a rough
	 * execution - may have to ease up a little.. 
	 */
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
	