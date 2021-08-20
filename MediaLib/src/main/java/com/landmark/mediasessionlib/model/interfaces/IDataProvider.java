package com.landmark.mediasessionlib.model.interfaces;


import com.landmark.mediasessionlib.model.model.MediaData;

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
public interface IDataProvider {

    MediaData getMusicDataList(int page, int size, String type);

    MediaData getSearch(int page, int size, String type);

    MediaData getSearchList(int page, int size, String type);

    boolean addCollectList(String mediaId);

    boolean cancelCollectList(String mediaId);

    MediaData getCollectList(int page, int size);

    boolean clearCollectList();

    boolean addHistoryList(String mediaId, long currentTime);

    MediaData getHistoryList(int page, int size);

    MediaData getHistory(String media);

    boolean clearHistoryList();


}
