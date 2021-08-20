package com.landmark.mediasessionlib.model.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**********************************************
 * Filename： FileVo
 * Author:   wangyi@zlingsmart.com.cn
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.0.1        wangyi   1) …
 ***********************************************/
@Entity(nameInDb = "table_video")
public class VideoVo implements Parcelable {
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String symbolName;


    private String path;
    private String width;
    private String height;
    private String size;
    private String duration;
    private String des;
    private String favFlag;

    private Long folderId;

    private String suffix;

    @Generated(hash = 1458415207)
    public VideoVo(Long id, String name, String symbolName, String path,
                   String width, String height, String size, String duration, String des,
                   String favFlag, Long folderId, String suffix) {
        this.id = id;
        this.name = name;
        this.symbolName = symbolName;
        this.path = path;
        this.width = width;
        this.height = height;
        this.size = size;
        this.duration = duration;
        this.des = des;
        this.favFlag = favFlag;
        this.folderId = folderId;
        this.suffix = suffix;
    }

    @Generated(hash = 724707836)
    public VideoVo() {
    }

    protected VideoVo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        symbolName = in.readString();
        path = in.readString();
        width = in.readString();
        height = in.readString();
        size = in.readString();
        duration = in.readString();
        des = in.readString();
        favFlag = in.readString();
        if (in.readByte() == 0) {
            folderId = null;
        } else {
            folderId = in.readLong();
        }
        suffix = in.readString();
    }

    public static final Creator<VideoVo> CREATOR = new Creator<VideoVo>() {
        @Override
        public VideoVo createFromParcel(Parcel in) {
            return new VideoVo(in);
        }

        @Override
        public VideoVo[] newArray(int size) {
            return new VideoVo[size];
        }
    };

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbolName() {
        return this.symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFavFlag() {
        return this.favFlag;
    }

    public void setFavFlag(String favFlag) {
        this.favFlag = favFlag;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(symbolName);
        dest.writeString(path);
        dest.writeString(width);
        dest.writeString(height);
        dest.writeString(size);
        dest.writeString(duration);
        dest.writeString(des);
        dest.writeString(favFlag);
        if (folderId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(folderId);
        }
        dest.writeString(suffix);
    }
}
