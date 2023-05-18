package com.project.gacha;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GachaDAO {
    private SQLiteDatabase db;
    private final GachaDatabaseHelper dbHelper;

    public GachaDAO(Context context) {
        dbHelper = new GachaDatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public void addCard(Card c) {
        open();
        ContentValues values = new ContentValues();
        values.put(GachaDatabaseHelper.COLUMN_ID, c.getId());
        values.put(GachaDatabaseHelper.COLUMN_IMAGE, c.getImage());
        values.put(GachaDatabaseHelper.COLUMN_RARITY, c.getRarity());
        values.put(GachaDatabaseHelper.COLUMN_NAME, c.getName());

        db.insert(GachaDatabaseHelper.TABLE_NAME, null, values);
        close();
    }

    public void updateCard (Card c) {
        open();
        db.delete(GachaDatabaseHelper.TABLE_NAME, GachaDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(c.getId())});
        close();

    }

    public void deleteCard (Card c) {
        open();
        db.delete(GachaDatabaseHelper.TABLE_NAME, GachaDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(c.getId())});
        close();
    }

    public ArrayList<Inventory> getAllRecord() {
        open();
        ArrayList<Inventory> invList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + GachaDatabaseHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                int record = cursor.getInt(0);
                int id = cursor.getInt(1);
                int image = cursor.getInt(2);
                int rarity = cursor.getInt(3);
                String name = cursor.getString(4);
                Inventory inventory = new Inventory(record, id, image, rarity, name);
                invList.add(inventory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return invList;
    }

    public ArrayList<Inventory> searchByRecord ( int minRecord, int maxRecord) {
        open();
        ArrayList<Inventory> inv = new ArrayList<>();

        String[] columns = {GachaDatabaseHelper.COLUMN_RECORD, GachaDatabaseHelper.COLUMN_ID, GachaDatabaseHelper.COLUMN_IMAGE, GachaDatabaseHelper.COLUMN_RARITY, GachaDatabaseHelper.COLUMN_NAME};
        String selections = GachaDatabaseHelper.COLUMN_RECORD + " >= ? AND " + GachaDatabaseHelper.COLUMN_RECORD + " <= ?";
        String[] selectionArgs = {String.valueOf(minRecord), String.valueOf(maxRecord)};

        Cursor cursor = db.query(GachaDatabaseHelper.TABLE_NAME, columns, selections, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int record = cursor.getInt(0);
                int id = cursor.getInt(1);
                int image = cursor.getInt(2);
                int rarity = cursor.getInt(3);
                String name = cursor.getString(4);
                Inventory inventory = new Inventory(record, id, image, rarity, name);
                inv.add(inventory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return inv;
    }

}
