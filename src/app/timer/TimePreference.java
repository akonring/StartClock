package app.timer;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ToggleButton;

/**
 * Custom preference for time selection. Hour and minute are persistent and
 * stored separately as ints in the underlying shared preferences under keys
 * KEY.hour and KEY.minute, where KEY is the preference's key.
 */
public class TimePreference extends DialogPreference {

  /** The widget for picking a time */
  private TimePicker timePicker;

  private ToggleButton real_time_button;
  /** Default hour */
  private int hour;

  /** Default minute */
  private int minute;


  /**
   * Creates a preference for choosing a time based on its XML declaration.
   *
   * @param context
   * @param attributes
   */
  public TimePreference(Context context,
                        AttributeSet attributes) {
    super(context, attributes);
    setPersistent(false);
    Calendar rightNow = Calendar.getInstance();
	rightNow = Calendar.getInstance();
	hour = rightNow.get(Calendar.HOUR_OF_DAY);
	minute = rightNow.get(Calendar.MINUTE);
  }

  /**
   * Initialize time picker to currently stored time preferences.
   *
   * @param view
   * The dialog preference's host view
   */
  @Override
  public void onBindDialogView(View view) {
    super.onBindDialogView(view);
    timePicker = (TimePicker) view.findViewById(R.id.prefTimePicker);
    real_time_button = (ToggleButton) view.findViewById(R.id.real_time_button);
    timePicker.setIs24HourView(DateFormat.is24HourFormat(timePicker.getContext()));
    timePicker.setCurrentHour(hour);
    timePicker.setCurrentMinute(minute);
    
    timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			if(real_time_button.isChecked()) {
				real_time_button.toggle();
			}	
		} 	  
    });
  } 

  /**
   * Handles closing of dialog. If user intended to save the settings, selected
   * hour and minute are stored in the preferences with keys KEY.hour and
   * KEY.minute, where KEY is the preference's KEY.
   *
   * @param okToSave
   * True if user wanted to save settings, false otherwise
   */
  @Override
  protected void onDialogClosed(boolean okToSave) {
    super.onDialogClosed(okToSave);
    boolean isToggleButtonChecked;
    if (okToSave) {
      timePicker.clearFocus();
      SharedPreferences.Editor editor = getEditor();
      editor.putInt(getKey() + ".hour", timePicker.getCurrentHour());
      editor.putInt(getKey() + ".minute", timePicker.getCurrentMinute());
      
      isToggleButtonChecked = real_time_button.isChecked();
      editor.putBoolean("real_time", isToggleButtonChecked);
      editor.commit();
    }
  }
}