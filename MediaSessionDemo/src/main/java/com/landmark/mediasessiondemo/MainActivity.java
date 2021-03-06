package com.landmark.mediasessiondemo;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.landmark.mediasessionlib.controller.MediaImpl.MediaPlayerManager;
import com.landmark.mediasessionlib.controller.utils.LogUtils;
import com.landmark.mediasessionlib.controller.utils.MediaIdUtils;

import static com.landmark.mediasessionlib.controller.MediaConfig.*;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {

    private MediaPlayerManager instance;
    private SeekBar mSeek;
    private RecyclerView mPlayerListView;
    private TextView textTitle;
    private Button btnPlayMode;
    private ArrayList<MediaBrowserCompat.MediaItem> mediaItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTitle = findViewById(R.id.text_title);
        mPlayerListView = findViewById(R.id.rv_player_list);
        btnPlayMode = findViewById(R.id.play_mode);

        mSeek = findViewById(R.id.seek);
        mSeek.setOnSeekBarChangeListener(onSeekBarChangeListener);
        requestPermissions(101);
        instance = MediaPlayerManager.getInstance();
        instance.connectMediaSession(this, MediaIdUtils.MEDIA_ID_ROOT);

        instance.getNativeMediaList(list -> {
            mediaItemList = (ArrayList<MediaBrowserCompat.MediaItem>) list;

            mPlayerListView.setAdapter(new RecyclerViewAdapter(this, mediaItemList));

        });

    }


    public void requestPermissions(int requestCode) {
        try {
            ArrayList<String> requestPrecessionArr = new ArrayList<>();
            int hasSdcardWrite = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasSdcardWrite != PackageManager.PERMISSION_GRANTED)
                requestPrecessionArr.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (requestPrecessionArr.size() >= 1) {
                String[] requestArray = new String[requestPrecessionArr.size()];
                for (int i = 0; i < requestArray.length; i++) {
                    requestArray[i] = requestPrecessionArr.get(i);
                }
                requestPermissions(requestArray, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(View view) {
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        instance.setSurfaceView(surfaceView);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MEDIA_PLAYER_LIST, mediaItemList);
        instance.getTransportControls().playFromSearch(MEDIA_PLAYER_LIST, bundle);

        instance.setOnMediaListDataChangeCallback((currentPosition, mLrcContent) -> {
            updateSeekBar();
            if (mLrcContent != null && !mLrcContent.getLrc_time().equals(-1))
                textTitle.setText(mLrcContent.getLrc());
            else textTitle.setText("????????????!");
        });

    }

    public void play_pause(View view) {
        if (instance.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
            instance.getTransportControls().pause();
        else if (instance.getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED)
            instance.getTransportControls().play();
    }

    public void stop(View view) {
        instance.getTransportControls().stop();
    }

    private static int playMode = 0;

    public void play_mode(View v) {
        playMode++;
        setPlayMode((Button) v);
    }

    private void setPlayMode(Button view) {
        switch (playMode) {
            case 1:
                view.setText("????????????");
                instance.setPlayerMode(MediaPlayerManager.random, false);
                break;
            case 2:
                view.setText("????????????");
                instance.setPlayerMode(MediaPlayerManager.single, false);
                break;
            case 3:
                view.setText("????????????");
                instance.setPlayerMode(MediaPlayerManager.order, true);
                break;
            case 4:
                view.setText("????????????");
                instance.setPlayerMode(MediaPlayerManager.random, true);
                break;
            case 5:
                view.setText("????????????");
                instance.setPlayerMode(MediaPlayerManager.single, true);
                break;
            case 6:
            case 0:
                playMode = 0;
                view.setText("????????????");
                instance.setPlayerMode(MediaPlayerManager.order, false);
                break;
        }
    }

    private void updateSeekBar() {
        PlaybackStateCompat playbackState = instance.getPlaybackState();
        long position = playbackState.getPosition();
        Bundle extras = playbackState.getExtras();
        long duration = 0;
        if (extras != null)
            duration = extras.getLong(STATE_DURATION);
        long p = duration == 0 ? 0 : position * 100 / duration;
        mSeek.setProgress((int) p);
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance.connect();
        setPlayMode(btnPlayMode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.disconnect();
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
            LogUtils.debug("MediaSessionCompat  onStopTrackingTouch: ");
            PlaybackStateCompat playbackState = instance.getPlaybackState();
            LogUtils.error(playbackState + "");
            Bundle extras = playbackState.getExtras();
            long duration = extras.getLong(STATE_DURATION);
            long position = seekBar.getProgress() * duration / 100;
            instance.getTransportControls().seekTo(position);
        }
    };

    public void skipNext(View view) {
        instance.getTransportControls().skipToNext();
    }

    public void skipPrevious(View view) {
        instance.getTransportControls().skipToPrevious();
    }

    public void rewind(View view) {
        instance.getTransportControls().rewind();
    }

    public void fastForward(View view) {
        instance.getTransportControls().fastForward();
    }
}
