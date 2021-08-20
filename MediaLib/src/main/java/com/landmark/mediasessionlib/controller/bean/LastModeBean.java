package com.landmark.mediasessionlib.controller.bean;

import android.support.v4.media.MediaBrowserCompat;

import java.util.List;

/**
 * Author: chenhuaxia
 * Description:
 * Date: 2021/8/17 14:41
 **/
public class LastModeBean {
    private long lastTimePosition;
    private int lastMediaIndex;
    private String lastMediaID;
    private int lastPlayerMode;
    private boolean lastLoopMode;
    private List<MediaBrowserCompat.MediaItem> mediaItemList;


    public String getLastMediaID() {
        return lastMediaID;
    }

    public void setLastMediaID(String lastMediaID) {
        this.lastMediaID = lastMediaID;
    }

    public long getLastTimePosition() {
        return lastTimePosition;
    }

    public void setLastTimePosition(long lastTimePosition) {
        this.lastTimePosition = lastTimePosition;
    }

    public int getLastMediaIndex() {
        return lastMediaIndex;
    }

    public void setLastMediaIndex(int lastMediaIndex) {
        this.lastMediaIndex = lastMediaIndex;
    }

    public int getLastPlayerMode() {
        return lastPlayerMode;
    }

    public void setLastPlayerMode(int lastPlayerMode) {
        this.lastPlayerMode = lastPlayerMode;
    }

    public boolean isLastLoopMode() {
        return lastLoopMode;
    }

    public void setLastLoopMode(boolean lastLoopMode) {
        this.lastLoopMode = lastLoopMode;
    }

    public List<MediaBrowserCompat.MediaItem> getMediaItemList() {
        return mediaItemList;
    }

    public void setMediaItemList(List<MediaBrowserCompat.MediaItem> mediaItemList) {
        this.mediaItemList = mediaItemList;
    }
}
