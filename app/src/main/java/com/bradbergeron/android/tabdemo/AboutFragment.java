package com.bradbergeron.android.tabdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import us.feras.mdv.MarkdownView;

public class AboutFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = AboutFragment.class.getSimpleName();

    private MarkdownView mMarkdownView;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isRefreshing;
    private boolean mContentLoaded;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setColorScheme(R.color.refreshing1, R.color.refreshing2, R.color.refreshing3,
                                      R.color.refreshing4);

        mMarkdownView = (MarkdownView) view.findViewById(R.id.markdownView);
        mMarkdownView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        mMarkdownView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged (WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    isRefreshing = false;
                    mContentLoaded = true;
                    mRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (savedInstanceState != null) {
            mContentLoaded = savedInstanceState.getBoolean("contentLoaded");
            mMarkdownView.restoreState(savedInstanceState);
        }

        return view;
    }

    @Override
    public void onResume () {
        super.onResume();

        mRefreshLayout.setOnRefreshListener(this);

        if (!mContentLoaded) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("contentLoaded", mContentLoaded);
        mMarkdownView.saveState(outState);
    }

    @Override
    public void onPause () {
        super.onPause();

        mRefreshLayout.setOnRefreshListener(null);
    }

    @Override
    public void onRefresh () {
        if (isRefreshing) {
            return;
        }

        mMarkdownView.loadMarkdownFile(
                "https://raw.githubusercontent.com/bdbergeron/Android-TabDemo/master/README.md");
    }
}
