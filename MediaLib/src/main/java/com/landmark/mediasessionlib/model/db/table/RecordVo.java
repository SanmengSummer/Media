package com.landmark.mediasessionlib.model.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.landmark.mediasessionlib.model.db.dao.AudioVoDao;
import com.landmark.mediasessionlib.model.db.dao.DaoSession;
import com.landmark.mediasessionlib.model.db.dao.RecordVoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

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
@Entity(nameInDb = "table_record")
public class RecordVo implements Parcelable {
    @Id(autoincrement = true)
    private Long id;

    private Long mediaId;
    @ToOne(joinProperty = "mediaId") // 注意该参数的值
    private AudioVo audioVo;


    private String endDuration;

    private String playTime;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1524652921)
    private transient RecordVoDao myDao;

    @Generated(hash = 1651765179)
    private transient Long audioVo__resolvedKey;

    protected RecordVo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            mediaId = null;
        } else {
            mediaId = in.readLong();
        }
        endDuration = in.readString();
        playTime = in.readString();
    }

    public static final Creator<RecordVo> CREATOR = new Creator<RecordVo>() {
        @Override
        public RecordVo createFromParcel(Parcel in) {
            return new RecordVo(in);
        }

        @Override
        public RecordVo[] newArray(int size) {
            return new RecordVo[size];
        }
    };

    @Override
    public String toString() {
        return "RecordVo{" +
                "id=" + id +
                ", mediaId=" + mediaId +
                ", endDuration='" + endDuration + '\'' +
                '}';
    }

    @Generated(hash = 694501553)
    public RecordVo(Long id, Long mediaId, String endDuration, String playTime) {
        this.id = id;
        this.mediaId = mediaId;
        this.endDuration = endDuration;
        this.playTime = playTime;
    }

    @Generated(hash = 75010410)
    public RecordVo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getEndDuration() {
        return this.endDuration;
    }

    public void setEndDuration(String endDuration) {
        this.endDuration = endDuration;
    }

    public String getPlayTime() {
        return this.playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 679260966)
    public AudioVo getAudioVo() {
        Long __key = this.mediaId;
        if (audioVo__resolvedKey == null || !audioVo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AudioVoDao targetDao = daoSession.getAudioVoDao();
            AudioVo audioVoNew = targetDao.load(__key);
            synchronized (this) {
                audioVo = audioVoNew;
                audioVo__resolvedKey = __key;
            }
        }
        return audioVo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 430356451)
    public void setAudioVo(AudioVo audioVo) {
        synchronized (this) {
            this.audioVo = audioVo;
            mediaId = audioVo == null ? null : audioVo.getId();
            audioVo__resolvedKey = mediaId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 82398987)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordVoDao() : null;
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
        if (mediaId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mediaId);
        }
        dest.writeString(endDuration);
        dest.writeString(playTime);
    }
}
