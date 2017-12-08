package com.example.sahar.androidlabs;

import android.content.Context;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sahar on 2017-10-10.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Messages.db" ;
    public static final String TABLE_NAME = "Messages_TABLE" ;
    public static final int VERSION_NUM = 203;

    //private final Context mCtx;

    // column names
    public static final  String COL_ID = "_id";
    public static final  String COL_MESSAGE ="_msg" ;

    //  SQL Instructions: Create table
    private static final String CREATE_TABLE_MESSAGES = "create table "  + TABLE_NAME  +
            " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_MESSAGE + " text "
            + " ); " ;


    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i("ChatDatabaseHelper", "Calling onCreate");
        db.execSQL(CREATE_TABLE_MESSAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion="+ newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void deleteItem(String id) {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = " + id);
    }


}
