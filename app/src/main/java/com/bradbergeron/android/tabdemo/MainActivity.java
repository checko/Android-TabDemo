package com.bradbergeron.android.tabdemo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private String[] mItemTitles;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private int mCurrentItemIndex = 0;

    private Fragment getContentFragment () {
        return getFragmentManager().findFragmentById(R.id.content_frame);
    }

    private boolean isCurrentFragment (String tag) {
        return (getContentFragment() != null && getContentFragment().getTag() != null &&
                getContentFragment().getTag().equals(tag));
    }

    private void selectItem (int position) {
        final FragmentManager fm = getFragmentManager();

        String fragmentName;
        Fragment fragment;
        boolean isDefaultFragment = false;

        switch (position) {
            case 1: {
                fragmentName = AboutFragment.class.getName();

                break;
            }
            case 0:
            default: {
                fragmentName = TabsFragment.class.getName();
                isDefaultFragment = true;

                break;
            }
        }

        if (isCurrentFragment(String.valueOf(position))) {
            mCurrentItemIndex = position;

            mDrawerList.setItemChecked(position, true);
            setTitle(mItemTitles[position]);

            mDrawerLayout.closeDrawer(mDrawerList);

            return;
        }

        if (isDefaultFragment && fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            fragment = Fragment.instantiate(this, fragmentName, null);

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment, String.valueOf(position));

            if (!isDefaultFragment) {
                ft.addToBackStack(null);
            }

            ft.commit();
        }

        mCurrentItemIndex = position;

        mDrawerList.setItemChecked(position, true);
        setTitle(mItemTitles[position]);

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                                                  R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened (View drawerView) {
                super.onDrawerOpened(drawerView);

                getActionBar().setTitle(mDrawerTitle);
            }

            @Override
            public void onDrawerClosed (View drawerView) {
                super.onDrawerClosed(drawerView);

                getActionBar().setTitle(mTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mItemTitles = getResources().getStringArray(R.array.navigation_items);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                                         mItemTitles)
        );
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCurrentItemIndex = savedInstanceState.getInt("currentItemIndex", 0);
    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume () {
        super.onResume();

        selectItem(mCurrentItemIndex);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("currentItemIndex", mCurrentItemIndex);
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();

        if (getContentFragment() != null) {
            int index = Integer.valueOf(getContentFragment().getTag());
            mDrawerList.setItemChecked(index, true);
            setTitle(mItemTitles[index]);
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle (CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
