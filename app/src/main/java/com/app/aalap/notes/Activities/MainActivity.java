package com.app.aalap.notes.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app.aalap.notes.Fragments.ReminderListFragment;
import com.app.aalap.notes.Fragments.NoteListFragment;
import com.app.aalap.notes.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    FragmentPagerAdapter pagerAdapter;
    TabLayout tablayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablayout = findViewById(R.id.tablayout);
        pagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setSelected(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class FragPagerAdapter extends FragmentPagerAdapter {

        public FragPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = position == 0 ? new NoteListFragment() : new ReminderListFragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Text" : "Reminder";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
