package com.landmark.mediasessionlib.model.db.data;

import android.annotation.SuppressLint;
import android.content.Context;

import com.landmark.mediasessionlib.model.interfaces.IDataProvider;
import com.landmark.mediasessionlib.model.model.MediaData;
import com.landmark.mediasessionlib.model.model.MediaDataModel;
import com.landmark.mediasessionlib.model.model.TypeModel;
import com.landmark.mediasessionlib.model.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**********************************************
 * Filename：
 * Author:   qiang.chen@landmark-phb.com
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.xx  2021/8/9 10  chenqiang   1) …
 ***********************************************/
@SuppressLint("NewApi")
public class MediaDataHelper implements IDataProvider {

    private static final String TAG = "MediaDataHelper";
    private static MediaDataHelper sInstance;
    private final MusicProvider mMusicProvider;
    private int mPager;
    private final int mPagerSize = 30;

    /**
     * devices connect status
     */
    private boolean isDevicesStatus = true;

    /**
     * get EngineeringModeManager.
     *
     * @return EngineeringModeManager
     */
    public static synchronized MediaDataHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MediaDataHelper(context);
        }
        return sInstance;
    }

    public MediaDataHelper(Context context) {
        mMusicProvider = MusicProvider.getInstance(context);
        loadDatabase();
        registerListener();
    }

    /**
     * register device listener
     */
    private void registerListener() {
        //todo registerListener
    }

    /**
     * unregister device listener
     */
    private void unregisterListener() {
        //todo unregisterListener
    }

    /**
     * load local database
     */
    private void loadDatabase() {
        if (mMusicProvider != null) {
            mMusicProvider.load();
        }
    }

    /**
     * @param page current page
     * @param size Current page size
     * @param type {@link MediaIDHelper}
     *             for example:
     *             __ROOT__/music All
     *             <p>
     *             __BY_ARTIST__/music All Singer
     *             __BY_ARTIST__/music/SingerID  All the songs under a certain singer
     *             __BY_ARTIST__/music/SingerID|songId  One of the songs of a particular singer
     *             <p>
     *             __BY_ALBUM__/music   All Album
     *             __BY_ALBUM__/music/AlbumID  All the songs on a particular album
     *             __BY_ALBUM__/music/AlbumID|songId  A song from one of those albums
     *             <p>
     *             __BY_GENRE__/music  All Gener
     *             __BY_GENRE__/music/generID   All the songs in a genre
     *             __BY_GENRE__/music/generID|songId   One of the songs in a genre
     *             <p>
     *             __BY_FOLDER__/music  All folder
     *             __BY_FOLDER__/music/folderID   All the songs in one folder
     *             __BY_FOLDER__/music/folderID|songId  One of the songs in a folder
     * @return Song information list
     */
    @Override
    public MediaData getMusicDataList(int page, int size, String type) {
        LogUtils.debug(TAG, " getPlayingList type: " + type);
        MediaData mediaData = new MediaData();
        if (!isDevicesStatus) {
            LogUtils.debug(TAG, " getMusicDataList isDevicesStatus: " + isDevicesStatus);
            return mediaData;
        }
        int[] ints = verifyValue(page, size);
        page = ints[0];
        size = ints[1];
        TypeModel musicDataListType = MediaIDHelper.getMusicDataListType(type);
        String category;
        if (musicDataListType.getCategory_name() == null) {
            category = musicDataListType.getCategory();//查这个所以的类型
        } else {
            if (musicDataListType.getMediaId() == null) {
                category = musicDataListType.getCategory_name(); //所以的类型中的一种
            } else {
                category = musicDataListType.getMediaId();//具体的歌曲id
            }
        }
        musicDataListType.setCategory_name(category);
        musicDataListType.setPage(page);
        musicDataListType.setSize(size);
        LogUtils.debug(TAG, "getPlayingList page: " + page + " size: " + size + "  category :"
                + category);
        if (mMusicProvider == null) {
            return mediaData;
        }
        List<MediaDataModel> mediaDataModels = new ArrayList<>();
        LogUtils.debug(TAG, "getCategory_name " + musicDataListType.getCategory());
        switch (musicDataListType.getCategory()) {
            case MediaIDHelper.MEDIA_ID_ROOT:
                mediaDataModels.addAll(mMusicProvider.getAllDataList(musicDataListType, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM:
                mediaDataModels.addAll(mMusicProvider.getAlbumDataList(musicDataListType, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_ARTIST:
                mediaDataModels.addAll(mMusicProvider.getArtistDataList(musicDataListType, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE:
                mediaDataModels.addAll(mMusicProvider.getGenreDataList(musicDataListType, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_FOLDER:
                mediaDataModels.addAll(mMusicProvider.getFolderDataList(musicDataListType, mediaData));
                break;
            default:
                break;
        }
        mediaData.setData(mediaDataModels);
        mediaData.setCurrentPage(page);
        mediaData.setPageSize(size);
        return mediaData;
    }

    /**
     * Fuzzy search for all categories by type, and then call
     * {@link #getSearchList(int, int, String) } to query specific song information
     *
     * @param page
     * @param size
     * @param type type {@link MediaIDHelper}
     *             for example:
     *             __BY_SEARCH__/__ROOT__/music/名字
     *             <p>
     *             <p>
     *             __BY_SEARCH__/__BY_ARTIST__/music/歌手名称
     *             __BY_SEARCH__/__BY_ARTIST__/music/歌手名称|歌曲id
     *             <p>
     *             __BY_SEARCH__/__BY_ALBUM__/music/专辑名称
     *             __BY_SEARCH__/__BY_ALBUM__/music/专辑名称|歌曲id
     *             <p>
     *             __BY_SEARCH__/__BY_GENRE__/music/流派类型
     *             __BY_SEARCH__/__BY_GENRE__/music/流派类型|歌曲id
     *             <p>
     *             __BY_SEARCH__/__BY_FOLDER__/music/文件夹名称
     *             __BY_SEARCH__/__BY_FOLDER__/music/文件夹名称|歌曲id
     *             <p>
     *             __BY_SEARCH__/__BY_TITLE__/music/歌曲名称
     *             __BY_SEARCH__/__BY_TITLE__/music/歌曲名称|歌曲id
     * @return
     */
    @Override
    public MediaData getSearch(int page, int size, String type) {
        MediaData mediaData = new MediaData();
        if (!isDevicesStatus) {
            LogUtils.debug(TAG, " getSearch isDevicesStatus: " + isDevicesStatus);
            return mediaData;
        }
        int[] ints = verifyValue(page, size);
        page = ints[0];
        size = ints[1];
        TypeModel searchDataType = MediaIDHelper.getSearchData(type);
        searchDataType.setSize(size);
        searchDataType.setPage(page);

        List<MediaDataModel> mediaDataModels = new ArrayList<>();
        switch (searchDataType.getCategory()) {
            case MediaIDHelper.MEDIA_ID_ROOT:
                mediaDataModels.addAll(mMusicProvider.getSearchAll(searchDataType, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM:
                mediaDataModels.addAll(mMusicProvider.getSearchAlbum(searchDataType, false, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_ARTIST:
                mediaDataModels.addAll(mMusicProvider.getSearchArtist(searchDataType, false, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE:
                mediaDataModels.addAll(mMusicProvider.getSearchGenre(searchDataType, false, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_FOLDER:
                mediaDataModels.addAll(mMusicProvider.getSearchFolder(searchDataType, false, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_TITLE:
                mediaDataModels.addAll(mMusicProvider.getSearchTitle(searchDataType, false, mediaData));
                break;
            default:
                break;
        }
        mediaData.setData(mediaDataModels);
        mediaData.setCurrentPage(page);
        mediaData.setPageSize(size);
        return mediaData;
    }

    @Override
    public MediaData getSearchList(int page, int size, String type) {
        MediaData mediaData = new MediaData();
        if (!isDevicesStatus) {
            LogUtils.debug(TAG, " getSearchList isDevicesStatus: " + isDevicesStatus);
            return mediaData;
        }
        int[] ints = verifyValue(page, size);
        page = ints[0];
        size = ints[1];
        TypeModel searchDataType = MediaIDHelper.getSearchData(type);
        searchDataType.setSize(size);
        searchDataType.setPage(page);
        List<MediaDataModel> mediaDataModels = new ArrayList<>();
        switch (searchDataType.getCategory()) {
            case MediaIDHelper.MEDIA_ID_ROOT:
                mediaDataModels.addAll(mMusicProvider.getSearchListAll(searchDataType, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM:
                mediaDataModels.addAll(mMusicProvider.getSearchAlbum(searchDataType, true, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_ARTIST:
                mediaDataModels.addAll(mMusicProvider.getSearchArtist(searchDataType, true, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE:
                mediaDataModels.addAll(mMusicProvider.getSearchGenre(searchDataType, true, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_FOLDER:
                mediaDataModels.addAll(mMusicProvider.getSearchFolder(searchDataType, true, mediaData));
                break;
            case MediaIDHelper.MEDIA_ID_MUSICS_BY_TITLE:
                mediaDataModels.addAll(mMusicProvider.getSearchTitle(searchDataType, true, mediaData));
                break;
        }
        mediaData.setData(mediaDataModels);
        mediaData.setCurrentPage(page);
        mediaData.setPageSize(size);
        return mediaData;
    }

    @Override
    public boolean addCollectList(String mediaId) {
        boolean addStatus = false;
        if (mMusicProvider != null && mediaId != null) {
            addStatus = mMusicProvider.addCollectList(mediaId);
        }
        return addStatus;
    }

    @Override
    public boolean cancelCollectList(String mediaId) {
        boolean addStatus = false;
        if (mMusicProvider != null && mediaId != null) {
            addStatus = mMusicProvider.cancelCollectList(mediaId);
        }
        return addStatus;
    }

    @Override
    public MediaData getCollectList(int page, int size) {
        int[] ints = verifyValue(page, size);
        page = ints[0];
        size = ints[1];
        LogUtils.debug(TAG, "getCollectList page: " + page + " size: " + size);
        MediaData mediaData = new MediaData();
        List<MediaDataModel> mediaDataModels = new ArrayList<>();
        if (mMusicProvider != null) {
            mediaDataModels = mMusicProvider.getCollectList(page, size,mediaData);
        }
        mediaData.setData(mediaDataModels);
        mediaData.setCurrentPage(page);
        mediaData.setPageSize(size);
        return mediaData;
    }

    @Override
    public boolean clearCollectList() {
        boolean status = false;
        if (mMusicProvider != null) {
            status = mMusicProvider.clearCollectList();
        }
        return status;
    }

    @Override
    public boolean addHistoryList(String mediaId, long currentTime) {
        boolean status = false;
        if (mMusicProvider != null && mediaId != null) {
            status = mMusicProvider.addHistoryList(mediaId, 0);
        }
        return status;
    }

    @Override
    public MediaData getHistoryList(int page, int size) {
        int[] ints = verifyValue(page, size);
        page = ints[0];
        size = ints[1];
        LogUtils.debug(TAG, "getHistoryList page: " + page + " size: " + size);
        MediaData mediaData = new MediaData();
        List<MediaDataModel> mediaDataModels = new ArrayList<>();
        if (mMusicProvider != null) {
            mediaDataModels = mMusicProvider.getHistoryList(page, size);
        }
        mediaData.setData(mediaDataModels);
        mediaData.setCurrentPage(page);
        mediaData.setPageSize(size);
        return mediaData;
    }

    @Override
    public MediaData getHistory(String media) {
        MediaData mediaData = new MediaData();
        if (mMusicProvider != null) {
            List<MediaDataModel> mediaDataModels = new ArrayList<>();
            mediaDataModels.add(mMusicProvider.getHistory(media));
            mediaData.setData(mediaDataModels);
        }
        return mediaData;
    }

    @Override
    public boolean clearHistoryList() {
        boolean status = false;
        if (mMusicProvider != null) {
            status = mMusicProvider.clearHistoryList();
        }
        return status;
    }

    /**
     * Verify page and size
     *
     * @param page current page
     * @param size current page size
     * @return page size
     */
    public int[] verifyValue(int page, int size) {
        LogUtils.debug(TAG, " verifyValue page: " + page + " size: " + size);
       /* if (page == 0 && mPager != 0) {
            page = mPager + 1;
        } else {
            mPager = page;
        }*/
        if (size == 0) {
            size = mPagerSize;
        }
        LogUtils.debug(TAG, " verifyValue page: " + page + " size: " + size);
        return new int[]{
                page, size
        };
    }
}
