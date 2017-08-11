package com.xiongxh.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.xiongxh.popularmovies.fragments.MovieDetailFragment;
import com.xiongxh.popularmovies.fragments.MovieGridFragment;
import com.xiongxh.popularmovies.sync.MovieSyncTask;
import com.xiongxh.popularmovies.sync.MovieSyncUtils;
import com.xiongxh.popularmovies.utilities.FakeMovieUtils;

import static com.xiongxh.popularmovies.data.MovieProvider.LOG_TAG;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, MovieGridFragment.Callback {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String MOVIEDETAILFRAGMENT = "MOVIEDETAILGRAGMENT";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;

            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieGridFragment(), MOVIEDETAILFRAGMENT)
                        .commit();
            }else{
                mTwoPane = false;
            }
        }

        setupSharedPreferences();

        MovieSyncUtils.startImmediateSync(this);
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onItemSelected(Uri movieUri) {
        Log.d(LOG_TAG, "Entering onItemSelected...");
        if (mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DEFAULT_MOVIE_URI, movieUri);

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, movieDetailFragment, MOVIEDETAILFRAGMENT)
                    .commit();
            Log.d(LOG_TAG, "Exit onItemSelected if block.");
        }else {
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class)
                .setData(movieUri);
            startActivity(movieDetailIntent);
            Log.d(LOG_TAG, "Exit onItemSelected else block. movieUri: " + movieUri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        Log.d(LOG_TAG, "onSavedInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent startSettingActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingActivity);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
