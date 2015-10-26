package com.example.dell.mymuviz;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import extras.SortListener;
import fragments.FirstFragment;
import fragments.HitFragment;
import fragments.UpcomingFragment;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by dell on 10/6/2015.
 */
public class MainPage extends ActionBarActivity implements MaterialTabListener, View.OnClickListener {
    private static final int JOB_ID = 100;
    private static final long POLL_FREQUENCY = 28800000;
    private static final int TAB_COUNT = 3;

    private static final int SEARCH_FRAGMENT = 0;
    private static final int HIT_FRAGMENT = 1;
    private static final int UPCOMING_FRAGMENT = 2;
    private static final String TAG_SORT_NAME = "sortName";
    //tag associated with the FAB menu button that sorts by date
    private static final String TAG_SORT_DATE = "sortDate";
    //tag associated with the FAB menu button that sorts by ratings
    private static final String TAG_SORT_RATINGS = "sortRatings";


    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    MaterialTabHost mTabHost;
    ViewPager mViewPager;
    MoviePageAdapter mAdapter;
    FragmentManager mFragmentManager;
    private FloatingActionButton mFAB;
    private FloatingActionMenu mFABMenu;
    private JobScheduler mJobScheduler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFragmentManager=getSupportFragmentManager();
        NavigationDrawerFragement navigationDrawerFragement= (NavigationDrawerFragement) mFragmentManager.findFragmentById(R.id.mainPageDrawerFragment);
        navigationDrawerFragement.setUp(R.id.mainPageDrawerFragment, mDrawerLayout, mToolbar);
        mTabHost= (MaterialTabHost) findViewById(R.id.materialTabHost);
        mAdapter = new MoviePageAdapter(mFragmentManager);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);

            }
        });
        for (int i = 0; i < mAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setIcon(mAdapter.getIcon(i))
                            .setTabListener(this));
        }
        setupFAB();
        setupJob();


    }

    private void setupFAB() {
        ImageView iconFAB = new ImageView(this);
        iconFAB.setImageResource(R.drawable.ic_action_new);

        //set the appropriate background for the main floating action button along with its icon
        mFAB = new FloatingActionButton.Builder(this)
                .setContentView(iconFAB)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        //define the icons for the sub action buttons
        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView iconSortRatings = new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.ic_action_important);

        //set the background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_gray));


        //build the sub buttons
        SubActionButton buttonSortName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortRatings = itemBuilder.setContentView(iconSortRatings).build();

        //to determine which button was clicked, set Tags on each button
        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortDate.setTag(TAG_SORT_DATE);
        buttonSortRatings.setTag(TAG_SORT_RATINGS);

        buttonSortName.setOnClickListener(this);
        buttonSortDate.setOnClickListener(this);
        buttonSortRatings.setOnClickListener(this);

        //add the sub buttons to the main floating action button
        mFABMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRatings)
                .attachTo(mFAB)
                .build();
    }
    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        //set an initial delay with a Handler so that the data loading by the JobScheduler does not clash with the loading inside the Fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //schedule the job after the delay has been elapsed
                buildJob();
            }
        }, 30000);
    }

    private void buildJob() {
        //attach the job ID and the name of the Service that will work in the background
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));
        //set periodic polling that needs net connection and works across device reboots
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_view_database) {
            Intent intent  = new Intent(getApplicationContext(),
                    AndroidDatabaseManager.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = (Fragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
        if (fragment instanceof SortListener) {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                //call the sort by name method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                //call the sort by date method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                //call the sort by ratings method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByRating();
            }
        }


    }

    private class MoviePageAdapter extends FragmentStatePagerAdapter {

        FragmentManager supportFragmentManager;

        int[] icons = {R.drawable.ic_action_search, R.drawable.ic_action_trending, R.drawable.ic_action_upcoming};

        public MoviePageAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            this.supportFragmentManager = supportFragmentManager;

            //tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }


        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:{
                    //fragment = SearchFragment.newInstance("", "");
                    fragment=new FirstFragment();
                    break;
                }
                case 1:{
                    fragment = HitFragment.newInstance("", "");
                    break;
                }
                case 2:{
                    //fragment=new ThirdFragment();
                    fragment = UpcomingFragment.newInstance("", "");
                    break;
                }
            }
            /*switch (position) {
                case SEARCH_FRAGMENT: {
                    fragment = SearchFragment.newInstance("", "");
                    break;
                }
                case HIT_FRAGMENT: {
                    fragment = HitFragment.newInstance("", "");
                    break;
                }
                case UPCOMING_FRAGMENT: {
                    fragment = UpcomingFragment.newInstance("", "");
                    break;
                }
            }*/
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
