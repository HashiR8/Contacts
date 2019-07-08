package com.example.contacts;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ContactscontentProvider extends ContentProvider {
    private static final int contactsID=100;
    private static final int contactsID_other=110;
    private static final String PACKAGENAME="com.example.contacts";
    public static final Uri contactsDBURI=Uri.parse("content://"+PACKAGENAME+"/contacts");
    public static final Uri uniquecontactsDBURI=Uri.parse("content://"+PACKAGENAME+"/contacts/");
    private static final UriMatcher contactsURImatcher;
    static{
        contactsURImatcher=new UriMatcher(UriMatcher.NO_MATCH);
        contactsURImatcher.addURI(PACKAGENAME,"contacts",contactsID);
        contactsURImatcher.addURI(PACKAGENAME,"contacts/*",contactsID_other);
    }
    ContactsDB contactsDB;
    @Override
    public boolean onCreate() {
        contactsDB=ContactsDB.getDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor=null;
        SQLiteDatabase databaseInstance=contactsDB.getReadableDatabase();
        switch (contactsURImatcher.match(uri))
        {
            case contactsID:
                cursor=databaseInstance.rawQuery("SELECT * FROM contacts",null);
                int check=cursor.getCount();
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database=contactsDB.getWritableDatabase();
        int check;
        switch(contactsURImatcher.match(uri))
        {
            case contactsID:
                String query="INSERT INTO contacts (Name,PNumber,WAddress,HAddress,Email,PicPath) VALUES ('"+contentValues.get("Name")+"','"+contentValues.get("PNumber")+"','"+contentValues.get("WAddress")+"','"+contentValues.get("HAddress")+"','"+contentValues.get("Email")+"','"+contentValues.get("PicPath")+"')";
                Cursor cursor=database.rawQuery(query,null);
                check=cursor.getCount();
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase databaseInstance=contactsDB.getReadableDatabase();
        switch(contactsURImatcher.match(uri))
        {
            case contactsID:
                databaseInstance.rawQuery("DELETE FROM contacts WHERE ID ="+strings[0],null).moveToNext();
                break;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase databaseInstance=contactsDB.getReadableDatabase();
        switch(contactsURImatcher.match(uri))
        {
            case contactsID:
                String query="UPDATE contacts SET Name ='"+contentValues.get("Name")+"', PNumber ='"+contentValues.get("PNumber")+"', WAddress ='"+contentValues.get("WAddress")+"', HAddress ='"+contentValues.get("HAddress")+"', Email='"+contentValues.get("Email")+"' WHERE ID ="+strings[0];
                databaseInstance.rawQuery(query,null).moveToNext();
                break;
        }
        return 0;
    }
}
