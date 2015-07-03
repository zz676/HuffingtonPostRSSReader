package com.huffingtonpost.ssreader.huffingtonpostrssreader.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;

/**
 * A fragment representing a single Feed detail screen.
 * This fragment is either contained in a {@link FeedListActivity}
 * in two-pane mode (on tablets) or a {@link FeedDetailActivity}
 * on handsets.
 */
public class FeedDetailFragment extends Fragment {

    private static final String TAG = "FeedDetailFragment";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String SELECTED_ITEM = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RssItem rssItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(SELECTED_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            rssItem = (RssItem) getArguments().getSerializable(SELECTED_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed_detail, container, false);
        rootView.setTag(TAG);
        if (rssItem != null) {
            final Activity activity = getActivity();
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            WebView webView = (WebView) rootView.findViewById(R.id.feed_detail);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    activity.setProgress(progress * 1000);
                }
            });
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (progressDialog.isShowing() && progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });

            webView.loadUrl(rssItem.getLink());
        }

        return rootView;
    }

/*    @Override
    public void onDestroy() {
        ViewGroup view = (ViewGroup) getActivity().getWindow().getDecorView();
        view.removeAllViews();
        view.setVisibility(View.INVISIBLE);
        //setVisible(false);
        super.onDestroy();
    }*/
}
