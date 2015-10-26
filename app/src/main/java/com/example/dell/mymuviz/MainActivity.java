package com.example.dell.mymuviz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends ActionBarActivity {
    private static final int JOB_ID = 100;
    private static final long POLL_FREQUENCY = 28800000;
    private static final int TAB_COUNT = 3;

    private static final int SEARCH_FRAGMENT = 0;
    private static final int HIT_FRAGMENT = 1;
    private static final int UPCOMING_FRAGMENT = 2;
    //private MoviePageAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_bar);
    }
}


/*

    public void onDrawerItemClicked(int index) {
        mViewPager.setCurrentItem(index);
    }
    public void onDrawerSlide(float slideOffset) {
        toggleTranslateFAB(slideOffset);
    }
    private void toggleTranslateFAB(float slideOffset) {
        if (mFABMenu != null) {
            if (mFABMenu.isOpen()) {
                mFABMenu.close(true);
            }
            mFAB.setTranslationX(slideOffset * 200);
        }
    }


    private void init() {
        mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mFragmentManager = getSupportFragmentManager();
        mAdapter = new MoviePageAdapter(mFragmentManager);
        mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);
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

    }

    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
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

    private void setupFAB() {
        //define the icon for the main floating action button
        ImageView iconFAB = new ImageView(this);
        iconFAB.setImageResource(R.drawable.ic_action_new);
        mFAB = new FloatingActionButton.Builder(this)
                .setContentView(iconFAB)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();
        ImageView sortName = new ImageView(this);
        sortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView sortDate = new ImageView(this);
        sortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView sortRating = new ImageView(this);
        sortRating.setImageResource(R.drawable.ic_action_important);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_gray));
        SubActionButton sortByNameButton = itemBuilder
                .setContentView(sortName)
                .build();
        SubActionButton sortByDateButton = itemBuilder.setContentView(sortDate).build();
        SubActionButton sortByRatingButton = itemBuilder.setContentView(sortRating).build();

        sortByNameButton.setTag("sortName");
        sortByDateButton.setTag("sortDate");
        sortByRatingButton.setTag("sortRating");

        sortByNameButton.setOnClickListener(this);
        sortByDateButton.setOnClickListener(this);
        sortByRatingButton.setOnClickListener(this);

        mFABMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sortByNameButton)
                .addSubActionView(sortByDateButton)
                .addSubActionView(sortByRatingButton)
                .attachTo(mFAB)
                .build();
        mJobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, (1000 * 30));

    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));
        builder.setPersisted(true)
                .setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        try {
            mJobScheduler.schedule(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition() );
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onClick(View v) {

    }

    private class MoviePageAdapter extends FragmentStatePagerAdapter {

        FragmentManager supportFragmentManager;

        int[] icons = {R.drawable.tomatoes, R.drawable.popcorn, R.drawable.rotten};

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
            switch (position) {
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
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
*/
