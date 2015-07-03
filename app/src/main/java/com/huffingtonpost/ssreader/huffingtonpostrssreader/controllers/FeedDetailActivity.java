package com.huffingtonpost.ssreader.huffingtonpostrssreader.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.helper.DatabaseHelper;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;

/**
 * An activity representing a single Feed detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link FeedListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link FeedDetailFragment}.
 */
public class FeedDetailActivity extends AppCompatActivity {

    private final static String TAG = "FeedDetailActivity";
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;
    private DatabaseHelper dbHelper;
    private RssItem currentItem;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DatabaseHelper(this);
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        currentItem = (RssItem) getIntent().getSerializableExtra(FeedDetailFragment.SELECTED_ITEM);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(FeedDetailFragment.SELECTED_ITEM, currentItem);
            FeedDetailFragment fragment = new FeedDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.feed_detail_container, fragment)
                    .commit();
        }
        if (currentItem != null) {
            mShareIntent = new Intent();
            mShareIntent.setAction(Intent.ACTION_SEND);
            mShareIntent.setType("text/plain");
            StringBuilder builder = new StringBuilder();
            builder.append(currentItem.getTitle() + "\n");
            builder.append(currentItem.getLink());
            mShareIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fee_detail_menu, menu);
        // Locate MenuItem with ShareActionProvider
        this.menu = menu;
        setFavoriteStatus(menu);

        MenuItem item = menu.findItem(R.id.share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(mShareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    private void setFavoriteStatus(Menu menu) {
        if (dbHelper.isExisting(currentItem)) {
            menu.findItem(R.id.favorite).setIcon(R.drawable.favorite);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favorite:
                new DatabaseTask().execute();
                return true;
            case R.id.share:
                Toast msxg = Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG);
                msxg.show();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setVisible(false);
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    class DatabaseTask extends AsyncTask<Void, Void, Integer> {

        private String tyepOfOperation;
        private boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        protected Integer doInBackground(Void... args) {
            //android.os.Debug.waitForDebugger();
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
                            Toast.makeText(getBaseContext(), "Added into favorites successfully.", Toast.LENGTH_LONG).show();
                            menu.findItem(R.id.favorite).setIcon(R.drawable.favorite);
                            break;
                        case "DELETE":
                            menu.findItem(R.id.favorite).setIcon(R.drawable.un_favorite);
                            break;
                    }
                }
            }
        }
    }
}
