package com.landmark.mediasessionlib.model.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

/**********************************************
 * Filename： DatabaseContext
 * Author:   wangyi@zlingsmart.com.cn
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.0.1        wangyi   1) …
 ***********************************************/
public class DatabaseContext extends ContextWrapper {
    public static String dbPath = "";
    public static String TAG = "DatabaseContext";

    public DatabaseContext(Context base, String dbPath) {
        super(base);
        this.dbPath = dbPath;
    }

    @Override
    public File getDatabasePath(String name){
        File dbDir = new File(dbPath);
        if(!dbDir.exists()){
            dbDir.mkdir();
        }

        File dbFile = new File(dbPath, name);
        if(!dbFile.exists()){
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dbFile;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}
