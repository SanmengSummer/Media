package com.landmark.mediasessionlib.model.db.table;

import com.landmark.mediasessionlib.model.db.dao.AlbumVoDao;
import com.landmark.mediasessionlib.model.db.dao.AudioVoDao;
import com.landmark.mediasessionlib.model.db.dao.DaoSession;
import com.landmark.mediasessionlib.model.db.dao.FolderVoDao;
import com.landmark.mediasessionlib.model.db.dao.GenreVoDao;
import com.landmark.mediasessionlib.model.db.dao.SingerVoDao;

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
@Entity(nameInDb = "table_audio")
public class AudioVo {
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String symbolName;

    private String path;
    private String size;
    private String duration;
    private String year;
    private boolean favFlag;

    private Long albumId; //该authId就是Author的主键id，注意类型一定要相同，这里都为long类型
    @ToOne(joinProperty = "albumId") // 注意该参数的值
    private AlbumVo albumVo;

    private Long folderId; //该authId就是Author的主键id，注意类型一定要相同，这里都为long类型
    @ToOne(joinProperty = "folderId") // 注意该参数的值
    private FolderVo folderVo;

    private Long singerId; //该authId就是Author的主键id，注意类型一定要相同，这里都为long类型
    @ToOne(joinProperty = "singerId") // 注意该参数的值
    private SingerVo singerVo;

    private Long GenreId;
    @ToOne(joinProperty = "GenreId")
    private GenreVo genreVo;


    private String suffix;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 44806035)
    private transient AudioVoDao myDao;

    @Generated(hash = 1648908478)
    public AudioVo(Long id, String name, String symbolName, String path,
            String size, String duration, String year, boolean favFlag,
            Long albumId, Long folderId, Long singerId, Long GenreId,
            String suffix) {
        this.id = id;
        this.name = name;
        this.symbolName = symbolName;
        this.path = path;
        this.size = size;
        this.duration = duration;
        this.year = year;
        this.favFlag = favFlag;
        this.albumId = albumId;
        this.folderId = folderId;
        this.singerId = singerId;
        this.GenreId = GenreId;
        this.suffix = suffix;
    }

    @Generated(hash = 1141950085)
    public AudioVo() {
    }

    @Generated(hash = 1779049616)
    private transient Long albumVo__resolvedKey;

    @Generated(hash = 6141150)
    private transient Long folderVo__resolvedKey;

    @Generated(hash = 1079347519)
    private transient Long singerVo__resolvedKey;

    @Generated(hash = 1791560788)
    private transient Long genreVo__resolvedKey;
   
    @Override
    public String toString() {
        return "AudioVo{" +
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
                ", GenreId=" + GenreId +
                ", genreVo=" + genreVo +
                ", suffix='" + suffix + '\'' +
                '}';
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

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean getFavFlag() {
        return this.favFlag;
    }

    public void setFavFlag(boolean favFlag) {
        this.favFlag = favFlag;
    }

    public Long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getSingerId() {
        return this.singerId;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }

    public Long getGenreId() {
        return this.GenreId;
    }

    public void setGenreId(Long GenreId) {
        this.GenreId = GenreId;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 590335922)
    public AlbumVo getAlbumVo() {
        Long __key = this.albumId;
        if (albumVo__resolvedKey == null || !albumVo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AlbumVoDao targetDao = daoSession.getAlbumVoDao();
            AlbumVo albumVoNew = targetDao.load(__key);
            synchronized (this) {
                albumVo = albumVoNew;
                albumVo__resolvedKey = __key;
            }
        }
        return albumVo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 767304466)
    public void setAlbumVo(AlbumVo albumVo) {
        synchronized (this) {
            this.albumVo = albumVo;
            albumId = albumVo == null ? null : albumVo.getId();
            albumVo__resolvedKey = albumId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2110994881)
    public FolderVo getFolderVo() {
        Long __key = this.folderId;
        if (folderVo__resolvedKey == null || !folderVo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FolderVoDao targetDao = daoSession.getFolderVoDao();
            FolderVo folderVoNew = targetDao.load(__key);
            synchronized (this) {
                folderVo = folderVoNew;
                folderVo__resolvedKey = __key;
            }
        }
        return folderVo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1009014164)
    public void setFolderVo(FolderVo folderVo) {
        synchronized (this) {
            this.folderVo = folderVo;
            folderId = folderVo == null ? null : folderVo.getId();
            folderVo__resolvedKey = folderId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1495377176)
    public SingerVo getSingerVo() {
        Long __key = this.singerId;
        if (singerVo__resolvedKey == null || !singerVo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SingerVoDao targetDao = daoSession.getSingerVoDao();
            SingerVo singerVoNew = targetDao.load(__key);
            synchronized (this) {
                singerVo = singerVoNew;
                singerVo__resolvedKey = __key;
            }
        }
        return singerVo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1216965967)
    public void setSingerVo(SingerVo singerVo) {
        synchronized (this) {
            this.singerVo = singerVo;
            singerId = singerVo == null ? null : singerVo.getId();
            singerVo__resolvedKey = singerId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2019356862)
    public GenreVo getGenreVo() {
        Long __key = this.GenreId;
        if (genreVo__resolvedKey == null || !genreVo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GenreVoDao targetDao = daoSession.getGenreVoDao();
            GenreVo genreVoNew = targetDao.load(__key);
            synchronized (this) {
                genreVo = genreVoNew;
                genreVo__resolvedKey = __key;
            }
        }
        return genreVo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 193074223)
    public void setGenreVo(GenreVo genreVo) {
        synchronized (this) {
            this.genreVo = genreVo;
            GenreId = genreVo == null ? null : genreVo.getId();
            genreVo__resolvedKey = GenreId;
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
    @Generated(hash = 1383738474)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAudioVoDao() : null;
    }
}
