package kr.pe.sgyou85.sgplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class SGPlayerActivity extends Activity {
	
	private MediaPlayer mpPlayer = new MediaPlayer();
	
	private TimerTicker timer = null;
	private LinearLayout llContainer = null;
	private SeekBar sbProgress = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.llContainer = (LinearLayout)this.findViewById(R.id.m_llContainer);
        this.sbProgress = (SeekBar)this.findViewById(R.id.m_sbProgress);
        
        this.timer = new TimerTicker();
        
        this.linkEvent();
    }
    
    private void linkEvent() {
    	Button btnPlay = (Button)this.findViewById(R.id.m_btnPlay);
    	btnPlay.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				play();
			}
		});
    	
    	Button btnStop = (Button)this.findViewById(R.id.m_btnStop);
    	btnStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stop();
			}
    	});
    	
    	this.mpPlayer.setOnInfoListener(new OnInfoListener() {
			
			public boolean onInfo(MediaPlayer mp, int what, int extra) {

				Log.i("SGPA", "mp --> " + what);

				return false;
			}
		});
    	
    	this.mpPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			
			public void onSeekComplete(MediaPlayer mp) {
				
				Log.i("SGPA", "mp --> onSeekComplete");
			}
		});
    	
    	this.mpPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			public void onBufferingUpdate(MediaPlayer mp, int percent) {

				Log.i("SGPA", "mp --> onBufferingUpdate");
			}
		});
    	
    	this.mpPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				
				Log.i("SGPA", "mp --> onCompletion");
			}
		});
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("SGPA", "----> Destory");
	}
	
	private void play() {
		try{
			
			String mp3FileName = getFilesDir().getAbsolutePath();
			mp3FileName += "/002.mp3";
			
			mpPlayer.setDataSource(mp3FileName);
        	mpPlayer.prepare();
        	mpPlayer.setLooping(false);
        	mpPlayer.start();
        	
        	this.sbProgress.setMax(this.mpPlayer.getDuration());
        	Log.i("SGPA", "max --> " + this.mpPlayer.getDuration());
        	
        	timer.start();
		} catch (Exception e) {
			Log.e("MP3", e.getMessage());
		}
	}
	
	private void stop() {
		try{
        	mpPlayer.stop();
        	mpPlayer.release();
        	
        	timer.stop();
		} catch(Exception e){
			Log.e("MP3", e.getMessage());
		}
	}

    private void updateProgress(){
    	try {
			//int max = this.mpPlayer.getDuration();
			int current = this.mpPlayer.getCurrentPosition();
			int progress = current; // * 100 / max;
			
			//Log.i("SGPA", "current --> " + current);
			
			this.sbProgress.setProgress(progress);
		} catch (Exception e) {
			Log.e("SGPA", e.getMessage());
		}
    	
    }
    
    private void postDelayed(int delayMillis) {
    	this.llContainer.postDelayed(this.timer, delayMillis);
    }

    private final class TimerTicker implements Runnable {
    	
    	private boolean isStop = false;
    	private int interval = 1000;

		public void stop() {
			this.isStop = true;
		}
		
		public void start() {
			this.isStop = false;
			this.run();
		}

		public void run() {
			try{
				if (this.isStop) return;
				if (mpPlayer.isPlaying() == false) return;
				
				updateProgress();
				postDelayed(interval);
			} catch (Exception e) {
				Log.e("SGPA", e.getMessage());
			}
    	}
    }
    
}