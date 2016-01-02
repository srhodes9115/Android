package com.example.wildcat.mysqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

/**
 * Created by wildcat on 10/28/2015.
 */
public class StudentHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="studentTable.db";
    private static final int DB_VERSION = 1;
    private static final String TB_STUDENT = "student";
    private static final String S_ID = BaseColumns._ID;
    public static final String S_NAME="name";
    public static final String S_MAJOR ="major";
    public static final String S_YEAR = "year";

    StudentHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_STUDENT + " (" + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + S_NAME + " TEXT NOT NULL, " + S_MAJOR + " TEXT NOT NULL," + S_YEAR + " INTEGER NOT NULL" +");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_STUDENT);
        onCreate(db);
    }

    public Cursor getStudents()
    {
        String[] cols = new String[] {S_ID, S_NAME, S_MAJOR,S_YEAR};
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TB_STUDENT,cols,null,null,null,null,S_NAME);
    }

    public Cursor getStudent(int studentId)
    {
        String[] cols = new String[] {S_ID,S_NAME, S_MAJOR, S_YEAR};
        String sel = S_ID + "=?";
        String[] selArgs = new String[] { String.valueOf(studentId)};
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TB_STUDENT, cols, sel, selArgs, null, null, null);
    }

    public long addStudent(String name, String status, int year) {
        ContentValues cv = new ContentValues();
        cv.put(S_NAME,name);
        cv.put(S_MAJOR,status);
        cv.put(S_YEAR, year);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TB_STUDENT, null, cv);
    }

    public long numStudents() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteStatement st = db.compileStatement("SELECT COUNT(1) " + "FROM " + TB_STUDENT + ";");
        return st.simpleQueryForLong();
    }

    public boolean delStudent (int studentId) {
        String sel = S_ID + "=?";
        String [] selArgs = new String [] {String.valueOf(studentId)};
        SQLiteDatabase db = getWritableDatabase();
        return (db.delete(TB_STUDENT,sel,selArgs) > 0);
    }

}
