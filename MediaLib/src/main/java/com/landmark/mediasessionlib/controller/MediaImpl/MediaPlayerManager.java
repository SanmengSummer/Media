package com.landmark.mediasessionlib.controller.MediaImpl;

import static com.landmark.mediasessionlib.controller.MediaConfig.*;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.landmark.mediasessionlib.controller.MediaService;
import com.landmark.mediasessionlib.controller.bean.LastModeBean;
import com.landmark.mediasessionlib.controller.utils.LogUtils;
import com.landmark.mediasessionlib.controller.utils.LrcProcess;
import com.landmark.mediasessionlib.controller.utils.SPUtils;

import java.util.List;

/**
 * Author: chenhuaxia
 * Description: The manager of MediaPlayer, you can use player by this manager,etc connect,listener,controller.
 * Date: 2021/7/15 15:55
 **/
@RequiresApi(api = Build.VERSION_CODES.R)
public class MediaPlayerManager {
    public static final int random = PlaybackStateCompat.SHUFFLE_MODE_NONE;
    public static final int single = PlaybackStateCompat.SHUFFLE_MODE_ALL;
    public static final int order = PlaybackStateCompat.SHUFFLE_MODE_GROUP;
    @SuppressLint("StaticFieldLeak")
    private static MediaPlayerManager mInstance;
    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mController;
    private MediaBrowserCompat.SubscriptionCallback mBrowserSubscriptionCallback;
    private MediaControllerCompat.Callback mControllerCallback;
    private Context mContext;
    private String mMediaId;
    private List<MediaBrowserCompat.MediaItem> mMediaItemList;
    private List<MediaBrowserCompat.MediaItem> mPlayMediaItemList;
    private long mCurrentPosition;
    private int mCurrentIndex = 0;
    private int mMode = order;
    private boolean isLoop = false;

    public synchronized static MediaPlayerManager getInstance() {
        if (null == mInstance) {
            mInstance = new MediaPlayerManager();
        }
        return mInstance;
    }

    public void connectMediaSession(Context context,
                                    String mediaId) {
        mControllerCallback = ControllerCallback;
        mBrowserSubscriptionCallback = BrowserSubscriptionCallback;
        initConnect(context, mediaId);
    }

    public void connectMediaSession(Context context,
                                    MediaControllerCompat.Callback iControllerCallback,
                                    String mediaId) {

        mControllerCallback = iControllerCallback;
        mBrowserSubscriptionCallback = BrowserSubscriptionCallback;
        initConnect(context, mediaId);
    }

    public void connectMediaSession(Context context,
                                    MediaBrowserCompat.SubscriptionCallback iBrowserSubscriptionCallback,
                                    MediaControllerCompat.Callback iControllerCallback,
                                    String mediaId) {
        mControllerCallback = iControllerCallback;
        mBrowserSubscriptionCallback = iBrowserSubscriptionCallback;
        initConnect(context, mediaId);
    }

    private void initConnect(Context context, String mediaId) {
        mContext = context;
        mMediaId = mediaId;
        mMediaBrowser = new MediaBrowserCompat(context,
                new ComponentName(context, MediaService.class),
                BrowserConnectionCallback, null);
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        if (mController == null || null == surfaceView) return;
        Bundle bundle = new Bundle();
        SurfaceHolder holder = surfaceView.getHolder();
        setSurface(holder, bundle);
    }

    private void setSurface(@NonNull SurfaceHolder holder, Bundle bundle) {
        Surface surface = holder.getSurface();
        bundle.putParcelable(CUSTOMS_ACTION_SET_SURFACE, surface);
        getTransportControls().sendCustomAction(CUSTOMS_ACTION_SET_SURFACE, bundle);
    }

    private MediaControllerCompat.TransportControls transportControls = null;

    public MediaControllerCompat.TransportControls getTransportControls() {
        if (null != mController && transportControls == null)
            transportControls = mController.getTransportControls();
        return transportControls;
    }

    public PlaybackStateCompat getPlaybackState() {
        if (null != mController)
            return mController.getPlaybackState();
        return null;
    }

    public void getNativeMediaList(MediaListCallback mMediaListCallback) {
        this.mMediaListCallback = mMediaListCallback;
    }

    public void setOnMediaListDataChangeCallback(MediaListDataChangeCallback MediaListDataChangeCallback) {
        getTransportControls().sendCustomAction(CUSTOMS_ACTION_RETURN_CURRENT_POSITION, null);
        if (mMediaListDataChangeCallback == null)
            mMediaListDataChangeCallback = MediaListDataChangeCallback;
    }

    public void setPlayerMode(int mode, boolean isLoop) {
        this.mMode = mode;
        this.isLoop = isLoop;
        if (getTransportControls() == null) return;
        setPlayerMediaItemList(mode);
        getTransportControls().setRepeatMode(isLoop ? PlaybackStateCompat.REPEAT_MODE_ALL :
                PlaybackStateCompat.REPEAT_MODE_NONE);
    }

    private void setPlayerMediaItemList(int mode) {
        getTransportControls().setShuffleMode(mode);
    }

    public void setPlayerLastMode() {
        LastModeBean lastModeBean = new LastModeBean();
        lastModeBean.setMediaItemList(mMediaItemList);
        lastModeBean.setLastPlayerMode(mMode);
        lastModeBean.setLastLoopMode(isLoop);
        lastModeBean.setLastMediaIndex(mCurrentIndex);
        lastModeBean.setLastMediaID(mMediaItemList.get(mCurrentIndex).getMediaId());
        lastModeBean.setLastTimePosition(mCurrentPosition);
        SPUtils.getInstance().put("LastMode", new Gson().toJson(lastModeBean));
    }

    public LastModeBean getPlayerLastMode() {
        String lastMode = SPUtils.getInstance().getString("LastMode");
        Gson gson = new Gson();
        return gson.fromJson(lastMode, LastModeBean.class);
    }

    private final MediaBrowserCompat.ConnectionCallback BrowserConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogUtils.debug("MediaBrowserCompat onConnected: ");
                    try {
                        connectToSession(mMediaId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConnectionFailed() {
                    LogUtils.debug("MediaBrowserCompat onConnectionFailed: ");
                }

                @Override
                public void onConnectionSuspended() {
                    LogUtils.debug("MediaBrowserCompat onConnectionSuspended: ");
                    super.onConnectionSuspended();
                }
            };

    private void connectToSession(@Nullable String mediaId) {
        if (mController == null)
            mController = new MediaControllerCompat(mContext, mMediaBrowser.getSessionToken());
        mController.registerCallback(mControllerCallback);
        if (mediaId == null)
            mediaId = mMediaBrowser.getRoot();
        mMediaBrowser.unsubscribe(mediaId);
        mMediaBrowser.subscribe(mediaId, mBrowserSubscriptionCallback);
    }

    public void connect() {
        if (!mMediaBrowser.isConnected())
            mMediaBrowser.connect();
    }

    public void disconnect() {
        if (mMediaBrowser.isConnected())
            mMediaBrowser.disconnect();
    }

    public void release() {
        mController.sendCommand(ACTION_RELEASE, null, null);
        mInstance = null;
        mContext = null;
        mController = null;
        mMediaBrowser = null;
    }

    private MediaListDataChangeCallback mMediaListDataChangeCallback;

    public interface MediaListDataChangeCallback {
        void getMediaListDataChangeCallback(long currentPosition, LrcProcess.LrcContent mLrcContent);
    }

    private MediaListCallback mMediaListCallback;

    public interface MediaListCallback {
        void getMediaList(List<MediaBrowserCompat.MediaItem> children);
    }

    MediaBrowserCompat.SubscriptionCallback BrowserSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    mMediaItemList = children;
                    setPlayerMediaItemList(mMode);
                    mMediaListCallback.getMediaList(mMediaItemList);
                }

                @Override
                public void onError(@NonNull String parentId) {

                }
            };

    MediaControllerCompat.Callback ControllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onSessionReady() {
            super.onSessionReady();
            LogUtils.debug("MediaControllerCompat onSessionReady: ");
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            LogUtils.debug("MediaControllerCompat onSessionDestroyed: ");
        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
            LogUtils.debug("MediaControllerCompat onSessionEvent: ");
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            LogUtils.debug("MediaControllerCompat onPlaybackStateChanged: " + state.toString());
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            LogUtils.debug("MediaControllerCompat onMetadataChanged: ");
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
            LogUtils.debug("MediaControllerCompat onMetadataChanged: ");
        }

        @Override
        public void onQueueTitleChanged(CharSequence title) {
            super.onQueueTitleChanged(title);
            LogUtils.debug("MediaControllerCompat onQueueTitleChanged: ");
        }

        @Override
        public void onExtrasChanged(Bundle extras) {
            super.onExtrasChanged(extras);
            mCurrentIndex = extras.getInt(CUSTOMS_ACTION_RETURN_CURRENT_INDEX);
            try {
                if (mMediaListDataChangeCallback != null) {
                    mCurrentPosition = extras.getLong(CUSTOMS_ACTION_RETURN_CURRENT_POSITION);
                    LrcProcess.LrcContent mLrcContent = extras.getParcelable(CUSTOMS_ACTION_RETURN_CURRENT_LRC);
                    if (mCurrentPosition > -1 && mLrcContent != null)
                        mMediaListDataChangeCallback.getMediaListDataChangeCallback(mCurrentPosition, mLrcContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
            super.onAudioInfoChanged(info);
            LogUtils.debug("MediaControllerCompat onAudioInfoChanged: ");
        }

        @Override
        public void onCaptioningEnabledChanged(boolean enabled) {
            super.onCaptioningEnabledChanged(enabled);
            LogUtils.debug("MediaControllerCompat onCaptioningEnabledChanged: ");
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            super.onRepeatModeChanged(repeatMode);
            LogUtils.debug("MediaControllerCompat onRepeatModeChanged: ");
        }

        @Override
        public void onShuffleModeChanged(int shuffleMode) {
            super.onShuffleModeChanged(shuffleMode);
            LogUtils.debug("MediaControllerCompat onShuffleModeChanged: ");
        }

        @SuppressLint("RestrictedApi")
        @Override
        public IMediaControllerCallback getIControllerCallback() {
            LogUtils.debug("MediaControllerCompat getIControllerCallback: ");
            return super.getIControllerCallback();
        }

        @Override
        public void binderDied() {
            super.binderDied();
            LogUtils.debug("MediaControllerCompat binderDied: ");
        }
    };
}

