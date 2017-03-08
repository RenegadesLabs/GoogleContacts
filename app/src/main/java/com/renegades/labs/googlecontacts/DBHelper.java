package com.renegades.labs.googlecontacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Виталик on 07.03.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table contacts ("
                + "id integer primary key autoincrement,"
                + "accId text,"
                + "first text,"
                + "last text" + ");");

        sqLiteDatabase.execSQL("create table emails ("
                + "id integer primary key autoincrement,"
                + "contactId integer,"
                + "email text" + ");");

        sqLiteDatabase.execSQL("create table phones ("
                + "id integer primary key autoincrement,"
                + "contactId integer,"
                + "phone text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
