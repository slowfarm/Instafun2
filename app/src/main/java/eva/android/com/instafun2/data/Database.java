package eva.android.com.instafun2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper implements BaseColumns {

    private SQLiteDatabase mSqLiteDatabase;
    private static Database instance;

    private static final String DATA_COLUMN = "dataColumn";

    private static final String DATABASE_NAME = "instaDataBase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_TABLE = "data";


    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + DATA_COLUMN + " text not null);";


    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }

    public void setUserData(String json) {
        mSqLiteDatabase = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATA_COLUMN, json);
        mSqLiteDatabase.insert(DATABASE_TABLE, null, values);
        mSqLiteDatabase.close();
    }

    public ArrayList<UserData> getUserData() {
        ArrayList<UserData> data = new ArrayList<>();
        mSqLiteDatabase = instance.getWritableDatabase();
        Cursor cursor = mSqLiteDatabase.query("data", new String[]{Database.DATA_COLUMN},
                null, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        do {
            try {
                data.add(new Parser().userDataParser(
                        cursor.getString(cursor.getColumnIndex(Database.DATA_COLUMN))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        while (cursor.moveToNext());
        cursor.close();
        mSqLiteDatabase.close();
        return data;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Update from version " + oldVersion + " to version " + newVersion);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}