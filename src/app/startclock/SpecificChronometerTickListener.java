package app.startclock;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;

public class SpecificChronometerTickListener implements Chronometer.OnChronometerTickListener {

	int beepInterval;
	boolean sound;
	int formats;
	Context context;

	public SpecificChronometerTickListener(Context context, int beepInterval, boolean sound, int formats) {
		this.beepInterval = beepInterval;
		this.sound = sound;
		this.formats = formats;
		this.context = context;
	}
	
	public String padWithZeroes(CharSequence seq) {
		switch(seq.length()) {
		case 2 : seq = "00:00:" + seq;
		break;
		case 4 : seq = "00:0" + seq;
        break;
		case 5 : seq = "00:" + seq; 
		break;
		case 6 : seq = "00" + seq;
		break;
		case 7: seq = "0" + seq;
        }
		return seq.toString();
	}

	@Override
	public void onChronometerTick(Chronometer chronometer) {
		
		String text = padWithZeroes(chronometer.getText());
		
		int hours = Integer.valueOf(text.substring(0, 2));
		
		text = String.valueOf(hours % 24) + text.substring(2, text.length());  
		
		text = padWithZeroes(text);
        
		switch(formats) {		       
		case 1 : chronometer.setText(text.subSequence(text.length()-2, text.length()));
		chronometer.setTextSize(120);
		break;
		case 2 : chronometer.setText(text.subSequence(text.length()-5, text.length()-3));
		chronometer.setTextSize(120);
		break;
		case 3 : chronometer.setText(text.subSequence(0, text.length()-6));
		chronometer.setTextSize(120);
		break;
		default : 
		chronometer.setText(text);
		chronometer.setTextSize(80);
		break;
		}

		if(sound && (((SystemClock.elapsedRealtime() - chronometer.getBase())/1000) + 5) % beepInterval == 0) {
			TimerApplication.soundPlayer.start();
		}      
	}
}
