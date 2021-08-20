package com.landmark.mediasessionlib.controller;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.landmark.mediasessionlib.controller.MediaImpl.PlayerAdapter;
import com.landmark.mediasessionlib.controller.MediaImpl.PlayerStateImpl;
import com.landmark.mediasessionlib.controller.utils.MediaIdUtils;
import com.landmark.mediasessionlib.model.db.data.MediaIDHelper;
import com.landmark.mediasessionlib.controller.utils.LogUtils;
import com.landmark.mediasessionlib.controller.utils.LrcProcess;
import com.landmark.mediasessionlib.controller.utils.MP3ID3v2.MetaInfoParser_MP3;
import com.landmark.mediasessionlib.controller.utils.UriToPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.landmark.mediasessionlib.controller.utils.MediaIdUtils.*;
import static com.landmark.mediasessionlib.controller.MediaConfig.*;

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
    private static float mPlaybackSpeed = 1.0f;
    private static int mCurrentIndex = 0;
    private final static long distance = 5000;
    private static boolean isLoop = true;
    private static boolean hasGetLrcAction = false;

    private ArrayList<MediaBrowserCompat.MediaItem> mCurrentPlayList;
    private String currentMediaId;


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
        initState();
    }

    private final MediaSessionCompat.Callback SessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            LogUtils.debug("MediaSessionCompat  onPlay: ");
            if (mPlaybackState.getState() != PlaybackStateCompat.STATE_PLAYING) {
                mPlayerAdapter.play();
                buildState(PlaybackStateCompat.STATE_PLAYING);
            }
        }

        @Override
        public void onPause() {
            LogUtils.debug("MediaSessionCompat  onPause: ");
            if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mPlayerAdapter.pause();
                buildState(PlaybackStateCompat.STATE_PAUSED);
            }
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            LogUtils.debug("MediaSessionCompat onPlayFromUri: " + uri);
            try {
                mPlayerAdapter.preparePlayForUri(MediaService.this, uri);
                buildState(PlaybackStateCompat.STATE_CONNECTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            if (hasGetLrcAction) mHandler.removeMessages(HANDLER_CURRENT_INFO);
            try {
                if (query.equals(MEDIA_PLAYER_ASSETS)) {
                    AssetFileDescriptor assetFileDescriptor = extras.getParcelable(MEDIA_PLAYER_ASSETS);
                    mPlayerAdapter.preparePlayForAssets(assetFileDescriptor);
                } else if (query.equals(MEDIA_PLAYER_PATH)) {
                    String path = extras.getString(MEDIA_PLAYER_PATH);
                    mPlayerAdapter.preparePlayForPath(path);
                } else if (query.equals(MEDIA_PLAYER_LIST)) {
                    MediaBrowserCompat.MediaItem mediaItem = mCurrentPlayList.get(mCurrentIndex);
                    currentMediaId = mediaItem.getMediaId();
                    mPlayerAdapter.preparePlayForUri(MediaService.this,
                            mediaItem.getDescription().getMediaUri());
                }
                buildState(PlaybackStateCompat.STATE_CONNECTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (hasGetLrcAction) mHandler.sendEmptyMessage(HANDLER_CURRENT_INFO);
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
            LogUtils.debug("MediaSessionCompat  onCustomAction: " + action);
            switch (action) {
                case CUSTOMS_ACTION_SET_SURFACE:
                    Surface surface = extras.getParcelable(CUSTOMS_ACTION_SET_SURFACE);
                    mPlayerAdapter.setSurface(surface);
                    break;
                case CUSTOMS_ACTION_RETURN_CURRENT_INDEX:
                    currentIndexBack();
                    break;
                case CUSTOMS_ACTION_RETURN_CURRENT_POSITION:
                    hasGetLrcAction = true;
                    mHandler.sendEmptyMessage(HANDLER_CURRENT_INFO);
                    break;
            }
        }

        @Override
        public void onCommand(@NonNull String command, @Nullable Bundle args, @Nullable ResultReceiver cb) {
            super.onCommand(command, args, cb);
            LogUtils.debug("MediaSessionCompat  onCommand: " + command);
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
            LogUtils.debug("MediaSessionCompat  onPrepare: ");

        }

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
            super.onPrepareFromMediaId(mediaId, extras);
            LogUtils.debug("MediaSessionCompat  onPrepareFromMediaId: ");
        }

        @Override
        public void onPrepareFromSearch(String query, Bundle extras) {
            super.onPrepareFromSearch(query, extras);
            LogUtils.debug("MediaSessionCompat  onPrepareFromSearch: ");
        }

        @Override
        public void onPrepareFromUri(Uri uri, Bundle extras) {
            super.onPrepareFromUri(uri, extras);
            LogUtils.debug("MediaSessionCompat  onPrepareFromUri: ");
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            LogUtils.debug("MediaSessionCompat  onPlayFromMediaId: ");
        }

        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
            LogUtils.debug("MediaSessionCompat  onSkipToQueueItem: ");
            if (id < mCurrentPlayList.size() && id >= 0) {
                mCurrentIndex = (int) id;
                currentIndexBack();
                onPlayFromSearch(MEDIA_PLAYER_LIST, null);
            } else Toast.makeText(MediaService.this, "不存在该歌曲", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            LogUtils.debug("MediaSessionCompat  onSkipToNext: ");
            playNext();
        }


        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            LogUtils.debug("MediaSessionCompat  onSkipToPrevious: ");
            playPrevious();
        }

        private void playNext() {
            if (mPlaybackState.getState() == PlaybackState.STATE_ERROR) {
                Toast.makeText(MediaService.this, "播放错误", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCurrentIndex < mCurrentPlayList.size()) {
                mCurrentIndex++;
                currentIndexBack();
                onPlayFromSearch(MEDIA_PLAYER_LIST, null);
            } else {
                if (isLoop) {
                    mCurrentIndex = 0;
                    currentIndexBack();
                    onPlayFromSearch(MEDIA_PLAYER_LIST, null);
                } else
                    Toast.makeText(MediaService.this, "到底了", Toast.LENGTH_SHORT).show();
            }
        }

        private void playPrevious() {
            if (mCurrentIndex > 0) {
                mCurrentIndex--;
                currentIndexBack();
                onPlayFromSearch(MEDIA_PLAYER_LIST, null);
            } else {
                if (isLoop) {
                    mCurrentIndex = mCurrentPlayList.size();
                    currentIndexBack();
                    onPlayFromSearch(MEDIA_PLAYER_LIST, null);
                } else
                    Toast.makeText(MediaService.this, "到头了", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
            LogUtils.debug("MediaSessionCompat  onFastForward: ");
//            if (playbackSpeed >= 2) playbackSpeed = 1;
//            playbackSpeed += .2;
//            mPlayerAdapter.setPlaybackParams(playbackSpeed);
            long position = mPlayerAdapter.getCurrentPosition();
            long duration = mPlayerAdapter.getDuration();
            long dorwardPostion = position + distance >= duration ? duration : position + distance;
            onSeekTo(dorwardPostion);
        }

        @Override
        public void onRewind() {
            super.onRewind();
            LogUtils.debug("MediaSessionCompat  onRewind: ");
            long position = mPlaybackState.getPosition();
            long rewindPostion = position - distance <= 0 ? 0 : position - distance;
            onSeekTo(rewindPostion);
        }

        @Override
        public void onStop() {
            super.onStop();
            LogUtils.debug("MediaSessionCompat  onStop: ");
            mPlayerAdapter.stop();
            buildStateStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            LogUtils.debug("MediaSessionCompat  onSeekTo: ");
            mPlayerAdapter.seekTo((int) pos);
            buildStatePosition(pos);
        }

        @Override
        public void onSetRating(RatingCompat rating) {
            super.onSetRating(rating);
            LogUtils.debug("MediaSessionCompat  onSetRating: " + rating);
        }

        @Override
        public void onSetRating(RatingCompat rating, Bundle extras) {
            super.onSetRating(rating, extras);
            LogUtils.debug("MediaSessionCompat  onSetRating: " + rating + "  extras:" + extras);
        }

        @Override
        public void onSetCaptioningEnabled(boolean enabled) {
            super.onSetCaptioningEnabled(enabled);
            LogUtils.debug("MediaSessionCompat  onSetCaptioningEnabled: " + enabled);
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            LogUtils.debug("MediaSessionCompat  onSetRepeatMode: " + repeatMode);
            switch (repeatMode) {
                case PlaybackStateCompat.REPEAT_MODE_ONE:
                    break;
                case PlaybackStateCompat.REPEAT_MODE_ALL:
                case PlaybackStateCompat.REPEAT_MODE_GROUP:
                    isLoop = true;
                    break;
                case PlaybackStateCompat.REPEAT_MODE_NONE:
                case PlaybackStateCompat.REPEAT_MODE_INVALID:
                    isLoop = false;
                    break;
            }
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            switch (shuffleMode) {
                case PlaybackStateCompat.SHUFFLE_MODE_NONE:
                    mCurrentPlayList = QueueManager.getRandomPlayList(MediaService.this, MediaIdUtils.MEDIA_ID_ROOT);
                    break;
                case PlaybackStateCompat.SHUFFLE_MODE_ALL:
                    mCurrentPlayList = QueueManager.getSinglePlayList(MediaService.this, mCurrentIndex, MediaIdUtils.MEDIA_ID_ROOT);
                    break;
                case PlaybackStateCompat.SHUFFLE_MODE_GROUP:
                case PlaybackStateCompat.SHUFFLE_MODE_INVALID:
                    mCurrentPlayList = QueueManager.getOrderPlayList(MediaService.this, MediaIdUtils.MEDIA_ID_ROOT);
                    break;
            }
            getCurrentIndex();
            currentIndexBack();
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            super.onAddQueueItem(description);
            LogUtils.debug("MediaSessionCompat  onAddQueueItem: " + description);
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description, int index) {
            super.onAddQueueItem(description, index);
            LogUtils.debug("MediaSessionCompat  onAddQueueItem: " + description);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            super.onRemoveQueueItem(description);
            LogUtils.debug("MediaSessionCompat  onAddQueueItem: " + description);
        }

        @Override
        public void onRemoveQueueItemAt(int index) {
            super.onRemoveQueueItemAt(index);
            LogUtils.debug("MediaSessionCompat  onRemoveQueueItemAt: " + index);
        }

        @Override
        public void onSetPlaybackSpeed(float speed) {
            super.onSetPlaybackSpeed(speed);
            mPlayerAdapter.setPlaybackParams(speed);
            LogUtils.debug("MediaSessionCompat  onSetPlaybackSpeed: " + speed);
        }
    };

    private void currentIndexBack() {
        Bundle bundle = new Bundle();
        bundle.putInt(CUSTOMS_ACTION_RETURN_CURRENT_INDEX, mCurrentIndex);
        mSession.setExtras(bundle);
    }

    public int getCurrentIndex() {
        mCurrentIndex = 0;
        if (mCurrentPlayList != null && !mCurrentPlayList.isEmpty() && mCurrentPlayList.size() > 1)
            mCurrentPlayList.forEach(new Consumer<MediaBrowserCompat.MediaItem>() {
                @Override
                public void accept(MediaBrowserCompat.MediaItem mediaItem) {
                    if (mediaItem.getMediaId() == currentMediaId) {
                        mCurrentIndex = mCurrentPlayList.indexOf(mediaItem);
                    }
                }
            });
        Log.e("TAG", "getCurrentIndex: " + mCurrentIndex);
        mCurrentPlayList.forEach(new Consumer<MediaBrowserCompat.MediaItem>() {
            @Override
            public void accept(MediaBrowserCompat.MediaItem mediaItem) {
                Log.e("TAG", ": " + mediaItem.getMediaId());
            }
        });
        return mCurrentIndex;
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, Bundle rootHints) {
        return new BrowserRoot(MEDIA_ID_ROOT, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentMediaId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.detach();
        ArrayList<MediaBrowserCompat.MediaItem> mediaItems = QueueManager.getCurrentList(this, 0, 10, MediaIDHelper.getRootType(MediaIDHelper.TYPE_1));
        result.sendResult(mediaItems);
    }


    private void initState() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, 0);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE, 0, mPlaybackSpeed)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);

    }

    private void buildState(int state) {
        buildState(0, state);
    }

    private void buildState(int buffer, int state) {
        long position = 0;
        long duration = 0;
        if (state != PlaybackStateCompat.STATE_CONNECTING) {
            position = mPlayerAdapter == null ? 0 : mPlayerAdapter.getCurrentPosition();
            duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        }
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(state, position, mPlaybackSpeed)
                .setBufferedPosition(buffer)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private void buildStateStop() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, 0);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_STOPPED, 0, mPlaybackSpeed)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private void buildStatePosition(long position) {
        long duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, position, mPlaybackSpeed)
                .setExtras(bundle)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private void buildStateError(int what, int extra) {
        long position = mPlaybackState == null ? 0 : mPlaybackState.getPosition();
        long duration = mPlayerAdapter == null ? 0 : mPlayerAdapter.getDuration();
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_DURATION, duration);
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.ERROR_CODE_UNKNOWN_ERROR, position, mPlaybackSpeed)
                .setExtras(bundle)
                .setErrorMessage(what, "message: " + extra)
                .build();
        mSession.setPlaybackState(mPlaybackState);
    }

    private LrcProcess.LrcContent getLrcContent() {
        Bundle bundle = new Bundle();
        LrcProcess lrcProcess = new LrcProcess();
        LrcProcess.LrcContent mLrcContent = new LrcProcess.LrcContent();

        long currentPosition = mPlayerAdapter.getCurrentPosition();
        if (mCurrentIndex >= mCurrentPlayList.size() || mCurrentIndex < 0)
            return mLrcContent = new LrcProcess.LrcContent("", 0);
        Uri mediaUri = mCurrentPlayList.get(mCurrentIndex).getDescription().getMediaUri();
        String lrc = null;
        try {
            MetaInfoParser_MP3 metaInfoParser_mp3 = new MetaInfoParser_MP3();
            metaInfoParser_mp3.parse(UriToPathUtil.getRealFile(this, mediaUri));
            lrc = metaInfoParser_mp3.getLrc();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lrc != null && !lrc.isEmpty() && lrc != "Unknown") {
            lrcProcess.readLRCFormString(lrc, "UTF-8");
        } else {
            String realFilePath = UriToPathUtil.getRealFilePath(this, mediaUri);
            String lrcFilePath = realFilePath.replace("mp3", "lrc");
            File file = new File(lrcFilePath);
            if (file.exists()) lrcProcess.readLRC(file);
        }
        List<LrcProcess.LrcContent> lrcContentList = lrcProcess.getLrcContent();
        if (lrcContentList != null && !lrcContentList.isEmpty()) {
            if (lrcContentList.size() == 1) {
                mLrcContent = lrcContentList.get(0);
            } else
                for (int i = 0; i < lrcContentList.size(); i++) {
                    Integer lastLrcTime = 0;
                    if (lrcContentList.get(i) == null) {
                        continue;
                    }
                    Integer lrcTime = lrcContentList.get(i).getLrc_time();
                    if (i < lrcContentList.size() - 1)
                        lastLrcTime = lrcContentList.get(i + 1).getLrc_time();
                    if (lrcTime <= currentPosition && currentPosition < lastLrcTime) {
                        mLrcContent = lrcContentList.get(i);
                    }
                }
        }
        if (mLrcContent == null || mLrcContent.getLrc() == null)
            mLrcContent = new LrcProcess.LrcContent("暂无歌词！", 0);
        bundle.putLong(CUSTOMS_ACTION_RETURN_CURRENT_POSITION, currentPosition);
        bundle.putParcelable(CUSTOMS_ACTION_RETURN_CURRENT_LRC, mLrcContent);
        mSession.setExtras(bundle);
        return mLrcContent;
    }

    private final static int HANDLER_CURRENT_INFO = 100;
    private final static long handlerDelayMills = 1000;
    @SuppressLint({"HandlerLeak", "SetTextI18n"})
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_CURRENT_INFO) {
                if (hasGetLrcAction) {
                    mHandler.sendEmptyMessageDelayed(HANDLER_CURRENT_INFO, handlerDelayMills);
                    getLrcContent();
                }
            }
        }
    };

    @Override
    public void setPlayingPlaybackState() {
        buildState(PlaybackStateCompat.STATE_PLAYING);
    }

    @Override
    public void setCompletionPlaybackState() {
        SessionCallback.onSkipToNext();
    }

    @Override
    public void setBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtils.debug("setBufferingUpdate" + percent);
        if (percent >= 100)
            buildState(0, PlaybackStateCompat.STATE_PLAYING);
        else
            buildState(percent, PlaybackStateCompat.STATE_BUFFERING);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        LogUtils.debug("onVideoSizeChanged(MediaPlayer" + mp + ", " + "width" + width + "height" + height + " )");
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        LogUtils.debug("onSeekComplete");
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        LogUtils.debug("onInfo(MediaPlayer" + mp + ", " + "what" + what + "extra" + extra + " )");
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        buildStateError(what, extra);
        LogUtils.error("(MediaPlayer" + mp + ", " + "what" + what + "extra" + extra + " )");
        Toast.makeText(this, "该歌曲无法播放！", Toast.LENGTH_SHORT).show();
        return false;
    }
}



