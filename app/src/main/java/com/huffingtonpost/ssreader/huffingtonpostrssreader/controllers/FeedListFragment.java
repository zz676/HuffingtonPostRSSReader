package com.huffingtonpost.ssreader.huffingtonpostrssreader.controllers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.adapter.CustomListAdapter;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.helper.RetrieveFeedTask;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssFeed;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.utilities.RssReader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Feeds. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link FeedDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FeedListFragment extends ListFragment {

    protected static final String URL = "http://www.huffingtonpost.com/feeds/index.xml";
    private static final String SELECT_ITEM = "SELECTED_ITEM";
    private static final String TAG = "RetrieveFeedTask";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private boolean mTwoPane = false;
    private List<RssItem> items = new ArrayList<RssItem>();
    private RssFeed rssFeed;
    private CustomListAdapter listAdapter;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(RssItem item);
    }

    /**
     * A simple implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(RssItem item) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        new RetrieveFeedTask(this).execute(URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_feeds_listview, container, false);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sCallbacks;

    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(rssFeed.getRssItems().get(position));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            savedInstanceState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    public void isTwoPanel() {
        mTwoPane = true;
    }

    public void refresh() {
        new RetrieveFeedTask(this).execute(URL);
    }

    public void updateUI(RssFeed rssFeed) {
        this.rssFeed = rssFeed;
        listAdapter = new CustomListAdapter(getActivity(), rssFeed.getRssItems(), mTwoPane);
        getListView().setAdapter(listAdapter);
        if (mTwoPane) {
            FeedListActivity feedListActivity = (FeedListActivity) getActivity();
            feedListActivity.beginNewFragment(rssFeed.getRssItems().get(0));
        }
    }


}
