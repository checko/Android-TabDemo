package com.bradbergeron.android.tabdemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class TabsFragment extends Fragment implements TabHost.OnTabChangeListener {
    private static final String TAG = TabsFragment.class.getSimpleName();

    private static final String TAB_ONE = "One";
    private String mCurrentTabId = TAB_ONE;
    private static final String TAB_TWO = "Two";
    private static final String TAB_THREE = "Three";
    private TabHost mTabHost;
    private Fragment mCurrentTabFragment;

    public TabsFragment () { }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);

        mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabHost.addTab(mTabHost.newTabSpec(TAB_ONE).setIndicator(TAB_ONE).setContent(R.id.tab1));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TWO).setIndicator(TAB_TWO).setContent(R.id.tab2));
        mTabHost.addTab(
                mTabHost.newTabSpec(TAB_THREE).setIndicator(TAB_THREE).setContent(R.id.tab3));

        if (savedInstanceState != null) {
            mCurrentTabId = savedInstanceState.getString("currentTabId");
        }

        return view;
    }

    @Override
    public void onResume () {
        super.onResume();

        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTabByTag(mCurrentTabId);
        onTabChanged(mCurrentTabId);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("currentTabId", mCurrentTabId);
    }

    @Override
    public void onPause () {
        super.onPause();

        mTabHost.setOnTabChangedListener(null);
    }

    @Override
    public void onTabChanged (String tabId) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (mCurrentTabFragment != null) {
            transaction.detach(mCurrentTabFragment);
        }

        Fragment tabFragment = getFragmentManager().findFragmentByTag(tabId);

        if (tabFragment != null) {
            transaction.attach(tabFragment);
        } else {
            Bundle args = new Bundle();
            args.putString(TabFragment.ARGS_TAB_NAME, tabId);

            tabFragment = Fragment.instantiate(getActivity(), TabFragment.class.getName(), args);

            transaction.add(R.id.realtabcontent, tabFragment, tabId);
        }

        mCurrentTabId = tabId;
        mCurrentTabFragment = tabFragment;

        transaction.commit();
    }
}
