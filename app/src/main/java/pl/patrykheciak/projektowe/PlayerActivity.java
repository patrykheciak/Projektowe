package pl.patrykheciak.projektowe;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.VideoView;

public class PlayerActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private IntroVideoView videoView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private ImageButton bRewind;
    private ImageButton bForward;
    private ImageButton bPlayPause;
    private SeekBar seekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        videoView = findViewById(R.id.video_view);


        // Set up the user interaction to manually show or hide the system UI.
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Touch", "" + event);
                if (event.getActionIndex() == MotionEvent.ACTION_DOWN) {
                    toggle();
                    return true;
                }
                return false;
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        bRewind = findViewById(R.id.button_fast_rewind);
        bForward = findViewById(R.id.button_fast_forward);
        bPlayPause = findViewById(R.id.button_play_pause);
        seekbar = findViewById(R.id.seekbar);


        bRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRewind();
            }
        });
        bPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    bPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24px);
                } else {
//                    videoView.start();
                    doStart();
                    bPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24px);
                }
            }
        });
        bForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFastForward();
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) videoView.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void doStart() {
        // The duration in milliseconds
        int duration = videoView.getDuration();

        int currentPosition = videoView.getCurrentPosition();
        if (currentPosition == 0) {
            seekbar.setMax(duration);
        } else if (currentPosition == duration) {
            // Resets the MediaPlayer to its uninitialized state.
            videoView.stopPlayback();
        }
        videoView.start();
        // Create a thread to update position of SeekBar.
        UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
        mHideHandler.postDelayed(updateSeekBarThread, 50);
    }

    class UpdateSeekBarThread implements Runnable {

        public void run() {
            int currentPosition = videoView.getCurrentPosition();

            seekbar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            mHideHandler.postDelayed(this, 50);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(1000);
//        Uri videoUri =
//                Uri.parse("https://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_5mb.mp4");
//        videoView.setVideoURI(videoUri);
        videoView.setVideoPath("https://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_5mb.mp4");
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("PlayerActivity", "onPrepared");
//                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                doStart();
            }
        });

//        doStart();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        videoView.pause();

        bPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24px);
    }

    public void doRewind() {
        int currentPosition = videoView.getCurrentPosition();
        int SUBTRACT_TIME = 5000;
        int target = currentPosition - SUBTRACT_TIME;
        if (target < 0) {
            target = 0;
        }
        videoView.seekTo(target);

    }

    public void doFastForward() {
        int currentPosition = videoView.getCurrentPosition();
        int duration = videoView.getDuration();
        int TIME = 5000;
        int target = currentPosition + TIME;
        if (target >= duration) {
            target = duration - 1;
        }
        videoView.seekTo(target);

    }
}
