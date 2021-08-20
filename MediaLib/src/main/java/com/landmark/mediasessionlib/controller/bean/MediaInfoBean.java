package com.landmark.mediasessionlib.controller.bean;

import android.graphics.Bitmap;

/**
 * Author: chenhuaxia
 * Description:
 * Date: 2021/8/13 16:58
 **/
public class MediaInfoBean {
    private String MediaId;
    private String MediaArtist;
    private String MediaFolder;
    private String MediaTitle;
    private String MediaEngTag;
    private String MediaAlbum;
    private String MediaPath;
    private String MediaSize;
    private String MediaDuration;
    private String MediaGenre;
    private String MediaClass;
    private String MediaYear;
    private boolean isCollect;
    private Bitmap MediaIconBitmap;

    public String getMediaGenre() {
        return MediaGenre;
    }

    public void setMediaGenre(String mediaGenre) {
        MediaGenre = mediaGenre;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getMediaArtist() {
        return MediaArtist;
    }

    public void setMediaArtist(String mediaArtist) {
        MediaArtist = mediaArtist;
    }

    public String getMediaFolder() {
        return MediaFolder;
    }

    public void setMediaFolder(String mediaFolder) {
        MediaFolder = mediaFolder;
    }

    public String getMediaTitle() {
        return MediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        MediaTitle = mediaTitle;
    }

    public String getMediaEngTag() {
        return MediaEngTag;
    }

    public void setMediaEngTag(String mediaEngTag) {
        MediaEngTag = mediaEngTag;
    }

    public String getMediaAlbum() {
        return MediaAlbum;
    }

    public void setMediaAlbum(String mediaAlbum) {
        MediaAlbum = mediaAlbum;
    }

    public String getMediaPath() {
        return MediaPath;
    }

    public void setMediaPath(String mediaPath) {
        MediaPath = mediaPath;
    }

    public String getMediaSize() {
        return MediaSize;
    }

    public void setMediaSize(String mediaSize) {
        MediaSize = mediaSize;
    }

    public String getMediaDuration() {
        return MediaDuration;
    }

    public void setMediaDuration(String mediaDuration) {
        MediaDuration = mediaDuration;
    }

    public String getMediaClass() {
        return MediaClass;
    }

    public void setMediaClass(String mediaClass) {
        MediaClass = mediaClass;
    }

    public String getMediaYear() {
        return MediaYear;
    }

    public void setMediaYear(String mediaYear) {
        MediaYear = mediaYear;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public Bitmap getMediaIconBitmap() {
        return MediaIconBitmap;
    }

    public void setMediaIconBitmap(Bitmap mediaIconBitmap) {
        MediaIconBitmap = mediaIconBitmap;
    }
}
