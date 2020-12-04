package video;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ak.demo.R;
import video.ui.CtVideo;
import video.ui.CtVideoPlayer;

/**
 * Created by ak on 3/23/16.
 */
public class VideoActivity extends Activity {


    public static final String VIDEO_KEY = "video";
    CtVideoPlayer videoPlayer;
    LinearLayout mCompleteActions;
    private CtVideo ctVideo;
    private VideoState mVideoState = VideoState.Init;
    private boolean pauseByLeave = false;
    private CtVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new CtVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            videoPlayer.close();
            finish();
        }

        @Override
        public void onSwitchPageType() {

        }

        @Override
        public void onPlayFinish() {
            updateStatus(PlayStatus.COMPLETE);
        }
    };
    private BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                if (intent.getIntExtra("state", 1) == 0) {
                    handleHeadsetDisconnected();
                }
            } else if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
                handleHeadsetDisconnected();
            }
        }

    };

    public static final void startPlay(Context context, String videoName, String url, String dispatchUrl) {
        Intent intent = new Intent(context, VideoActivity.class);
        CtVideo video = new CtVideo();
        video.setmVideoName(videoName);
        video.setmVideoUrl(url);
        video.setDispatchUrl(dispatchUrl);
        intent.putExtra(VIDEO_KEY, video);
//        Jumper.launchActivity(context, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
         videoPlayer = findViewById(R.id.video_player);
         mCompleteActions = findViewById(R.id.complete_actions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getIntent().hasExtra(VIDEO_KEY)) {
            ctVideo = (CtVideo) getIntent().getSerializableExtra(VIDEO_KEY);
        } else {
            finish();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        videoPlayer.setVideoPlayCallback(mVideoPlayCallback);
        initData();
        registerHeadsetPlugReceiver();
    }

    private void registerHeadsetPlugReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        registerReceiver(headsetPlugReceiver, intentFilter);
    }

    private void handleHeadsetDisconnected() {
        if (videoPlayer.isPlaying()) {
            videoPlayer.pausePlay(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.setPlayerToBeStop(false);
        if (mVideoState == VideoState.Pause || mVideoState == VideoState.Playing) {
            videoPlayer.goOnPlayLastPosition(mVideoState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.setPlayerToBeStop(true);
        videoPlayer.leaveVideo();
        if (videoPlayer.isPlaying()) {
            mVideoState = VideoState.Playing;
            videoPlayer.pausePlay(true);
        } else {
            mVideoState = VideoState.Pause;
        }
    }

    private void initData() {
        videoPlayer.loadVideo(ctVideo);
    }

    private void updateStatus(PlayStatus playStatus) {
        switch (playStatus) {
            case PLAYING:
                mCompleteActions.setVisibility(View.GONE);
                videoPlayer.completePlayProgress();
                videoPlayer.setVideoViewVisibility(View.VISIBLE);
                break;
            case COMPLETE:
                mCompleteActions.setVisibility(View.VISIBLE);
                videoPlayer.setVideoViewVisibility(View.GONE);
                break;
        }
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replay_action:
                updateStatus(PlayStatus.PLAYING);
                videoPlayer.goOnPlay();
                break;
            case R.id.view_trip_action:
//                SchemeUrlHandler.getInstance().dispatch(VideoActivity.this, Uri.parse(ctVideo.getDispatchUrl()));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(headsetPlugReceiver);
        if (videoPlayer != null) {
            videoPlayer.close();
        }
    }

    public enum VideoState {Init, Playing, Pause}

    private enum PlayStatus {
        PLAYING, COMPLETE;
    }


}
