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
package com.landmark.mediasessionlib.model.db.data;


import com.landmark.mediasessionlib.model.model.TypeModel;
import com.landmark.mediasessionlib.model.utils.LogUtils;

public class MediaIDHelper {
    private static final String TAG = "MediaIDHelper";
    public static final String TYPE_1 = "music";
    public static final String TYPE_2 = "video";
    public static final String MEDIA_ID_EMPTY_ROOT = "__EMPTY_ROOT__";
    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final String MEDIA_ID_MUSICS_BY_FOLDER = "__BY_FOLDER__";
    public static final String MEDIA_ID_MUSICS_BY_ARTIST = "__BY_ARTIST__";
    public static final String MEDIA_ID_MUSICS_BY_ALBUM = "__BY_ALBUM__";
    public static final String MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__";
    public static final String MEDIA_ID_MUSICS_BY_SEARCH = "__BY_SEARCH__";
    public static final String MEDIA_ID_MUSICS_BY_TITLE = "__BY_TITLE__";

    private static final String CATEGORY_SEPARATOR = "/";
    private static final String LEAF_SEPARATOR = "|";
    public static final String SEARCH_SEPARATOR = ":";
    private static int typeLength = 5;


    /**
     * @param type {@link #getType}
     * @return
     */
    public static TypeModel getMusicDataListType(String type) {
        TypeModel typeModel = new TypeModel();
        int index = type.indexOf(LEAF_SEPARATOR);
        if (index != -1) {//__BY_GENRE__/mp3/gener|songId
            String mediaId = type.substring(index + 1);
            typeModel.setMediaId(mediaId);
            type = type.substring(0, index);
        }
        String[] split = type.split(CATEGORY_SEPARATOR);
        if (split.length == 2) {
            typeModel.setCategory(split[0]);
            typeModel.setFormat(split[1]);
        } else {
            typeModel.setCategory(split[0]);
            typeModel.setFormat(split[1]);
            typeModel.setCategory_name(split[2]);
        }
        return typeModel;
    }

    /**
     * @param type __BY_SEARCH__/__ROOT__/mp3/名字
     *             __BY_SEARCH__/__ROOT__/music/itemType/第
     *             <p>
     *             __BY_SEARCH__/__BY_ALBUM__/mp3/专辑名称
     * @return TypeModel
     */
    public static TypeModel getSearchData(String type) {
        TypeModel typeModel = new TypeModel();
        int index = type.indexOf(LEAF_SEPARATOR);
        if (index != -1) {
            String mediaId = type.substring(index + 1);
            typeModel.setMediaId(mediaId);
            type = type.substring(0, index);
        }
        String[] split = type.split(CATEGORY_SEPARATOR);
        if (split.length == typeLength) {
            //__BY_SEARCH__/__ROOT__/music/itemType/xxx
            // 1 2 3 4 is index
            typeModel.setCategory(split[1]);
            typeModel.setFormat(split[2]);
            typeModel.setCategory_name(split[3] + SEARCH_SEPARATOR + split[4]);
        } else {
            typeModel.setCategory(split[1]);
            typeModel.setFormat(split[2]);
            typeModel.setCategory_name(split[3]);
        }
        return typeModel;
    }

    /**
     * @param format music/video
     * @return String
     */
    public static String getRootType(String format) {
        if (format == null) {
            format = TYPE_1;
        }
        String strType = MEDIA_ID_ROOT + CATEGORY_SEPARATOR + format;
        LogUtils.debug(TAG, " getRootType: " + strType);
        return strType;
    }

    /**
     * @param format   music/video
     * @param value    Specific data content
     * @param itemType data show type
     * @return String
     */
    public static String getSearchRootType(String format, String value, String itemType) {
        String strType;
        if (itemType == null) {
            strType = MEDIA_ID_MUSICS_BY_SEARCH + CATEGORY_SEPARATOR
                    + MEDIA_ID_ROOT + CATEGORY_SEPARATOR + format
                    + CATEGORY_SEPARATOR + value;
        } else {
            strType = MEDIA_ID_MUSICS_BY_SEARCH + CATEGORY_SEPARATOR
                    + MEDIA_ID_ROOT + CATEGORY_SEPARATOR + format
                    + CATEGORY_SEPARATOR + itemType + CATEGORY_SEPARATOR + value;
        }
        LogUtils.debug(TAG, " getSearchRootType: " + strType);
        return strType;
    }

    /**
     * @param category MEDIA_ID_MUSICS_BY_FOLDER/MEDIA_ID_MUSICS_BY_ARTIST
     *                 /MEDIA_ID_MUSICS_BY_ALBUM/MEDIA_ID_MUSICS_BY_GENRE
     *                 /MEDIA_ID_MUSICS_BY_TITLE
     * @param isSearch true is search type ,false is getdata type
     * @param type     length =1 is mediatype music/video
     *                 length =2 is mediatype and artistId/albumId/folderId/genreId
     * @return String
     */
    public static String getType(String category, boolean isSearch, String... type) {
        int length = type.length;
        String strType;
        if (isSearch) {
            strType = MEDIA_ID_MUSICS_BY_SEARCH +
                    CATEGORY_SEPARATOR + category +
                    CATEGORY_SEPARATOR + type[0] +
                    CATEGORY_SEPARATOR + type[1];
        } else {
            if (length > 1) {
                strType = category + CATEGORY_SEPARATOR + type[0] +
                        CATEGORY_SEPARATOR + type[1];
            } else {
                strType = category + CATEGORY_SEPARATOR + type[0];
            }
        }
        LogUtils.debug(TAG, " getGenreType: " + strType);
        return strType;
    }


}
