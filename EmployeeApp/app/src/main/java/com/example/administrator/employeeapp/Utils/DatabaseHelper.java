package com.example.administrator.employeeapp.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private String DB_PATH;
    private static String DB_NAME="/dvhcvn.db";
    private SQLiteDatabase myDatabase;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/"+context.getApplicationContext().getPackageName()+"/databases/";
        boolean dbexist = checkdatabase();
        Log.d("1abc", "DB_PATH: " + DB_PATH);
        if(dbexist)
        {
        }
        else
        {
            System.out.println("Database doesn't exist!");

            createDatabse();
        }
    }

    public void createDatabse() {

        this.getReadableDatabase();

        try
        {
            copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SQLiteDatabase getMyDatabase()
    {
        return myDatabase;
    }


    private void copyDatabase() throws IOException {

        AssetManager dirPath = context.getAssets();

        InputStream myinput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream("data/data/com.example.administrator.employeeapp/databases/dvhcvn.sqlite");

        byte[] buffer = new byte[1024];
        int length;

        while ((length = myinput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myinput.close();
    }

    public void open()
    {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close()
    {
        myDatabase.close();
        super.close();
    }

    private boolean checkdatabase() {

        boolean checkdb = false;

        try
        {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        }
        catch (SQLiteException e)
        {
            System.out.println("Databse doesn't exist!");
        }

        return checkdb;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<String> getCityTest(){
        ArrayList<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM  'province'";
        SQLiteDatabase db = this.getMyDatabase();
       /* Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }*/
        Cursor cursor=db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'",null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Log.d("1abc", "Table Name=> "+cursor.getString(0));
        }

        cursor.close();
        db.close();
        return list;
    }

}
