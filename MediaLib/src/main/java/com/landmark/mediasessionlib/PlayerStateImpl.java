package com.landmark.mediasessionlib;

import android.media.MediaPlayer;

/**
 * Author: chenhuaxia
 * Description: The interface relate PlayerAdapter and MusicService listening native player`s callback.
 * Date: 2021/7/16 9:55
 **/
public interface PlayerStateImpl {
    void setPlayingPlaybackState();

    void setCompletionPlaybackState();

    void setBufferingUpdate(MediaPlayer mp, int percent);

    void onVideoSizeChanged(MediaPlayer mp, int width, int height);

    void onSeekComplete(MediaPlayer mp);

    boolean onInfo(MediaPlayer mp, int what, int extra);

    boolean onError(MediaPlayer mp, int what, int extra);
}
