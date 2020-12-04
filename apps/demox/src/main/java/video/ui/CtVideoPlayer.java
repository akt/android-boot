package video.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.demo.R;
import com.example.bizinterface.XUtil;

import video.MediaController;
import video.VideoActivity;

/**
 * video 播放控制器
 */
public class CtVideoPlayer extends RelativeLayout {

    private static final int TIME_UPDATE_PLAY_TIME = 200;
    private final int MSG_HIDE_CONTROLLER = 10;
    private final int MSG_UPDATE_PLAY_TIME = 11;
    TextView mBarMiddleItem;
    LinearLayout mLayoutTopbarContainer;

    private Context mContext;
    private CtVideoView mSuperVideoView;
    private MediaController mMediaController;
    private VideoPlayCallbackImpl mVideoPlayCallback;
    private View mProgressBarView;

    private CtVideo mNowPlayVideo;
    private int lastPlayTime = 0;

    private int totalTime;

    private int mSeekLastTime;

    private boolean playerToBeStop = false;

    private int lastPlayPosition = 0;
    private int lastCachePosition = 0;

    private boolean mSeeking;


    private boolean mNeedToSeek = false;//当播放状态丢失，重新回来，需要seek到之前的位置。
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_PLAY_TIME) {
                if (isPlayerToBeStop() || isSeeking()) {
                    return false;
                }
                totalTime = mSuperVideoView.getDuration();
                int playTime = mSuperVideoView.getCurrentPosition();
                updatePlayTime(playTime, totalTime);
                updatePlayProgress(playTime, totalTime);
                showProgress(playTime);
                mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PLAY_TIME, TIME_UPDATE_PLAY_TIME);
            } else if (msg.what == MSG_HIDE_CONTROLLER) {
                showOrHideController();
                showOrHideTitle();
            }
            return false;
        }
    });

    private OnTouchListener mOnTouchVideoListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                showOrHideController();
                showOrHideTitle();
            }
            return true;
        }
    };


    private MediaController.MediaControlImpl mMediaControl = new MediaController.MediaControlImpl() {
        @Override
        public void alwaysShowController() {
            CtVideoPlayer.this.alwaysShowController();
        }


        @Override
        public void onPlayTurn() {
            if (mSuperVideoView.isPlaying()) {
                pausePlay(true);
            } else {
                goOnPlay();
            }
        }

        @Override
        public void onPageTurn() {
            mVideoPlayCallback.onSwitchPageType();
        }

        @Override
        public void onProgressTurn(MediaController.ProgressState state, int progress) {
            if (state.equals(MediaController.ProgressState.START)) {
                setSeeking(true);
                mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            } else if (state.equals(MediaController.ProgressState.STOP)) {
                setSeeking(false);
                resetHideController();
                mSuperVideoView.seekTo(mSeekLastTime);
                onSeekBarProgressChanged(progress, true);
            } else if (state.equals(MediaController.ProgressState.DOING)) {
                onSeekBarProgressChanged(progress, false);
            }
        }
    };
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {

            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
                            || what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
                        hideProgressView();
                        if (mNeedToSeek) {
                            seekToLastPosition();
                            mNeedToSeek = false;
                        }
                        return true;
                    }
                    return false;
                }
            });

        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            stopUpdateTimer();
            stopHideController(true);
            mMediaController.playFinish(mSuperVideoView.getDuration());
            mVideoPlayCallback.onPlayFinish();
        }
    };

    public CtVideoPlayer(Context context) {
        super(context);
        initView(context);
    }

    public CtVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CtVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    protected boolean isSeeking() {
        return mSeeking;
    }

    protected void setSeeking(boolean isSeeking) {
        mSeeking = isSeeking;
    }

    protected void onSeekBarProgressChanged(int progress, boolean isFinishSeek) {
        if (isFinishSeek) {
            mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
            mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_TIME);
            updatePlayTime(mSeekLastTime, totalTime);
        } else {
            mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
            mSeekLastTime = Math.round(((float) progress / 100f) * totalTime);
            updatePlayTime(mSeekLastTime, totalTime);
        }
    }

    private void seekToLastPosition() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSuperVideoView.seekTo(lastPlayTime);
            }
        }, 1000);
    }

    private void showProgress(int currentPosition) {
        if (lastPlayPosition == currentPosition && (mSuperVideoView.isPlaying() || currentPosition == 0)) {
            showProgressView();
        } else {
            hideProgressView();
        }
        lastPlayPosition = currentPosition;
    }

    public void setVideoPlayCallback(VideoPlayCallbackImpl videoPlayCallback) {
        mVideoPlayCallback = videoPlayCallback;
    }

    /***
     * 强制横屏模式
     */
    @SuppressWarnings("unused")
    public void forceLandscapeMode() {
        mMediaController.forceLandscapeMode();
    }

    public void loadVideo(CtVideo video) {
        mNowPlayVideo = video;
        mBarMiddleItem.setText(mNowPlayVideo.getmVideoName());
        loadAndPlay(mNowPlayVideo.getmVideoUrl(), 0);
    }

    /**
     * 暂停播放
     *
     * @param isShowController 是否显示控制条
     */
    public void pausePlay(boolean isShowController) {
        mSuperVideoView.pause();
        mMediaController.setPlayState(MediaController.PlayState.PAUSE);
        hideProgressView();
        stopHideController(isShowController);
        stopUpdateTimer();
    }

    /***
     * 继续播放
     */
    public void goOnPlay() {
        mSuperVideoView.start();
        mMediaController.setPlayState(MediaController.PlayState.PLAY);
        resetHideController();
        resetUpdateTimer();
    }

    public void goOnPlayLastPosition(VideoActivity.VideoState state) {
        switch (state) {
            case Playing:
                goOnPlay();
                break;
            case Pause:
                pausePlay(true);
                break;
        }
        mNeedToSeek = true;
    }

    public void playVideoAtLastPos() {
//        mSuperVideoView.stopPlayback();
        loadAndPlay(mNowPlayVideo.getmVideoUrl(), lastPlayTime);
    }

    public boolean isPlayerToBeStop() {
        return playerToBeStop;
    }

    public void setPlayerToBeStop(boolean playerTobeStop) {
        this.playerToBeStop = playerTobeStop;
    }

    public void leaveVideo() {
        lastPlayTime = mSuperVideoView.getCurrentPosition();
    }

    /**
     * 关闭视频
     */
    public void close() {
        stopHideController(true);
        stopUpdateTimer();
        mSuperVideoView.stopPlayback();
        mSuperVideoView.setVisibility(GONE);
    }

    private void initView(Context context) {
        mContext = context;
        View.inflate(context, R.layout.full_video_player_layout, this);
        mBarMiddleItem = findViewById(R.id.bar_middle_item);
        mLayoutTopbarContainer = findViewById(R.id.layout_topbar_container);
        mSuperVideoView = (CtVideoView) findViewById(R.id.video_view);
        mMediaController = (MediaController) findViewById(R.id.controller);
        mProgressBarView = findViewById(R.id.progressbar);

        mMediaController.setMediaControl(mMediaControl);
        mSuperVideoView.setOnTouchListener(mOnTouchVideoListener);
        findViewById(R.id.layout_left_area).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlayCallback.onCloseVideo();
            }
        });
        showProgressView();

    }

    private void setTopBarVisibility(boolean isShow) {
        mLayoutTopbarContainer.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }


    /**
     * 加载并开始播放视频
     *
     * @param videoUrl videoUrl
     */
    private void loadAndPlay(String videoUrl, int seekTime) {
        showProgressView();
        mSuperVideoView.setOnPreparedListener(mOnPreparedListener);
        mSuperVideoView.setVideoPath(videoUrl);
        mSuperVideoView.setVisibility(VISIBLE);
        startPlayVideo(seekTime);
    }

    /**
     * 播放视频
     * should called after setVideoPath()
     */
    private void startPlayVideo(int seekTime) {
        resetUpdateTimer();
        resetHideController();
        mSuperVideoView.setOnCompletionListener(mOnCompletionListener);
        if (seekTime > 0) {
            mSuperVideoView.seekTo(seekTime);
        }
        mSuperVideoView.start();
        mMediaController.setPlayState(MediaController.PlayState.PLAY);
    }

    /**
     * 更新播放的进度时间
     */
    private void updatePlayTime(int playTime, int allTime) {
        mMediaController.setPlayProgressTxt(playTime, allTime);
    }

    /**
     * 更新播放进度条
     */
    private void updatePlayProgress(int currentTime, int allTime) {
        int loadProgress = mSuperVideoView.getBufferPercentage();
        if (loadProgress < lastCachePosition) {
            loadProgress = lastCachePosition;
        } else {
            lastCachePosition = loadProgress;
        }
        int progress = currentTime * 100 / allTime;
        mMediaController.setProgressBar(progress, loadProgress);
    }

    public void completePlayProgress() {
        mMediaController.setProgressBar(100, 100);
        hideProgressView();
    }

    /**
     * 显示loading圈
     */
    private void showProgressView() {
        if (mProgressBarView.getVisibility() != VISIBLE) {
            mProgressBarView.setVisibility(VISIBLE);
        }
    }

    private void hideProgressView() {
        if (mProgressBarView.getVisibility() != GONE) {
            mProgressBarView.setVisibility(GONE);
        }
    }

    /***
     *
     */
    private void showOrHideController() {
        /*if (mMediaController.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_exit_from_bottom);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mMediaController.setVisibility(View.GONE);
                }
            });
            mMediaController.startAnimation(animation);
        } else {
            mMediaController.setVisibility(View.VISIBLE);
            mMediaController.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_enter_from_bottom);
            mMediaController.startAnimation(animation);
            resetHideController();
        }*/
    }

    private void showOrHideTitle() {
        /*if (mLayoutTopbarContainer.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_exit_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mLayoutTopbarContainer.setVisibility(View.GONE);
                }
            });
            mLayoutTopbarContainer.startAnimation(animation);
        } else {
            mLayoutTopbarContainer.setVisibility(View.VISIBLE);
            mLayoutTopbarContainer.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.anim_enter_from_top);
            mLayoutTopbarContainer.startAnimation(animation);
        }*/
    }


    private void alwaysShowController() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.setVisibility(View.VISIBLE);
    }

    private void resetHideController() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        int TIME_SHOW_CONTROLLER = 4000;
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, TIME_SHOW_CONTROLLER);
    }

    private void stopHideController(boolean isShowController) {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.clearAnimation();
        mLayoutTopbarContainer.clearAnimation();
        mMediaController.setVisibility(isShowController ? View.VISIBLE : View.GONE);
        mLayoutTopbarContainer.setVisibility(isShowController ? View.VISIBLE : View.GONE);
    }

    private void resetUpdateTimer() {
        lastCachePosition = 0;
        lastPlayPosition = 0;
        mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
        mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_TIME);
    }

    private void stopUpdateTimer() {
        mHandler.removeMessages(MSG_UPDATE_PLAY_TIME);
    }

    public void setVideoViewVisibility(int visibility) {
        mSuperVideoView.setVisibility(visibility);
    }

    public boolean isPlaying() {
        return mSuperVideoView.isPlaying();
    }


    public interface VideoPlayCallbackImpl {
        void onCloseVideo();

        void onSwitchPageType();

        void onPlayFinish();
    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }

}