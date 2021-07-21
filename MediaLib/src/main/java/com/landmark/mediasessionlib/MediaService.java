package com.landmark.mediasessionlib;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.media.MediaBrowserServiceCompat;

import java.util.ArrayList;
import java.util.List;

import static com.landmark.mediasessionlib.MediaPlayerManager.*;

/**
 * Author: chenhuaxia
 * Description: The Service of MediaPlayer, extend MediaBrowserServiceCompat compile MediaSession frame.
 * Date: 2021/7/20 15:55
 **/
@SuppressLint("NewApi")
public class MediaService extends MediaBrowserServiceCompat implements PlayerStateImpl {
    private MediaSessionCompat mSession;
    private PlaybackStateCompat mPlaybackState;
    private PlayerAdapter mPlayerAdapter;
    private static float playbackSpeed = 1.0f;

    @Override
    public void onCreate() {
        super.onCreate();
        mSession = new MediaSessionCompat(this, "MediaService");
        mSession.setCallback(SessionCallback);
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        setSessionToken(mSession.getSessionToken());
        mSession.setActive(true);
        mPlayerAdapter = PlayerAdapter.createPlayer();
        mPlayerAdapter.setOnPlayerStateImpl(this);
        buildState(PlaybackStateCompat.STATE_NONE);
    }

    private final MediaSessionCompat.Callback SessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            LogUtil.debug("MediaSessionCompat  onPlay: ");
            if (mPlaybackState.getState() != PlaybackStateCompat.STATE_PLAYING) {
                mPlayerAdapter.play();
                buildState(PlaybackStateCompat.STATE_PLAYING);
            }
        }

        @Override
        public void onPause() {
            LogUtil.debug("MediaSessionCompat  onPause: ");
            if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mPlayerAdapter.pause();
                buildState(PlaybackStateCompat.STATE_PAUSED);
            }
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            LogUtil.debug("MediaSessionCompat onPlayFromUri: " + uri);
            try {
                mPlayerAdapter.preparePlayForUri(MediaService.this, uri);
                buildState(PlaybackStateCompat.STATE_CONNECTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            try {
                if (query.equals(MEDIA_PLAYER_ASSETS)) {
                    AssetFileDescriptor assetFileDescriptor = extras.getParcelable(MEDIA_PLAYER_ASSETS);
                    mPlayerAdapter.preparePlayForAssets(assetFileDescriptor);
                } else if (query.equals(MEDIA_PLAYER_PATH)) {
                    String path = extras.getString(MEDIA_PLAYER_PATH);
                    mPlayerAdapter.preparePlayForPath(path);
                }
                buildState(PlaybackStateCompat.STATE_CONNECTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
            LogUtil.debug("MediaSessionCompat  onCustomAction: " + action);
            if (action.equals("surface")) {
                Surface surface = extras.getParcelable("surface");
                mPlayerAdapter.setSurface(surface);
            }
        }

        @Override
        public void onCommand(@NonNull String command, @Nullable Bundle args, @Nullable ResultReceiver cb) {
            super.onCommand(command, args, cb);
            LogUtil.debug("MediaSessionCompat  onCommand: " + command);
            if (command.equals(ACTION_RELEASE)) {
                mPlayerAdapter.release();
                MediaService.this.stopSelf();
            }
        }

        @Override
        public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
            return super.onMediaButtonEvent(mediaButtonIntent);
        }

        @Override
        public void onPrepare() {
            super.onPrepare();
            LogUtil.debug("MediaSessionCompat  onPrepare: ");

        }

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
            super.onPrepareFromMediaId(mediaId, extras);
            LogUtil.debug("MediaSessionCompat  onPrepareFromMediaId: ");
        }

        @Override
        public void onPrepareFromSearch(String query, Bundle extras) {
            super.onPrepareFromSearch(query, extras);
            LogUtil.debug("MediaSessionCompat  onPrepareFromSearch: ");
        }

        @Override
        public void onPrepareFromUri(Uri uri, Bundle extras) {
            super.onPrepareFromUri(uri, extras);
            LogUtil.debug("MediaSessionCompat  onPrepareFromUri: ");
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            LogUtil.debug("MediaSessionCompat  onPlayFromMediaId: ");
        }

        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
            LogUtil.debug("MediaSessionCompat  onSkipToQueueItem: ");
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            LogUtil.debug("MediaSessionCompat  onSkipToNext: ");
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            LogUtil.debug("MediaSessionCompat  onSkipToPrevious: ");
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
            LogUtil.debug("MediaSessionCompat  onFastForward: ");
            if (playbackSpeed >= 2) playbackSpeed = 1;
            playbackSpeed += .2;
            mPlayerAdapter.setPlaybackParams(playbackSpeed);
        }

        @Override
        public void onRewind() {
            super.onRewind();
            LogUtil.debug("MediaSessionCompat  onRewind: ");
            mPlayerAdapter.setPlaybackParams(1);
        }

        @Override
        public void onStop() {
            super.onStop();
            LogUtil.debug("MediaSessionCompat  onStop: ");
            mPlayerAdapter.stop();
            buildStateStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            LogUtil.debug("MediaSessionCompat  onSeekTo: ");
            mPlayerAdapter.seekTo((int) pos);
            buildStatePosition(pos);
        }

        @Override
        public void onSetRating(RatingCompat rating) {
            super.onSetRating(rating);
            LogUtil.debug("MediaSessionCompat  onSetRating: " + rating);
        }

        @Override
        public void onSetRating(RatingCompat rating, Bundle extras) {
            super.onSetRating(rating, extras);
            LogUtil.debug("MediaSessionCompat  onSetRating: " + rating + "  extras:" + extras);
        }

        @Override
        public void onSetCaptioningEnabled(boolean enabled) {
            super.onSetCaptioningEnabled(enabled);
            LogUtil.debug("MediaSessionCompat  onSetCaptioningEnabled: " + enabled);
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            LogUtil.debug("MediaSessionCompat  onSetRepeatMode: " + repeatMode);
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            LogUtil.debug("MediaSessionCompat  onSetShuffleMode: " + shuffleMode);
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            super.onAddQueueItem(description);
            LogUtil.debug("MediaSessionCompat  onAddQueueItem: " + description);
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description, int index) {
            super.onAddQueueItem(description, index);
            LogUtil.debug("MediaSessionCompat  onAddQueueItem: " + description);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            super.onRemoveQueueItem(description);
            LogUtil.debug("MediaSessionCompat  onAddQueueItem: " + description);
        }

        @Override
        public void onRemoveQueueItemAt(int index) {
            super.onRemoveQueueItemAt(index);
            LogUtil.debug("MediaSessionCompat  onRemoveQueueItemAt: " + index);
        }

        @Override
        public void onSetPlaybackSpeed(float speed) {
            super.onSetPlaybackSpeed(speed);
            LogUtil.debug("MediaSessionCompat  onSetPlaybackSpeed: " + speed);
        }
    };


    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, Bundle rootHints) {
        return new BrowserRoot("MEDIA_ID_ROOT", null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.detach();
        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, "https://media.w3.org/2010/05/sintel/trailer.mp4")
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, "http://10.1.1.62:8080/mediares/trailer.mp4")
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "圣诞歌")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "圣诞歌")
                .build();
        ArrayList<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        mediaItems.add(new MediaBrowserCompat.MediaItem(
                metadata.getDescription(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        ));
        result.sendResult(mediaItems);
    }


    private void buildState(int state) {
        long position = mPlaybackState == null ? 0 : mPlaybackState.getPosition();
        long duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(state, position, playbackSpeed)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private void buildState(int buffer, int state) {
        long position = mPlayerAdapter == null ? 0 : mPlayerAdapter.getCurrentPosition();
        long duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(state, position, playbackSpeed)
                .setBufferedPosition(buffer)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private void buildStateStop() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, 0);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_STOPPED, 0, playbackSpeed)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private void buildStatePosition(long position) {
        long duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, position, playbackSpeed)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    @SuppressLint("WrongConstant")
    private void buildStateError(int what, int extra) {
        long position = mPlaybackState == null ? 0 : mPlaybackState.getPosition();
        long duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.ERROR_CODE_UNKNOWN_ERROR, position, playbackSpeed)
                .setExtras(bundle)
                .setErrorMessage(what, "message: " + extra)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    @Override
    public void setPlayingPlaybackState() {
        buildState(PlaybackStateCompat.STATE_PLAYING);
    }

    @Override
    public void setCompletionPlaybackState() {
        buildState(PlaybackStateCompat.STATE_NONE);
    }

    @Override
    public void setBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.debug("setBufferingUpdate" + percent);
        if (percent >= 100)
            buildState(0, PlaybackStateCompat.STATE_PLAYING);
        else
            buildState(percent, PlaybackStateCompat.STATE_BUFFERING);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        buildStateError(what, extra);
        return false;
    }
}



