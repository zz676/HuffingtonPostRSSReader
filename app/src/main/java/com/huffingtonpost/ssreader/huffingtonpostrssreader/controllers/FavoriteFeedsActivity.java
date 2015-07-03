package com.huffingtonpost.ssreader.huffingtonpostrssreader.controllers;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.adapter.CustomListAdapter;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.helper.DatabaseHelper;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;
import java.util.ArrayList;

public class FavoriteFeedsActivity extends ListActivity {

    private final static String TAG = "FavoriteFeedsActivity";
    private CustomListAdapter favoriteFeedsListAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<RssItem> favoriteFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_feeds);
        databaseHelper = new DatabaseHelper(this);
        new QueryFavoriteFeedsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite_feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.back_to_home){
            final Intent homeIntent = new Intent(this, FeedListActivity.class);
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        handleDialog(favoriteFeeds.get(position));
    }

    private void handleDialog(RssItem item) {
        Dialog feedDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        feedDialog.getWindow();
        // dialog.setTitle("Please take a photo.");
        feedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedDialog.setContentView(R.layout.fragment_feed_detail);

        if (item != null) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            WebView webView = (WebView) feedDialog.findViewById(R.id.feed_detail);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    setProgress(progress * 1000);
                }
            });
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplication(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressDialog.isShowing() && progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });
            webView.loadUrl(item.getLink());
            feedDialog.setCancelable(true);
            feedDialog.show();
            Toast.makeText(getApplication(), "Press BACK to exit full screen.", Toast.LENGTH_SHORT).show();
        }
    }

    class QueryFavoriteFeedsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... urls) {
            //android.os.Debug.waitForDebugger();

            try {
                favoriteFeeds = databaseHelper.getAllFavoriteFeeds();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            //android.os.Debug.waitForDebugger();
            favoriteFeedsListAdapter = new CustomListAdapter(getApplication(), favoriteFeeds, false);
            if(favoriteFeeds != null){
                getListView().setAdapter(favoriteFeedsListAdapter);
            }
        }
    }
}
