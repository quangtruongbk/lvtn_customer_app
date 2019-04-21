package com.example.administrator.employeeapp.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "dvhcvn.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

    public GetAddressHelper getCity() {
        ArrayList<String> cityName = new ArrayList<>();
        ArrayList<String> cityID = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  'province'";
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        if (cursor.moveToFirst()) {
            do {
                cityName.add(cursor.getString(1));
                cityID.add(cursor.getString(0));
                Log.d("1abc", "getString: " + cursor.getString(0) + " " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        GetAddressHelper list = new GetAddressHelper(cityName, cityID);
        cursor.close();
        db.close();
        return list;
    }

    public GetAddressHelper getDistrict(String provinceID) {
        ArrayList<String> districtName = new ArrayList<>();
        ArrayList<String> districtID = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  'district' WHERE province_id ='" + provinceID + "'";
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        if (cursor.moveToFirst()) {
            do {
                districtName.add(cursor.getString(1));
                districtID.add(cursor.getString(0));
                Log.d("1abc", "getString: " + cursor.getString(0) + " " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        GetAddressHelper list = new GetAddressHelper(provinceID, districtName, districtID);
        cursor.close();
        db.close();
        return list;
    }

    public GetAddressHelper getWard(String districtID) {
        ArrayList<String> wardName = new ArrayList<>();
        ArrayList<String> wardID = new ArrayList<>();
        String selectQuery = "SELECT  * FROM  'ward' WHERE district_id ='" + districtID + "'";
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        if (cursor.moveToFirst()) {
            do {
                wardName.add(cursor.getString(1));
                wardID.add(cursor.getString(0));
                Log.d("1abc", "getString: " + cursor.getString(0) + " " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        GetAddressHelper list = new GetAddressHelper();
        list.setWardName(wardName);
        list.setWardID(wardID);
        cursor.close();
        db.close();
        return list;
    }

    public String getCityID(String city) {
        String cityID="";
        String selectQuery = "SELECT  * FROM  'province' WHERE name='"+city+"'";
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        if (cursor.moveToFirst()) {
            do {
                cityID = cursor.getString(0);
                Log.d("1abc", "getString: " + cursor.getString(0) + " " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cityID;
    }

    public String getDistrictID(String district) {
        String cityID="";
        String selectQuery = "SELECT  * FROM  'district' WHERE name='"+district+"'";
        SQLiteDatabase db;
        try {
            db = this.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        if (cursor.moveToFirst()) {
            do {
                cityID = cursor.getString(0);
                Log.d("1abc", "getString: " + cursor.getString(0) + " " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cityID;
    }
}