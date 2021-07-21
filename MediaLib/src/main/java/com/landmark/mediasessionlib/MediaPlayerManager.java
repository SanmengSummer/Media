package com.landmark.mediasessionlib;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Surface;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Author: chenhuaxia
 * Description: The manager of MediaPlayer, you can use player by this manager,etc connect,listener,controller.
 * Date: 2021/7/15 15:55
 **/
public class MediaPlayerManager {
    public final static String STATE_DURATION = "state_duration";
    public final static String ACTION_RELEASE = "action_release";
    public final static String MEDIA_PLAYER_ASSETS = "media_player_assets";
    public final static String MEDIA_PLAYER_PATH = "media_player_path";

    @SuppressLint("StaticFieldLeak")
    private static MediaPlayerManager mInstance;
    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mController;
    private MediaBrowserCompat.SubscriptionCallback mBrowserSubscriptionCallback;
    private Context mContext;

    public synchronized static MediaPlayerManager getInstance() {
        if (null == mInstance) {
            mInstance = new MediaPlayerManager();
        }
        return mInstance;
    }

    public void init(Context context, MediaBrowserCompat.SubscriptionCallback BrowserSubscriptionCallback) {
        mContext = context;
        mBrowserSubscriptionCallback = BrowserSubscriptionCallback;
        mMediaBrowser = new MediaBrowserCompat(context,
                new ComponentName(context, MediaService.class),
                BrowserConnectionCallback, null);
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        if (null != surfaceView) {
            Bundle bundle = new Bundle();
            Surface surface = surfaceView.getHolder().getSurface();
            bundle.putParcelable("surface", surface);
            mController.getTransportControls().sendCustomAction("surface", bundle);
        }
    }

    public MediaControllerCompat.TransportControls getTransportControls() {
        if (null != mController)
            return mController.getTransportControls();
        return null;
    }

    public PlaybackStateCompat getPlaybackState() {
        if (null != mController)
            return mController.getPlaybackState();
        return null;
    }

    private final MediaBrowserCompat.ConnectionCallback BrowserConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogUtil.debug("MediaBrowserCompat onConnected: ");
                    try {
                        mController = new MediaControllerCompat(mContext,
                                mMediaBrowser.getSessionToken());
                        mController.registerCallback(ControllerCallback);
                        String mediaId = mMediaBrowser.getRoot();
                        mMediaBrowser.unsubscribe(mediaId);
                        mMediaBrowser.subscribe(mediaId, mBrowserSubscriptionCallback);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConnectionFailed() {
                    LogUtil.debug("MediaBrowserCompat onConnectionFailed: ");
                }

                @Override
                public void onConnectionSuspended() {
                    LogUtil.debug("MediaBrowserCompat onConnectionSuspended: ");
                    super.onConnectionSuspended();
                }
            };

    private final MediaControllerCompat.Callback ControllerCallback = new MediaControllerCompat.Callback() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            LogUtil.debug("  MediaControllerCompat onPlaybackStateChanged: " + state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            LogUtil.debug("MediaControllerCompat onMetadataChanged: ");
        }

        @Override
        public void onSessionDestroyed() {
            LogUtil.debug("MediaControllerCompat onSessionDestroyed: ");
        }

        @Override
        public void onSessionEvent(@NonNull String event, @Nullable Bundle extras) {
            LogUtil.debug("MediaControllerCompat onSessionEvent: " + event);
        }

        @Override
        public void onQueueChanged(@Nullable List<MediaSessionCompat.QueueItem> queue) {
            LogUtil.debug("MediaControllerCompat onQueueChanged: ");
        }

        @Override
        public void onQueueTitleChanged(@Nullable CharSequence title) {
            LogUtil.debug("MediaControllerCompat onQueueTitleChanged: " + title);
        }

        @Override
        public void onExtrasChanged(@Nullable Bundle extras) {
            LogUtil.debug("MediaControllerCompat onExtrasChanged: ");
        }

        @Override
        public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
            LogUtil.debug("MediaControllerCompat onAudioInfoChanged: ");
        }

        @Override
        public void onSessionReady() {
            LogUtil.debug("MediaControllerCompat onSessionReady: ");
        }

        @Override
        public void onCaptioningEnabledChanged(boolean enabled) {
            super.onCaptioningEnabledChanged(enabled);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            super.onRepeatModeChanged(repeatMode);
        }

        @Override
        public void onShuffleModeChanged(int shuffleMode) {
            super.onShuffleModeChanged(shuffleMode);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public IMediaControllerCallback getIControllerCallback() {
            return super.getIControllerCallback();
        }

        @Override
        public void binderDied() {
            super.binderDied();
        }
    };

    public void connect() {
        if (!mMediaBrowser.isConnected())
            mMediaBrowser.connect();
    }

    public void disconnect() {
        if (mMediaBrowser.isConnected())
            mMediaBrowser.disconnect();
    }

    public void release() {
        mController.sendCommand(MediaPlayerManager.ACTION_RELEASE, null, null);
        mInstance = null;
        mContext = null;
        mController = null;
        mMediaBrowser = null;
    }
}
