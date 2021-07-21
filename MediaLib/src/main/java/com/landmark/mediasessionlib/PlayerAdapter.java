package com.landmark.mediasessionlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.view.Surface;


import java.io.IOException;

/**
 * Author: chenhuaxia
 * Description: The adapter of player on MediaPlayer , you can replace a new player (exoplayer,ijkplayer,vitamio ...).
 * Date: 2021/7/16 9:53
 **/
@SuppressLint("NewApi")
public class PlayerAdapter {
    private PlayerStateImpl mPlayerImpl;
    private static MediaPlayer mMediaPlayer = null;
    private static PlayerAdapter mInstance;

    public synchronized static PlayerAdapter createPlayer() {
        if (null == mMediaPlayer)
            mMediaPlayer = new MediaPlayer();
        if (null == mInstance)
            mInstance = new PlayerAdapter();
        mMediaPlayer.setOnPreparedListener(mInstance.PreparedListener);
        mMediaPlayer.setOnBufferingUpdateListener(mInstance.BufferingUpdateListener);
        mMediaPlayer.setOnCompletionListener(mInstance.CompletionListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mInstance.VideoSizeChangedListener);
        mMediaPlayer.setOnSeekCompleteListener(mInstance.SeekCompleteListener);
        mMediaPlayer.setOnInfoListener(mInstance.InfoListener);
        mMediaPlayer.setOnErrorListener(mInstance.ErrorListener);
        return mInstance;
    }

    public void setOnPlayerStateImpl(PlayerStateImpl player) {
        mPlayerImpl = player;
    }

    public void stop() {
        mMediaPlayer.stop();
    }

    public void play() {
        if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
    }

    public void reset() {
        mMediaPlayer.reset();
    }

    public void setSurface(Surface surface) {
        mMediaPlayer.setSurface(surface);
    }

    public void release() {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();

    }

    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    public long getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    public long getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    public void setVideoScalingMode(int scalingMode) {
        mMediaPlayer.setVideoScalingMode(scalingMode);
    }

    public void setPlaybackParams(float speed) {
        PlaybackParams playbackParams = mMediaPlayer.getPlaybackParams();
        playbackParams.setSpeed(speed);
        mMediaPlayer.setPlaybackParams(playbackParams);
    }


    public void preparePlayForUri(Context context, Uri uri) {
        LogUtil.debug("preparePlayForUri: " + uri);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preparePlayForAssets(AssetFileDescriptor assetFileDescriptor) {
        LogUtil.debug(mMediaPlayer + "preparePlayForAssets: " + assetFileDescriptor);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preparePlayForPath(String path) {
        LogUtil.debug(mMediaPlayer + "preparePlayForPath: " + path);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mMediaPlayer.start();
            mPlayerImpl.setPlayingPlaybackState();
        }
    };

    private final MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mPlayerImpl.setCompletionPlaybackState();
        }
    };
    private final MediaPlayer.OnBufferingUpdateListener BufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mPlayerImpl.setBufferingUpdate(mp, percent);
        }
    };
    private final MediaPlayer.OnVideoSizeChangedListener VideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mPlayerImpl.onVideoSizeChanged(mp, width, height);
        }
    };
    private final MediaPlayer.OnSeekCompleteListener SeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            mPlayerImpl.onSeekComplete(mp);
        }
    };
    private final MediaPlayer.OnInfoListener InfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return mPlayerImpl.onInfo(mp, what, extra);
        }
    };
    private final MediaPlayer.OnErrorListener ErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return mPlayerImpl.onError(mp, what, extra);
        }
    };
}
