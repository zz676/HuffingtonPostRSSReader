package com.huffingtonpost.ssreader.huffingtonpostrssreader.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.adapter.CustomListAdapter;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.controllers.FeedListActivity;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.controllers.FeedListFragment;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssFeed;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.utilities.RssReader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zhisheng on 2015/7/3.
 */
public class RetrieveFeedTask extends AsyncTask<String, Void, Integer> {

    private final static String TAG = "RetrieveFeedTask";
    private Response response;

    private RssFeed rssFeed;
    private FeedListFragment mContext;
    private OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        response = client.newCall(request).execute();
        return response.body().string();
    }

    public RetrieveFeedTask(final FeedListFragment mContext) {
        this.mContext = (FeedListFragment) mContext;
    }

    @Override
    protected void onPreExecute() {

    }

    protected Integer doInBackground(String... urls) {
        //android.os.Debug.waitForDebugger();

        String response = null;
        try {
            response = run(urls[0]);
            InputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
            rssFeed = RssReader.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } catch (SAXException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    protected void onPostExecute(Integer result) {
        //android.os.Debug.waitForDebugger();
        if (result == 1) {
            mContext.updateUI(rssFeed);
        } else {
            Log.e(TAG, "Failed to fetch data!");
        }
    }
}
