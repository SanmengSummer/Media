package com.landmark.mediasessionlib.model.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.landmark.mediasessionlib.model.application.MediaApplication;
import com.landmark.mediasessionlib.model.common.Constants;
import com.landmark.mediasessionlib.model.db.dao.DaoMaster;
import com.landmark.mediasessionlib.model.db.dao.DaoSession;
import com.landmark.mediasessionlib.model.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

/**********************************************
 * Filename： DaoManager
 * Author:   wangyi@zlingsmart.com.cn
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.0.1        wangyi   1) …
 ***********************************************/
@SuppressLint("NewApi")
public class DaoManager {

    //多线程中要被共享的使用volatile关键字修饰
    private volatile static DaoManager manager = new DaoManager();
    private DaoMaster mDaoMaster;
    private DaoMaster.DevOpenHelper mHelper;
    private DaoSession mDaoSession;
    private String TAG = "DaoManager";

    /**
     * 单例模式获得操作数据库对象
     */
    public static DaoManager getInstance() {
        return manager;
    }

    private DaoManager() {
        setDebug();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
            if (!sdExist) {//如果不存在,
                LogUtils.error(TAG, "SD卡不存在，请加载SD卡");
                throw new RuntimeException("SD卡不存在，请加载SD卡");
            }
//            String dbDir = Environment.getStorageDirectory().getAbsolutePath() + "/scannerdb";

            DatabaseContext context = new DatabaseContext(MediaApplication.getContext(), Constants.DB_DIR);
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, Constants.DB_NAME, null);
            mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
                setDebug();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    private void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    private void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}
