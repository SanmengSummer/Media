package com.landmark.mediasessionlib.model.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.landmark.mediasessionlib.model.db.table.AlbumVo;
import com.landmark.mediasessionlib.model.db.table.AudioVo;
import com.landmark.mediasessionlib.model.db.table.FolderVo;
import com.landmark.mediasessionlib.model.db.table.GenreVo;
import com.landmark.mediasessionlib.model.db.table.SingerVo;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.SqlUtils;

import java.util.ArrayList;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "table_audio".
*/
public class AudioVoDao extends AbstractDao<AudioVo, Long> {

    public static final String TABLENAME = "table_audio";

    /**
     * Properties of entity AudioVo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property SymbolName = new Property(2, String.class, "symbolName", false, "SYMBOL_NAME");
        public final static Property Path = new Property(3, String.class, "path", false, "PATH");
        public final static Property Size = new Property(4, String.class, "size", false, "SIZE");
        public final static Property Duration = new Property(5, String.class, "duration", false, "DURATION");
        public final static Property Year = new Property(6, String.class, "year", false, "YEAR");
        public final static Property FavFlag = new Property(7, boolean.class, "favFlag", false, "FAV_FLAG");
        public final static Property AlbumId = new Property(8, Long.class, "albumId", false, "ALBUM_ID");
        public final static Property FolderId = new Property(9, Long.class, "folderId", false, "FOLDER_ID");
        public final static Property SingerId = new Property(10, Long.class, "singerId", false, "SINGER_ID");
        public final static Property GenreId = new Property(11, Long.class, "GenreId", false, "GENRE_ID");
        public final static Property Suffix = new Property(12, String.class, "suffix", false, "SUFFIX");
    }

    private DaoSession daoSession;


    public AudioVoDao(DaoConfig config) {
        super(config);
    }
    
    public AudioVoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"table_audio\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"SYMBOL_NAME\" TEXT," + // 2: symbolName
                "\"PATH\" TEXT," + // 3: path
                "\"SIZE\" TEXT," + // 4: size
                "\"DURATION\" TEXT," + // 5: duration
                "\"YEAR\" TEXT," + // 6: year
                "\"FAV_FLAG\" INTEGER NOT NULL ," + // 7: favFlag
                "\"ALBUM_ID\" INTEGER," + // 8: albumId
                "\"FOLDER_ID\" INTEGER," + // 9: folderId
                "\"SINGER_ID\" INTEGER," + // 10: singerId
                "\"GENRE_ID\" INTEGER," + // 11: GenreId
                "\"SUFFIX\" TEXT);"); // 12: suffix
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"table_audio\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AudioVo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String symbolName = entity.getSymbolName();
        if (symbolName != null) {
            stmt.bindString(3, symbolName);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(4, path);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(5, size);
        }
 
        String duration = entity.getDuration();
        if (duration != null) {
            stmt.bindString(6, duration);
        }
 
        String year = entity.getYear();
        if (year != null) {
            stmt.bindString(7, year);
        }
        stmt.bindLong(8, entity.getFavFlag() ? 1L: 0L);
 
        Long albumId = entity.getAlbumId();
        if (albumId != null) {
            stmt.bindLong(9, albumId);
        }
 
        Long folderId = entity.getFolderId();
        if (folderId != null) {
            stmt.bindLong(10, folderId);
        }
 
        Long singerId = entity.getSingerId();
        if (singerId != null) {
            stmt.bindLong(11, singerId);
        }
 
        Long GenreId = entity.getGenreId();
        if (GenreId != null) {
            stmt.bindLong(12, GenreId);
        }
 
        String suffix = entity.getSuffix();
        if (suffix != null) {
            stmt.bindString(13, suffix);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AudioVo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String symbolName = entity.getSymbolName();
        if (symbolName != null) {
            stmt.bindString(3, symbolName);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(4, path);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(5, size);
        }
 
        String duration = entity.getDuration();
        if (duration != null) {
            stmt.bindString(6, duration);
        }
 
        String year = entity.getYear();
        if (year != null) {
            stmt.bindString(7, year);
        }
        stmt.bindLong(8, entity.getFavFlag() ? 1L: 0L);
 
        Long albumId = entity.getAlbumId();
        if (albumId != null) {
            stmt.bindLong(9, albumId);
        }
 
        Long folderId = entity.getFolderId();
        if (folderId != null) {
            stmt.bindLong(10, folderId);
        }
 
        Long singerId = entity.getSingerId();
        if (singerId != null) {
            stmt.bindLong(11, singerId);
        }
 
        Long GenreId = entity.getGenreId();
        if (GenreId != null) {
            stmt.bindLong(12, GenreId);
        }
 
        String suffix = entity.getSuffix();
        if (suffix != null) {
            stmt.bindString(13, suffix);
        }
    }

    @Override
    protected final void attachEntity(AudioVo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AudioVo readEntity(Cursor cursor, int offset) {
        AudioVo entity = new AudioVo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // symbolName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // path
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // size
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // duration
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // year
            cursor.getShort(offset + 7) != 0, // favFlag
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // albumId
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // folderId
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // singerId
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // GenreId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // suffix
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AudioVo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSymbolName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSize(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDuration(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setYear(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFavFlag(cursor.getShort(offset + 7) != 0);
        entity.setAlbumId(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setFolderId(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setSingerId(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setGenreId(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setSuffix(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AudioVo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AudioVo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AudioVo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getAlbumVoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getFolderVoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getSingerVoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getGenreVoDao().getAllColumns());
            builder.append(" FROM table_audio T");
            builder.append(" LEFT JOIN table_album T0 ON T.\"ALBUM_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN table_folder T1 ON T.\"FOLDER_ID\"=T1.\"_id\"");
            builder.append(" LEFT JOIN table_singer T2 ON T.\"SINGER_ID\"=T2.\"_id\"");
            builder.append(" LEFT JOIN table_genre T3 ON T.\"GENRE_ID\"=T3.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected AudioVo loadCurrentDeep(Cursor cursor, boolean lock) {
        AudioVo entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        AlbumVo albumVo = loadCurrentOther(daoSession.getAlbumVoDao(), cursor, offset);
        entity.setAlbumVo(albumVo);
        offset += daoSession.getAlbumVoDao().getAllColumns().length;

        FolderVo folderVo = loadCurrentOther(daoSession.getFolderVoDao(), cursor, offset);
        entity.setFolderVo(folderVo);
        offset += daoSession.getFolderVoDao().getAllColumns().length;

        SingerVo singerVo = loadCurrentOther(daoSession.getSingerVoDao(), cursor, offset);
        entity.setSingerVo(singerVo);
        offset += daoSession.getSingerVoDao().getAllColumns().length;

        GenreVo genreVo = loadCurrentOther(daoSession.getGenreVoDao(), cursor, offset);
        entity.setGenreVo(genreVo);

        return entity;    
    }

    public AudioVo loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<AudioVo> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<AudioVo> list = new ArrayList<AudioVo>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<AudioVo> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<AudioVo> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}