package pl.patrykheciak.projektowe;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import pl.patrykheciak.projektowe.movies.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    private int sortType = 0;
    private MoviesFragment moviesFragment;
    private String KEY_SORT_TYPE = "KEY_SORT_TYPE";
    private String MOVIES_FRAGMENT_KEY = "MOVIES_FRAGMENT_KEY" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AndroidCameraApi.class));
            }
        });
        Log.d("MainActivity", "onCreate");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String fragTag = savedInstanceState.getString(MOVIES_FRAGMENT_KEY);
        if (fragTag != null)
        {
            moviesFragment = (MoviesFragment)getSupportFragmentManager().findFragmentByTag(fragTag);
        }

        if (savedInstanceState.containsKey(KEY_SORT_TYPE)) {
            sortType = savedInstanceState.getInt(KEY_SORT_TYPE);
            if (moviesFragment != null) {
                moviesFragment.applySortType(sortType);
            } else {
                Log.d("MainActivity", "moviesFragment not yet created");
            }
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        if (sortType == 0) menu.findItem(R.id.menu_sort_name_asc).setChecked(true);
        if (sortType == 1) menu.findItem(R.id.menu_sort_name_desc).setChecked(true);
        if (sortType == 2) menu.findItem(R.id.menu_sort_date_asc).setChecked(true);
        if (sortType == 3) menu.findItem(R.id.menu_sort_date_desc).setChecked(true);
        if (sortType == 4) menu.findItem(R.id.menu_sort_duration_asc).setChecked(true);
        if (sortType == 5) menu.findItem(R.id.menu_sort_duration_desc).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_sort_name_asc) {
            item.setChecked(true);
            sortType = 0;
        }
        if (id == R.id.menu_sort_name_desc) {
            item.setChecked(true);
            sortType = 1;
        }
        if (id == R.id.menu_sort_date_asc) {
            item.setChecked(true);
            sortType = 2;
        }
        if (id == R.id.menu_sort_date_desc) {
            item.setChecked(true);
            sortType = 3;
        }
        if (id == R.id.menu_sort_duration_asc) {
            item.setChecked(true);
            sortType = 4;
        }
        if (id == R.id.menu_sort_duration_desc) {
            item.setChecked(true);
            sortType = 5;
        }
        moviesFragment.applySortType(sortType);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SORT_TYPE, sortType);

        if (moviesFragment !=null){
            String tag = moviesFragment.getTag();
            outState.putString(MOVIES_FRAGMENT_KEY, tag);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (moviesFragment == null)
                    moviesFragment = MoviesFragment.newInstance();
                return moviesFragment;
            }
            if (position == 1)
                return MoviesFragment.newInstance();

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
