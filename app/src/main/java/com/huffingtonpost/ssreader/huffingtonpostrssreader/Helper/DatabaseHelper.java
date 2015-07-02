package com.huffingtonpost.ssreader.huffingtonpostrssreader.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Zhisheng on 6/24/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    private static final int DATABASE_VERSION = 1;
    /**
     * Log tag
     */
    private static final String TAG = "DatabaseHelper";
    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "HuffingtonPostRSSReader";

    /**
     * Table Name
     */
    public static final String TABLE_FAVORITE_FEEDS = "Favorite_Feeds";

    //CLAIMS TABLE- COLUMN NAMES
    public static final String KEY_FEED_ID = "feed_id";
    public static final String KEY_FEED_TITLE = "feed_title";
    public static final String KEY_FEED_PDATE = "feed_pdate";
    public static final String KEY_FEED_AUTHOR = "feed_author";
    public static final String KEY_FEED_LINK = "feed_link";

    private static final String CREATE_FAVORITE_FEEDS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FAVORITE_FEEDS + "("
            + KEY_FEED_ID + " TEXT PRIMARY KEY,"
            + KEY_FEED_TITLE + " TEXT,"
            + KEY_FEED_PDATE + " TEXT,"
            + KEY_FEED_AUTHOR + " TEXT,"
            + KEY_FEED_LINK + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_FEEDS_TABLE);
    }

    /**
     * Insert a new feed into TABLE_FAVORITE_FEEDS
     *
     * @param item the item to be inserted
     * @return true if successfully, otherwise false
     */
    public Boolean addNewFeed(RssItem item) {
        boolean isSuccess = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_FEED_ID, UUID.randomUUID().toString());
            values.put(KEY_FEED_TITLE, item.getTitle().replace("'", "\"").toUpperCase());
            values.put(KEY_FEED_PDATE, dateFormat.format(item.getPubDate()));
            values.put(KEY_FEED_AUTHOR, item.getAuthor().toUpperCase());
            values.put(KEY_FEED_LINK, item.getLink());
            db.insert(TABLE_FAVORITE_FEEDS, null, values);
            db.close();
            isSuccess = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return isSuccess;
    }

    /**
     * Delete a existing feed in TABLE_FAVORITE_FEEDS
     *
     * @param item item to be deleted
     * @return true if successfully, otherwise false
     */
    public Boolean deleteFeed(RssItem item) {
        int deleted = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            deleted = db.delete(TABLE_FAVORITE_FEEDS, KEY_FEED_TITLE + " = '" + item.getTitle().replace("'", "\"").toUpperCase() + "'", null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return deleted > 0;
    }

    /**
     * Check whether a feed is in TABLE_FAVORITE_FEEDS
     *
     * @param item item to be checked
     * @return true if existing, otherwise false
     */
    public Boolean isExisting(RssItem item) {
        int num = 0;
        try {
            String selectQuery = "SELECT  count(*) FROM " + TABLE_FAVORITE_FEEDS + " where " + KEY_FEED_TITLE + " = '"
                    + item.getTitle().replace("'", "\"").toUpperCase() + "' and " + KEY_FEED_AUTHOR + " = '" + item.getAuthor().toUpperCase() + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    num = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return num > 0;
    }

    /**
     * Get all the favorite feeds in the database
     *
     * @return a list of RssItem objects
     */
    public ArrayList<RssItem> getAllFavoriteFeeds() {

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE_FEEDS + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<RssItem> items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                RssItem item = new RssItem();
                item.setGuid(cursor.getString(0));
                item.setTitle(cursor.getString(1));
                item.setPubDate(cursor.getString(2));
                item.setAuthor(cursor.getString(3));
                item.setLink(cursor.getString(4));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
