package com.app.aalap.notes.DBUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by Aalap on 2016-12-08.
 */

public class NoteDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notes.db";
    public static final int DATABASE_VERSION = 1;
    public static final String Col1_ID = "_id";
    public static final String Col2_TITLE = "title";
    public static final String Col3_NOTE = "note";
    public static final String Col4_TIME = "time";
    public static final String TABLE_NAME = "note_info";
    private static final String TAG = NoteDBHelper.class.getSimpleName();
    Cursor cursor;

    public NoteDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE "+TABLE_NAME+"("+ Col1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +","+
                Col2_TITLE+" TEXT" + ","+
                Col3_NOTE+ " TEXT" + ","+
                Col4_TIME+ " TEXT"+
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    public void addNote(String title, String note, String time, SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col2_TITLE, title);
        contentValues.put(Col3_NOTE, note);
        contentValues.put(Col4_TIME, time);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void updateNote(int id, String title, String detail, String time, SQLiteDatabase sqLiteDatabase){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Col2_TITLE, title);
        updatedValues.put(Col3_NOTE, detail);
        updatedValues.put(Col4_TIME, time);
        sqLiteDatabase.update(TABLE_NAME, updatedValues, "_id="+id, null);
    }

    public void delete(SQLiteDatabase sqLiteDatabase, int id, Context context){
        sqLiteDatabase.delete(TABLE_NAME, Col1_ID +"="+id, null);
        Toasty.success(context, "Note deleted").show();
    }

    public Cursor getInformation(SQLiteDatabase sqLiteDatabase){
        String[] projections = {Col1_ID, Col2_TITLE, Col3_NOTE, Col4_TIME};
        cursor = sqLiteDatabase.query(TABLE_NAME, projections, null, null, null, null, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
