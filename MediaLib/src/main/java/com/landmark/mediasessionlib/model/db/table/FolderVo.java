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
@Entity(nameInDb = "table_folder")
public class FolderVo implements Parcelable{
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String symbolName;
    private String path;
    private String parentPath;
    private String parentId;

    protected FolderVo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        symbolName = in.readString();
        path = in.readString();
        parentPath = in.readString();
        parentId = in.readString();
    }

    @Generated(hash = 404621743)
    public FolderVo(Long id, String name, String symbolName, String path,
            String parentPath, String parentId) {
        this.id = id;
        this.name = name;
        this.symbolName = symbolName;
        this.path = path;
        this.parentPath = parentPath;
        this.parentId = parentId;
    }

    @Generated(hash = 243982118)
    public FolderVo() {
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
        dest.writeString(parentPath);
        dest.writeString(parentId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getParentPath() {
        return this.parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static final Creator<FolderVo> CREATOR = new Creator<FolderVo>() {
        @Override
        public FolderVo createFromParcel(Parcel in) {
            return new FolderVo(in);
        }

        @Override
        public FolderVo[] newArray(int size) {
            return new FolderVo[size];
        }
    };

    @Override
    public String toString() {
        return "FolderVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbolName='" + symbolName + '\'' +
                ", path='" + path + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
