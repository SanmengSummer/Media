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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.landmark.mediasessionlib.model.common.MetadataTypeValue;
import com.landmark.mediasessionlib.model.db.DaoManager;
import com.landmark.mediasessionlib.model.db.DbOperationHelper;
import com.landmark.mediasessionlib.model.db.dao.AlbumVoDao;
import com.landmark.mediasessionlib.model.db.dao.AudioVoDao;
import com.landmark.mediasessionlib.model.db.dao.DaoSession;
import com.landmark.mediasessionlib.model.db.dao.FolderVoDao;
import com.landmark.mediasessionlib.model.db.dao.GenreVoDao;
import com.landmark.mediasessionlib.model.db.dao.SingerVoDao;
import com.landmark.mediasessionlib.model.db.table.AlbumVo;
import com.landmark.mediasessionlib.model.db.table.AudioVo;
import com.landmark.mediasessionlib.model.db.table.FolderVo;
import com.landmark.mediasessionlib.model.db.table.GenreVo;
import com.landmark.mediasessionlib.model.db.table.RecordVo;
import com.landmark.mediasessionlib.model.db.table.SingerVo;
import com.landmark.mediasessionlib.model.db.table.VideoVo;
import com.landmark.mediasessionlib.model.model.MediaData;
import com.landmark.mediasessionlib.model.model.MediaDataModel;
import com.landmark.mediasessionlib.model.model.TypeModel;
import com.landmark.mediasessionlib.model.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
@RequiresApi(api = Build.VERSION_CODES.N)
public class MusicProvider {
    private static MusicProvider sInstance;
    private final String TAG = "MusicProvider";
    private SQLiteDatabase mSqLiteDatabase;
    private Context mContext;
    private DaoSession mDaoSession;
    private DbOperationHelper<AlbumVo> mAlbumHelper;
    private DbOperationHelper<AudioVo> mAudioHelper;
    private DbOperationHelper<FolderVo> mFolderHelper;
    private DbOperationHelper<RecordVo> mRecordHelper;
    private DbOperationHelper<SingerVo> mSingerHelper;
    private DbOperationHelper<VideoVo> mVideoHelper;
    private DbOperationHelper<GenreVo> mGenreVoHelper;

    private List<MediaDataModel> mSearchAllData = new ArrayList<>();

    public static synchronized MusicProvider getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MusicProvider(context);
        }
        return sInstance;
    }

    public MusicProvider(Context context) {
        mContext = context;
    }

    /**
     * load database
     */
    public void load() {
        mDaoSession = DaoManager.getInstance().getDaoSession();
        mAlbumHelper = new DbOperationHelper<>(AlbumVo.class, mDaoSession.getAlbumVoDao());
        mAudioHelper = new DbOperationHelper<>(AudioVo.class, mDaoSession.getAudioVoDao());
        mFolderHelper = new DbOperationHelper<>(FolderVo.class, mDaoSession.getFolderVoDao());
        mRecordHelper = new DbOperationHelper<>(RecordVo.class, mDaoSession.getRecordVoDao());
        mSingerHelper = new DbOperationHelper<>(SingerVo.class, mDaoSession.getSingerVoDao());
        mVideoHelper = new DbOperationHelper<>(VideoVo.class, mDaoSession.getVideoVoDao());
        mGenreVoHelper = new DbOperationHelper<>(GenreVo.class, mDaoSession.getGenreVoDao());
    }

    public List<MediaDataModel> getAllDataList(TypeModel typeModel, MediaData mediaData) {
        LogUtils.debug(TAG, " getAllDataList :" + typeModel.getCategory());
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getAllDataList page: " + page + " size: " + size);
        int offset = page * size;
        List<AudioVo> limit = mAudioHelper.queryBuilder().offset(offset).limit(size).list();
        List<MediaDataModel> collect = convertData(limit);
        long count = mAudioHelper.queryBuilder().count();
        setTotalData(mediaData, size, count);
        return collect;
    }

    public List<MediaDataModel> getAlbumDataList(TypeModel typeModel, MediaData mediaData) {
        List<MediaDataModel> list = new ArrayList<>();
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getAllDataList page: " + page + " size: " + size);
        int offset = page * size;
        LogUtils.debug(TAG, " getAlbumDataList: " + typeModel.getCategory() + " mediaType: "
                + typeModel.getFormat());
        final MediaDataModel[] value = new MediaDataModel[1];
        if (typeModel.getCategory_name().equals(MediaIDHelper.MEDIA_ID_MUSICS_BY_ALBUM)) {
            LogUtils.debug(TAG, " getAlbumDataList All Album");
            List<AlbumVo> albumVos = mAlbumHelper.queryBuilder().offset(offset).limit(size).list();
            List<MediaDataModel> collect = albumVos.stream().map(new Function<AlbumVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(AlbumVo albumVo) {
                    value[0] = new MediaDataModel();
                    value[0].setAlbumVo(albumVo);
                    value[0].setAlbumId(albumVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_ALBUM.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            list.addAll(collect);
            long count = mAlbumHelper.queryBuilder().offset(offset).limit(size).count();
            setTotalData(mediaData, size, count);
        } else {
            LogUtils.debug(TAG, " getAlbumDataList All Album-> name");
            List<AudioVo> limit = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.AlbumId.eq(typeModel.getCategory_name())).list();
            List<MediaDataModel> collect = convertData(limit);
            list.addAll(collect);
            long count = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.AlbumId.eq(typeModel.getCategory_name())).count();
            setTotalData(mediaData, size, count);
        }
        return list;
    }

    private void setTotalData(MediaData mediaData, int size, long count) {
        if (count > 0) {
            long l = count / size;
            long l1 = count % size;
            if (l1 > 0) {
                l = l + 1;
            }
            mediaData.setTotalNum((int) count);
            mediaData.setTotalPage((int) l);
        }
    }

    public List<MediaDataModel> getArtistDataList(TypeModel typeModel, MediaData mediaData) {
        List<MediaDataModel> list = new ArrayList<>();
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getAllDataList page: " + page + " size: " + size);
        int offset = page * size;

        LogUtils.debug(TAG, " getArtistDataList: " + typeModel.getCategory() + " mediaType: "
                + typeModel.getFormat());
        final MediaDataModel[] value = new MediaDataModel[1];
        if (typeModel.getCategory_name().equals(MediaIDHelper.MEDIA_ID_MUSICS_BY_ARTIST)) {
            LogUtils.debug(TAG, " getArtistDataList All Artist");
            List<SingerVo> singerVos = mSingerHelper.queryBuilder().offset(offset).limit(size).list();
            List<MediaDataModel> collect = singerVos.stream().map(new Function<SingerVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(SingerVo singervo) {
                    value[0] = new MediaDataModel();
                    value[0].setSingerVo(singervo);
                    value[0].setSingerId(singervo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_ARTIST.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            list.addAll(collect);
            long count = mSingerHelper.queryBuilder().offset(offset).limit(size).count();
            setTotalData(mediaData, size, count);
        } else {
            LogUtils.debug(TAG, " getArtistDataList All Artist-> name");
            List<AudioVo> limit = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.SingerId.eq(typeModel.getCategory_name())).list();
            List<MediaDataModel> collect = convertData(limit);
            list.addAll(collect);
            long count = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.SingerId.eq(typeModel.getCategory_name())).count();
            setTotalData(mediaData, size, count);
        }
        return list;
    }

    private List<MediaDataModel> convertData(List<AudioVo> data) {
        MediaDataModel[] value = new MediaDataModel[1];
        return data.stream().map(albumVo -> {
            value[0] = new MediaDataModel();
            value[0].setId(albumVo.getId());
            value[0].setName(albumVo.getName());
            value[0].setPath(albumVo.getPath());
            value[0].setSize(albumVo.getSize());
            value[0].setYear(albumVo.getYear());
            value[0].setGenreId(albumVo.getGenreId());
            value[0].setGenreVo(albumVo.getGenreVo());
            value[0].setFavFlag(albumVo.getFavFlag());
            value[0].setAlbumId(albumVo.getAlbumId());
            value[0].setAlbumVo(albumVo.getAlbumVo());
            value[0].setFolderId(albumVo.getFolderId());
            value[0].setFolderVo(albumVo.getFolderVo());
            value[0].setSingerVo(albumVo.getSingerVo());
            value[0].setSingerId(albumVo.getSingerId());
            value[0].setItemType(MetadataTypeValue.TYPE_MUSIC.getType());
            return value[0];
        }).collect(Collectors.toList());
    }

    public List<MediaDataModel> getGenreDataList(TypeModel typeModel, MediaData mediaData) {
        List<MediaDataModel> list = new ArrayList<>();
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getGenreDataList page: " + page + " size: " + size);
        int offset = page * size;
        final MediaDataModel[] value = new MediaDataModel[1];
        if (typeModel.getCategory_name().equals(MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE)) {
            LogUtils.debug(TAG, " getGenreDataList All ");
            List<GenreVo> singerVos = mGenreVoHelper.queryBuilder().offset(offset).limit(size).list();
            List<MediaDataModel> collect = singerVos.stream().map(new Function<GenreVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(GenreVo singervo) {
                    value[0] = new MediaDataModel();
                    value[0].setGenreVo(singervo);
                    value[0].setGenreId(singervo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_GENRE.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            list.addAll(collect);
            long count = mGenreVoHelper.queryBuilder().offset(offset).limit(size).count();
            setTotalData(mediaData, size, count);
        } else {
            LogUtils.debug(TAG, " getGenreDataList All Album-> name");
            List<AudioVo> limit = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.GenreId.eq(typeModel.getCategory_name())).list();
            List<MediaDataModel> collect = convertData(limit);
            list.addAll(collect);
            long count = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.GenreId.eq(typeModel.getCategory_name())).count();
            setTotalData(mediaData, size, count);
        }
        return list;
    }

    public List<MediaDataModel> getFolderDataList(TypeModel typeModel, MediaData mediaData) {
        List<MediaDataModel> list = new ArrayList<>();
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        MediaDataModel[] value = new MediaDataModel[1];
        if (typeModel.getCategory_name().equals(MediaIDHelper.MEDIA_ID_MUSICS_BY_FOLDER)) {
            List<FolderVo> folderVo = mFolderHelper.queryBuilder().offset(offset).limit(size).list();
            List<MediaDataModel> collect = folderVo.stream().map(new Function<FolderVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(FolderVo folderVo) {
                    value[0] = new MediaDataModel();
                    value[0].setFolderVo(folderVo);
                    value[0].setFolderId(folderVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_FOLDER.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            list.addAll(collect);
            long count = mFolderHelper.queryBuilder().offset(offset).limit(size).count();
            setTotalData(mediaData, size, count);
        } else {
            LogUtils.debug(TAG, " getFolderDataList getCategory_name: " + typeModel.getCategory_name());
            List<AudioVo> data = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.FolderId.eq(typeModel.getCategory_name())).list();
            List<MediaDataModel> mediaDataModels = convertData(data);
            list.addAll(mediaDataModels);
            List<FolderVo> list1 = mFolderHelper.queryBuilder().offset(offset).limit(size).where(
                    FolderVoDao.Properties.ParentId.eq(data.get(0).getFolderVo().getId())).list();
            List<MediaDataModel> folderVo = list1.stream().map(new Function<FolderVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(FolderVo folderVo) {
                    value[0] = new MediaDataModel();
                    value[0].setFolderVo(folderVo);
                    value[0].setFolderId(folderVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_FOLDER.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            list.addAll(folderVo);
            long count = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.FolderId.eq(typeModel.getCategory_name())).count();
            if (count > 0) {
                long l = count / size;
                long l1 = count % size;
                if (l1 > 0) {
                    l = l + 1;
                }
                mediaData.setTotalNum((int) count + list1.size());
                mediaData.setTotalPage((int) l);
            }
        }
        return list;
    }

    public List<MediaDataModel> getSearchAll(TypeModel typeModel, MediaData mediaData) {
        LogUtils.debug(TAG, " getSearchAll mSearchAllData: " + mSearchAllData.size() + " name: " + typeModel.getCategory_name());
        if (mSearchAllData.size() == 0) {
            List<AudioVo> list1 = mAudioHelper.queryBuilder()
                    .where(AudioVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).list();
            List<MediaDataModel> mediaDataModels = convertData(list1);
            mSearchAllData.addAll(mediaDataModels);

            MediaDataModel[] value = new MediaDataModel[1];
            List<AlbumVo> list2 = mAlbumHelper.queryBuilder()
                    .where(AlbumVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).list();
            List<MediaDataModel> collect = list2.stream().map(new Function<AlbumVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(AlbumVo albumVo) {
                    value[0] = new MediaDataModel();
                    value[0].setAlbumVo(albumVo);
                    value[0].setAlbumId(albumVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_ALBUM.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            mSearchAllData.addAll(collect);

            List<GenreVo> list3 = mGenreVoHelper.queryBuilder()
                    .where(GenreVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).list();
            List<MediaDataModel> collect1 = list3.stream().map(new Function<GenreVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(GenreVo genreVo) {
                    value[0] = new MediaDataModel();
                    value[0].setGenreVo(genreVo);
                    value[0].setGenreId(genreVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_GENRE.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            mSearchAllData.addAll(collect1);
            List<SingerVo> list4 = mSingerHelper.queryBuilder()
                    .where(SingerVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).list();
            List<MediaDataModel> collect2 = list4.stream().map(new Function<SingerVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(SingerVo singerVo) {
                    value[0] = new MediaDataModel();
                    value[0].setSingerVo(singerVo);
                    value[0].setSingerId(singerVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_ARTIST.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());

            mSearchAllData.addAll(collect2);

            List<FolderVo> list5 = mFolderHelper.queryBuilder()
                    .where(FolderVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).list();
            List<MediaDataModel> collect3 = list5.stream().map(new Function<FolderVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(FolderVo foldervo) {
                    value[0] = new MediaDataModel();
                    value[0].setFolderVo(foldervo);
                    value[0].setFolderId(foldervo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_FOLDER.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());

            mSearchAllData.addAll(collect3);
        }
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        int offset = page * size;
        int count = mSearchAllData.size();
        LogUtils.debug(TAG, " getSearchAll page: " + page + " size: " + (offset + 1) + " count: " + count);
        setTotalData(mediaData, size, count);
        if (size > count || offset > count) {
            size = count;
        }
        //todo 数据排序
        List<MediaDataModel> mediaDataModels = mSearchAllData.subList(offset, size);
        return mediaDataModels;
    }

    public Collection<? extends MediaDataModel> getSearchAlbum(TypeModel typeModel,
                                                               boolean isList, MediaData mediaData) {
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        MediaDataModel[] value = new MediaDataModel[1];
        List<MediaDataModel> mSearchAlbumData = new ArrayList<>();
        String category_name = typeModel.getCategory_name();
        if (isList) {
            //根据查到的专辑 来查询具体专辑下的歌曲
            List<AudioVo> audioVos = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.AlbumId.eq(category_name)).list();
            mSearchAlbumData.addAll(convertData(audioVos));
            long count = mAudioHelper.queryBuilder().where(
                    AudioVoDao.Properties.AlbumId.eq(category_name)).count();
            setTotalData(mediaData, size, count);
        } else {
            //搜专辑名称的 模糊查找
            List<AlbumVo> list2 = mAlbumHelper.queryBuilder()
                    .where(AlbumVoDao.Properties.Name.like("%" + category_name + "%")).list();
            List<MediaDataModel> collect = list2.stream().map(new Function<AlbumVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(AlbumVo albumVo) {
                    value[0] = new MediaDataModel();
                    value[0].setAlbumVo(albumVo);
                    value[0].setAlbumId(albumVo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_ALBUM.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            mSearchAlbumData.addAll(collect);
            long count = mAlbumHelper.queryBuilder()
                    .where(AlbumVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).count();
            setTotalData(mediaData, size, count);
        }
        return mSearchAlbumData;
    }

    public Collection<? extends MediaDataModel> getSearchArtist(TypeModel typeModel,
                                                                boolean isList, MediaData mediaData) {
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        MediaDataModel[] value = new MediaDataModel[1];
        List<MediaDataModel> mSearchAlbumData = new ArrayList<>();
        String category_name = typeModel.getCategory_name();
        if (isList) {
            //根据查到的专辑 来查询具体专辑下的歌曲
            List<AudioVo> audioVos = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.SingerId.eq(category_name)).list();
            mSearchAlbumData.addAll(convertData(audioVos));
            long count = mAudioHelper.queryBuilder().where(
                    AudioVoDao.Properties.SingerId.eq(category_name)).count();
            setTotalData(mediaData, size, count);
        } else {
            //搜专辑名称的 模糊查找
            List<SingerVo> list2 = mSingerHelper.queryBuilder()
                    .where(SingerVoDao.Properties.Name.like("%" + category_name + "%")).list();
            List<MediaDataModel> collect = list2.stream().map(new Function<SingerVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(SingerVo singervo) {
                    value[0] = new MediaDataModel();
                    value[0].setSingerVo(singervo);
                    value[0].setSingerId(singervo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_ARTIST.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            mSearchAlbumData.addAll(collect);
            long count = mSingerHelper.queryBuilder()
                    .where(SingerVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).count();
            setTotalData(mediaData, size, count);
        }
        return mSearchAlbumData;
    }

    public Collection<? extends MediaDataModel> getSearchGenre(TypeModel typeModel,
                                                               boolean isList, MediaData mediaData) {
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        MediaDataModel[] value = new MediaDataModel[1];
        List<MediaDataModel> mSearchAlbumData = new ArrayList<>();
        String category_name = typeModel.getCategory_name();
        if (isList) {
            //根据查到的专辑 来查询具体专辑下的歌曲
            List<AudioVo> audioVos = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.GenreId.eq(category_name)).list();
            mSearchAlbumData.addAll(convertData(audioVos));
            long count = mAudioHelper.queryBuilder().where(
                    AudioVoDao.Properties.GenreId.eq(category_name)).count();
            setTotalData(mediaData, size, count);
        } else {
            //搜专辑名称的 模糊查找
            List<GenreVo> list2 = mGenreVoHelper.queryBuilder()
                    .where(GenreVoDao.Properties.Name.like("%" + category_name + "%")).list();
            List<MediaDataModel> collect = list2.stream().map(new Function<GenreVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(GenreVo genrevo) {
                    value[0] = new MediaDataModel();
                    value[0].setGenreVo(genrevo);
                    value[0].setGenreId(genrevo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_GENRE.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            mSearchAlbumData.addAll(collect);
            long count = mGenreVoHelper.queryBuilder()
                    .where(GenreVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).count();
            setTotalData(mediaData, size, count);
        }
        return mSearchAlbumData;
    }

    public Collection<? extends MediaDataModel> getSearchFolder(TypeModel typeModel,
                                                                boolean isList, MediaData mediaData) {
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        MediaDataModel[] value = new MediaDataModel[1];
        List<MediaDataModel> mSearchAlbumData = new ArrayList<>();
        String category_name = typeModel.getCategory_name();
        if (isList) {
            //根据查到的专辑 来查询具体专辑下的歌曲
            List<AudioVo> audioVos = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.FolderId.eq(category_name)).list();
            mSearchAlbumData.addAll(convertData(audioVos));
            long count = mAudioHelper.queryBuilder().where(
                    AudioVoDao.Properties.FolderId.eq(category_name)).count();
            setTotalData(mediaData, size, count);
        } else {
            //搜专辑名称的 模糊查找
            List<FolderVo> list2 = mFolderHelper.queryBuilder()
                    .where(FolderVoDao.Properties.Name.like("%" + category_name + "%")).list();
            List<MediaDataModel> collect = list2.stream().map(new Function<FolderVo, MediaDataModel>() {
                @Override
                public MediaDataModel apply(FolderVo foldervo) {
                    value[0] = new MediaDataModel();
                    value[0].setFolderVo(foldervo);
                    value[0].setFolderId(foldervo.getId());
                    value[0].setItemType(MetadataTypeValue.TYPE_FOLDER.getType());
                    return value[0];
                }
            }).collect(Collectors.toList());
            mSearchAlbumData.addAll(collect);
            long count = mFolderHelper.queryBuilder()
                    .where(GenreVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).count();
            setTotalData(mediaData, size, count);
        }
        return mSearchAlbumData;
    }

    public Collection<? extends MediaDataModel> getSearchTitle(TypeModel typeModel,
                                                               boolean isList, MediaData mediaData) {

        //根据查到的专辑 来查询具体专辑下的歌曲
        int page = typeModel.getPage();
        int size = typeModel.getSize();
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        List<MediaDataModel> mSearchAlbumData = new ArrayList<>();
        String category_name = typeModel.getCategory_name();
        if (isList) {
            List<AudioVo> audioVos = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.Id.eq(category_name)).list();
            mSearchAlbumData.addAll(convertData(audioVos));
            long count = mAudioHelper.queryBuilder().where(
                    AudioVoDao.Properties.Id.eq(category_name)).count();
            setTotalData(mediaData, size, count);
        } else {
            List<AudioVo> list1 = mAudioHelper.queryBuilder()
                    .where(AudioVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).list();
            List<MediaDataModel> mediaDataModels = convertData(list1);
            mSearchAlbumData.addAll(mediaDataModels);
            long count = mAudioHelper.queryBuilder()
                    .where(AudioVoDao.Properties.Name.like("%" + typeModel.getCategory_name() + "%")).count();
            setTotalData(mediaData, size, count);
        }
        return mSearchAlbumData;
    }

    public boolean addCollectList(String mediaId) {
        if (TextUtils.isEmpty(mediaId)) {
            LogUtils.debug(TAG, " addCollectList: mediaId is null");
            return false;
        }
        AudioVo data = mAudioHelper.queryBuilder().where(AudioVoDao.Properties.Id.eq(mediaId)).list().get(0);
        data.setFavFlag(true);
        boolean update = mAudioHelper.update(data);
        return update;
    }

    public boolean cancelCollectList(String mediaId) {
        if (TextUtils.isEmpty(mediaId)) {
            LogUtils.debug(TAG, " addCollectList: mediaId is null");
            return false;
        }
        AudioVo data = mAudioHelper.queryBuilder().where(AudioVoDao.Properties.Id.eq(mediaId)).list().get(0);
        data.setFavFlag(false);
        boolean update = mAudioHelper.update(data);
        return update;
    }

    public List<MediaDataModel> getCollectList(int page, int size, MediaData mediaData) {
        LogUtils.debug(TAG, " getFolderDataList page: " + page + " size: " + size);
        int offset = page * size;
        List<AudioVo> list = mAudioHelper.queryBuilder().offset(offset).limit(size).where(AudioVoDao.Properties.FavFlag.eq(true)).list();
        List<MediaDataModel> mediaDataModels = convertData(list);
        long count = mAudioHelper.queryBuilder().where(AudioVoDao.Properties.FavFlag.eq(true)).count();
        setTotalData(mediaData, size, count);
        return mediaDataModels;
    }

    public boolean clearCollectList() {
        List<AudioVo> list = mAudioHelper.queryBuilder().where(AudioVoDao.Properties.FavFlag.eq(true)).list();
        List<AudioVo> audioVoStream = list.stream().map(new Function<AudioVo, AudioVo>() {
            @Override
            public AudioVo apply(AudioVo audioVo) {
                audioVo.setFavFlag(false);
                return audioVo;
            }
        }).collect(Collectors.toList());
        new Thread(new Runnable() {
            @Override
            public void run() {
                long l = SystemClock.currentThreadTimeMillis();
                boolean status = mAudioHelper.updateMultiple(audioVoStream);
                LogUtils.debug(TAG, " clearCollectList time: " + (SystemClock.currentThreadTimeMillis() - l));
            }
        }).start();

        return true;
    }

    public boolean addHistoryList(String mediaId, long currentTime) {
        //todo
        return false;
    }

    public List<MediaDataModel> getHistoryList(int page, int size) {
        //todo
        return null;
    }

    public boolean clearHistoryList() {
        //todo
        return false;
    }

    public MediaDataModel getHistory(String media) {
        //todo
        return null;
    }

    public Collection<? extends MediaDataModel> getSearchListAll(TypeModel searchDataType, MediaData mediaData) {
        String category_name = searchDataType.getCategory_name();
        String[] split = category_name.split(MediaIDHelper.SEARCH_SEPARATOR);
        String type = split[0];
        String name = split[1];
        int page = searchDataType.getPage();
        int size = searchDataType.getSize();
        LogUtils.debug(TAG, " getAllDataList page: " + page + " size: " +
                size + " type: " + type + " name: " + name);
        int offset = page * size;

        List<AudioVo> list = null;
        if (type.equals(MetadataTypeValue.TYPE_ARTIST.getType())) {
            list = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.SingerId.eq(name)).list();
        } else if (type.equals(MetadataTypeValue.TYPE_ALBUM.getType())) {
            list = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.AlbumId.eq(name)).list();
        } else if (type.equals(MetadataTypeValue.TYPE_GENRE.getType())) {
            list = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.GenreId.eq(name)).list();
        } else if (type.equals(MetadataTypeValue.TYPE_FOLDER.getType())) {
            //todo 需要查询该文件夹下的文件夹吗？
            list = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.FolderId.eq(name)).list();
        } else if (type.equals(MetadataTypeValue.TYPE_MUSIC.getType())) {
            list = mAudioHelper.queryBuilder().offset(offset).limit(size).where(
                    AudioVoDao.Properties.Id.eq(name)).list();
        }
        List<MediaDataModel> mediaDataModels = new ArrayList<>();
        if (list != null) {
            mediaDataModels.addAll(convertData(list));
        }
        return mediaDataModels;
    }
}
