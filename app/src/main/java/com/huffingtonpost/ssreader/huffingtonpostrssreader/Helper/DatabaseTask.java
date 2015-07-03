package com.huffingtonpost.ssreader.huffingtonpostrssreader.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Menu;
import android.widget.Toast;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhisheng on 2015/7/2.
 */
public class DatabaseTask extends AsyncTask<RssItem, Void, Integer> {
    private DatabaseHelper dbHelper;
    private String tyepOfOperation;
    private boolean isSuccess = false;
    private Context mContext;
    private Menu mMenu;

    public DatabaseTask(final Context mContext, final Menu mMenu) {
        this.mContext = mContext;
        this.mMenu = mMenu;
        dbHelper = new DatabaseHelper(mContext);
    }

    @Override
    protected void onPreExecute() {

    }

    protected Integer doInBackground(RssItem... items) {
        //android.os.Debug.waitForDebugger();
        final RssItem currentItem = items[0];
        try {
            if (dbHelper.isExisting(currentItem)) {
                isSuccess = dbHelper.deleteFeed(currentItem);
                tyepOfOperation = "DELETE";
            } else {
                isSuccess = dbHelper.addNewFeed(currentItem);
                tyepOfOperation = "INSERT";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    protected void onPostExecute(Integer result) {
        //android.os.Debug.waitForDebugger();
        if (result == 1) {
            if (isSuccess) {
                switch (tyepOfOperation) {
                    case "INSERT":
                        Toast.makeText(mContext, "Added into favorites successfully.", Toast.LENGTH_LONG).show();
                        mMenu.findItem(R.id.favorite).setIcon(R.drawable.favorite);
                        break;
                    case "DELETE":
                        mMenu.findItem(R.id.favorite).setIcon(R.drawable.un_favorite);
                        break;
                }
            }
        }
    }
}
