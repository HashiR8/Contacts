package com.example.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDB extends SQLiteOpenHelper {
    private static final String creatTablequery="CREATE TABLE contacts(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT, PNumber TEXT, WAddress TEXT, HAddress TEXT, Email TEXT,PicPath TEXT)";

    private static ContactsDB instance;
    public static synchronized ContactsDB getDB(Context context)
    {
        if(instance==null)
        {
            instance=new ContactsDB(context);
        }
        return instance;
    }

    public ContactsDB(Context context)
    {
        super(context,"contactsDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(creatTablequery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
