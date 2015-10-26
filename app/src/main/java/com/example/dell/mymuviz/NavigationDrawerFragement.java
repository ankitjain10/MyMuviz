package com.example.dell.mymuviz;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pojo.Information;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragement extends Fragment {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    String sharedPrefName = "drawerPref";
    String KEY_USER_PREF = "user_learned_drawer";
    boolean mUserdrawerLearned;
    boolean mSavedInstance;
    RecyclerView mDrawerList;
    RecyclerAdapter mRecyclerAdapter;
    View view;
    public NavigationDrawerFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserdrawerLearned = Boolean.valueOf(readFromPref(getActivity(), KEY_USER_PREF, "false"));
        //L.m("Before mUserdrawerLearned "+mUserdrawerLearned+"");
        if (savedInstanceState != null) {
            mSavedInstance = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_navigationdrawer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        mRecyclerAdapter=new RecyclerAdapter(getActivity(),getData());
        mDrawerList.setAdapter(mRecyclerAdapter);
        mDrawerList.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    public List<Information> getData() {
        //load only static data inside a drawer
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_action_search_orange, R.drawable.ic_action_trending_orange, R.drawable.ic_action_upcoming_orange};
        String[] titles = getResources().getStringArray(R.array.drawer_tabs);
        for (int i = 0; i < titles.length; i++) {
            Information information = new Information();
            information.title = titles[i];
            information.iconId = icons[i];
            data.add(information);
        }
        return data;
    }


    public void setUp(int fragmentId,DrawerLayout mDrawerLayout, final Toolbar mToolbar) {
        view =getActivity().findViewById(fragmentId);
        this.mDrawerLayout = mDrawerLayout;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserdrawerLearned){
                    mUserdrawerLearned=true;
                    //L.m("After mUserdrawerLearned  "+String.valueOf(mUserdrawerLearned));
                    saveToPref(getActivity(), KEY_USER_PREF, String.valueOf(mUserdrawerLearned));
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mToolbar.setAlpha(1 - slideOffset / 2);

            }
        };
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
        if(!mUserdrawerLearned && !mSavedInstance){
            mDrawerLayout.openDrawer(view);
        }
    }

    public void saveToPref(Context context, String prefKey, String prefValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefKey, prefValue);
        editor.apply();
    }

    public String readFromPref(Context context, String prefKey, String prefValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefKey, prefValue);
    }
}

