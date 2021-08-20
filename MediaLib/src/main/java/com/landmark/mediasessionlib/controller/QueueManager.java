
package com.landmark.mediasessionlib.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.landmark.mediasessionlib.R;
import com.landmark.mediasessionlib.controller.bean.MediaInfoBean;
import com.landmark.mediasessionlib.model.db.data.MediaDataHelper;
import com.landmark.mediasessionlib.model.model.MediaData;
import com.landmark.mediasessionlib.model.model.MediaDataModel;
import com.landmark.mediasessionlib.controller.utils.LogUtils;
import com.landmark.mediasessionlib.controller.utils.MP3ID3v2.MP3ReadID3v2;
import com.landmark.mediasessionlib.controller.utils.UriToPathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class to help on queue related tasks.
 */

@SuppressLint("NewApi")
public class QueueManager {

    private static MediaData musicDataList;
    private static ArrayList<MediaBrowserCompat.MediaItem> mediaItems;
    private static ArrayList<MediaBrowserCompat.MediaItem> mCurrentMediaItems = new ArrayList<>();

    public static ArrayList<MediaBrowserCompat.MediaItem> getCurrentList(Context mContext, int page, int size, String type) {
        mediaItems = new ArrayList<>();
        musicDataList = MediaDataHelper.getInstance(mContext).getMusicDataList(page, size, type);
        List<MediaDataModel> data = musicDataList.getData();
        for (int i = 0; i < data.size(); i++) {
            MediaDataModel mediaDataModel = data.get(i);
            String path = mediaDataModel.getPath();
            Uri uri = UriToPathUtil.getUri(path);
            MediaInfoBean mediaInfo = getMediaInfo(mContext, uri);
            String albumName = mediaDataModel.getAlbumVo() == null ? mediaInfo.getMediaAlbum() : mediaDataModel.getAlbumVo().getName();
            String genreName = mediaDataModel.getGenreVo() == null ? "" : mediaDataModel.getGenreVo().getName();
            String singerName = mediaDataModel.getSingerVo() == null ? mediaInfo.getMediaArtist() : mediaDataModel.getSingerVo().getName();
            String name = mediaDataModel.getName();

            MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, UriToPathUtil.getRealFilePath(mContext, uri))
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, name)
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, mediaInfo.getMediaIconBitmap())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                    .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genreName)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, singerName)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, albumName)
                    .build();
            mediaItems.add(new MediaBrowserCompat.MediaItem(
                    metadata.getDescription(),
                    MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            ));
        }
        return mediaItems;
    }

    public static ArrayList<MediaBrowserCompat.MediaItem> getRandomPlayList(Context mContext, String type) {
        if (musicDataList == null || mediaItems == null || mediaItems.isEmpty())
            mediaItems = getCurrentList(mContext, 0, 10, type);
        mCurrentMediaItems = new ArrayList<>(mediaItems);
        Collections.shuffle(mCurrentMediaItems);
        return mCurrentMediaItems;
    }

    public static ArrayList<MediaBrowserCompat.MediaItem> getSinglePlayList(Context mContext, int currentIndex, String type) {
        if (musicDataList == null || mediaItems == null || mediaItems.isEmpty())
            mediaItems = getCurrentList(mContext, 0, 10, type);
        MediaBrowserCompat.MediaItem mediaItem = mCurrentMediaItems.get(currentIndex);
        mCurrentMediaItems.clear();
        mCurrentMediaItems.add(mediaItem);
        return mCurrentMediaItems;
    }

    public static ArrayList<MediaBrowserCompat.MediaItem> getOrderPlayList(Context mContext, String type) {
        if (musicDataList == null || mediaItems == null || mediaItems.isEmpty())
            mediaItems = getCurrentList(mContext, 0, 10, type);
        mCurrentMediaItems.clear();
        mCurrentMediaItems.addAll(mediaItems);
        return mCurrentMediaItems;
    }

    private static MediaInfoBean getMediaInfo(Context mContext, Uri mediaUri) {
        MediaInfoBean mediaInfoBean = new MediaInfoBean();
        Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.play_img_default);
        mediaInfoBean.setMediaIconBitmap(bitmap1);
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mContext, mediaUri);
            mediaInfoBean.setMediaTitle(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            mediaInfoBean.setMediaAlbum(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            mediaInfoBean.setMediaArtist(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

            if (mediaUri.toString().contains(".mp4")) {
                int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));//时长(毫秒)
                if (duration >= 10 * 1000) {
                    Bitmap bitmap = retriever.getFrameAtTime(10 * 1000L, MediaMetadataRetriever.OPTION_CLOSEST);
                    if (bitmap != null)
                        mediaInfoBean.setMediaIconBitmap(bitmap);
                    else {
                        LogUtils.debug("bitmap == null");
                    }
                } else {
                    LogUtils.debug("the time is out of video");
                }
            } else {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(this, mediaUri);
//                LogUtils.debug("getAuthor:" + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
//                LogUtils.debug("123:" + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));

//                MetaInfoParser_MP3 metaInfoParser_mp3 = new MetaInfoParser_MP3();
//                metaInfoParser_mp3.parse(UriToPathUtil.getRealFile(this, mediaUri));
//                LogUtils.debug("getArtist: " + metaInfoParser_mp3.getArtist());
//                LogUtils.debug("getTitle: " + metaInfoParser_mp3.getTitle());
//                LogUtils.debug("getLrc: " + metaInfoParser_mp3.getLrc());

//                LogUtils.debug("getLrc: " + mp3ReadId3v2.getLrc());
//                LogUtils.debug("getAuthor: " + mp3ReadId3v2.getAuthor());
//                LogUtils.debug("getName: " + mp3ReadId3v2.getName());
//                LogUtils.debug("getSpecial: " + mp3ReadId3v2.getSpecial());
                MP3ReadID3v2 mp3ReadId3v2 = new MP3ReadID3v2(UriToPathUtil.getRealFile(mContext, mediaUri));
                byte[] img = mp3ReadId3v2.getImg();
                if (img != null && img.length != 0) {
                    mediaInfoBean.setMediaIconBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaInfoBean;
    }

}
