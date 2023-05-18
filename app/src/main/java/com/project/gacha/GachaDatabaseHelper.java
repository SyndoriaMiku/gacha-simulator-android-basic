package com.project.gacha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GachaDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "history_db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "history";
    public static final String COLUMN_RECORD = "record";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_RARITY = "rarity";
    public static final String COLUMN_NAME = "name";

    public GachaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_RECORD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID + " INTEGER," +
                COLUMN_IMAGE + " INTEGER," +
                COLUMN_RARITY + " INTEGER," +
                COLUMN_NAME + " TEXT" +
                ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
}
