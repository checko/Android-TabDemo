package com.bradbergeron.android.tabdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment {
    public static final String ARGS_TAB_NAME = "tabName";

    private static final String TAG = TabFragment.class.getSimpleName();

    private String mName;

    public TabFragment () { }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mName = getArguments().getString(ARGS_TAB_NAME, "Unnamed");
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        TextView nameTextView = (TextView) view.findViewById(R.id.tabTextView);
        nameTextView.setText(mName);

        return view;
    }
}
