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
@Entity(nameInDb = "table_album")
public class AlbumVo implements Parcelable {
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String symbolName;

    @Generated(hash = 89140298)
    public AlbumVo(Long id, String name, String symbolName) {
        this.id = id;
        this.name = name;
        this.symbolName = symbolName;
    }

    @Generated(hash = 1343948380)
    public AlbumVo() {
    }

    protected AlbumVo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        symbolName = in.readString();
    }

    public static final Creator<AlbumVo> CREATOR = new Creator<AlbumVo>() {
        @Override
        public AlbumVo createFromParcel(Parcel in) {
            return new AlbumVo(in);
        }

        @Override
        public AlbumVo[] newArray(int size) {
            return new AlbumVo[size];
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

    @Override
    public String toString() {
        return "AlbumVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbolName='" + symbolName + '\'' +
                '}';
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
    }
}
