package com.landmark.mediasessionlib.model.model;

import android.os.Parcel;
import android.os.Parcelable;

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
 * V0.xx  2021/8/12 11  chenqiang   1) …
 ***********************************************/

public class MediaData implements Parcelable {
    List<MediaDataModel> data;
    int totalPage;
    int currentPage;
    int pageSize;
    int totalNum;

    public MediaData() {
    }

    public MediaData(List<MediaDataModel> data, int totalPage, int currentPage, int pageSize, int totalNum) {
        this.data = data;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
    }

    protected MediaData(Parcel in) {
        data = in.createTypedArrayList(MediaDataModel.CREATOR);
        totalPage = in.readInt();
        currentPage = in.readInt();
        pageSize = in.readInt();
        totalNum = in.readInt();
    }

    public static final Creator<MediaData> CREATOR = new Creator<MediaData>() {
        @Override
        public MediaData createFromParcel(Parcel in) {
            return new MediaData(in);
        }

        @Override
        public MediaData[] newArray(int size) {
            return new MediaData[size];
        }
    };

    public List<MediaDataModel> getData() {
        return data;
    }

    public void setData(List<MediaDataModel> data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeInt(totalPage);
        dest.writeInt(currentPage);
        dest.writeInt(pageSize);
        dest.writeInt(totalNum);
    }

    @Override
    public String toString() {
        return "MediaData{" +
                "data=" + data +
                ", totalPage=" + totalPage +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalNum=" + totalNum +
                '}';
    }
}
