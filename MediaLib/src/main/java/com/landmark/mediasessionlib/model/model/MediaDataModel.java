package com.landmark.mediasessionlib.model.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.landmark.mediasessionlib.model.db.table.AlbumVo;
import com.landmark.mediasessionlib.model.db.table.FolderVo;
import com.landmark.mediasessionlib.model.db.table.GenreVo;
import com.landmark.mediasessionlib.model.db.table.SingerVo;

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

public class MediaDataModel implements Parcelable {
    private Long id;
    private String name;
    private String symbolName;
    private String path;
    private String size;
    private String duration;
    private String year;
    private boolean favFlag;
    private Long albumId;
    private AlbumVo albumVo;
    private Long folderId;
    private FolderVo folderVo;
    private Long singerId;
    private SingerVo singerVo;
    private String suffix;
    private String itemType;//数据类型
    private Long GenreId;
    private GenreVo genreVo; //流派


    protected MediaDataModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        symbolName = in.readString();
        path = in.readString();
        size = in.readString();
        duration = in.readString();
        year = in.readString();
        favFlag = in.readByte() != 0;
        if (in.readByte() == 0) {
            albumId = null;
        } else {
            albumId = in.readLong();
        }
        albumVo = in.readParcelable(AlbumVo.class.getClassLoader());
        if (in.readByte() == 0) {
            folderId = null;
        } else {
            folderId = in.readLong();
        }
        folderVo = in.readParcelable(FolderVo.class.getClassLoader());
        if (in.readByte() == 0) {
            singerId = null;
        } else {
            singerId = in.readLong();
        }
        singerVo = in.readParcelable(SingerVo.class.getClassLoader());
        suffix = in.readString();
        itemType = in.readString();
        if (in.readByte() == 0) {
            GenreId = null;
        } else {
            GenreId = in.readLong();
        }
        genreVo = in.readParcelable(GenreVo.class.getClassLoader());
    }

    public static final Creator<MediaDataModel> CREATOR = new Creator<MediaDataModel>() {
        @Override
        public MediaDataModel createFromParcel(Parcel in) {
            return new MediaDataModel(in);
        }

        @Override
        public MediaDataModel[] newArray(int size) {
            return new MediaDataModel[size];
        }
    };

    public Long getGenreId() {
        return GenreId;
    }

    public void setGenreId(Long genreId) {
        GenreId = genreId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public MediaDataModel() {
    }

    public GenreVo getGenreVo() {
        return genreVo;
    }

    public void setGenreVo(GenreVo genreVo) {
        this.genreVo = genreVo;
    }


    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean getFavFlag() {
        return favFlag;
    }

    public void setFavFlag(boolean favFlag) {
        this.favFlag = favFlag;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public AlbumVo getAlbumVo() {
        return albumVo;
    }

    public void setAlbumVo(AlbumVo albumVo) {
        this.albumVo = albumVo;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public FolderVo getFolderVo() {
        return folderVo;
    }

    public void setFolderVo(FolderVo folderVo) {
        this.folderVo = folderVo;
    }

    public Long getSingerId() {
        return singerId;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }

    public SingerVo getSingerVo() {
        return singerVo;
    }

    public void setSingerVo(SingerVo singerVo) {
        this.singerVo = singerVo;
    }


    @Override
    public String toString() {
        return "MediaDataModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbolName='" + symbolName + '\'' +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                ", duration='" + duration + '\'' +
                ", year='" + year + '\'' +
                ", favFlag='" + favFlag + '\'' +
                ", albumId=" + albumId +
                ", albumVo=" + albumVo +
                ", folderId=" + folderId +
                ", folderVo=" + folderVo +
                ", singerId=" + singerId +
                ", singerVo=" + singerVo +
                ", suffix='" + suffix + '\'' +
                ", itemType='" + itemType + '\'' +
                ", GenreId=" + GenreId +
                ", genreVo=" + genreVo +
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
        dest.writeString(path);
        dest.writeString(size);
        dest.writeString(duration);
        dest.writeString(year);
        dest.writeByte((byte) (favFlag ? 1 : 0));
        if (albumId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(albumId);
        }
        dest.writeParcelable(albumVo, flags);
        if (folderId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(folderId);
        }
        dest.writeParcelable(folderVo, flags);
        if (singerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(singerId);
        }
        dest.writeParcelable(singerVo, flags);
        dest.writeString(suffix);
        dest.writeString(itemType);
        if (GenreId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(GenreId);
        }
        dest.writeParcelable(genreVo, flags);
    }
}
