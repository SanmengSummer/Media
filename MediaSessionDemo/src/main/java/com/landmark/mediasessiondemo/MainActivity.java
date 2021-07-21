package com.landmark.mediasessiondemo;


import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.landmark.mediasessionlib.LogUtil;
import com.landmark.mediasessionlib.MediaPlayerManager;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MediaPlayerManager instance;
    private SeekBar mSeek;
    private List<MediaBrowserCompat.MediaItem> mediaItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = MediaPlayerManager.getInstance();
        instance.init(this, BrowserSubscriptionCallback);
        mSeek = findViewById(R.id.seek);
        mSeek.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start(View view) {
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        instance.setSurfaceView(surfaceView);
        Bundle bundle = new Bundle();
        if (mediaItemList == null) {
            Uri mediaUri = mediaItemList.get(0).getDescription().getMediaUri();
            bundle.putString(MediaPlayerManager.MEDIA_PLAYER_PATH, mediaUri.toString());
            instance.getTransportControls().playFromSearch(MediaPlayerManager.MEDIA_PLAYER_PATH, bundle);
        } else {
            AssetFileDescriptor afd = null;
            try {
                afd = getResources().getAssets().openFd("WAKE.mp4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            bundle.putParcelable(MediaPlayerManager.MEDIA_PLAYER_ASSETS, afd);
            instance.getTransportControls().playFromSearch(MediaPlayerManager.MEDIA_PLAYER_ASSETS, bundle);
        }
        mHandler.sendEmptyMessage(0);
    }

    public void play_pause(View view) {
        if (instance.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
            instance.getTransportControls().pause();
        } else if (instance.getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED) {
            instance.getTransportControls().play();
        }
    }

    public void stop(View view) {
        instance.getTransportControls().stop();
    }

    private final Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mHandler.sendEmptyMessageDelayed(0, 1000);
                PlaybackStateCompat playbackState = instance.getPlaybackState();
                LogUtil.error(playbackState + "");
                Bundle extras = playbackState.getExtras();
                long duration = extras.getLong(MediaPlayerManager.STATE_DURATION);
                long p = duration == 0 ? 0 : playbackState.getPosition() * 100 / duration;
                mSeek.setProgress((int) p);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        instance.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        instance.release();
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            LogUtil.debug("MediaSessionCompat  onStopTrackingTouch: ");
            PlaybackStateCompat playbackState = instance.getPlaybackState();
            LogUtil.error(playbackState + "");
            Bundle extras = playbackState.getExtras();
            long duration = extras.getLong(MediaPlayerManager.STATE_DURATION);
            long position = seekBar.getProgress() * duration / 100;
            instance.getTransportControls().seekTo(position);
        }
    };
    MediaBrowserCompat.SubscriptionCallback BrowserSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    mediaItemList = children;

                }

                @Override
                public void onError(@NonNull String parentId) {

                }
            };
}
