package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private String mWeather;

    public DetailActivityFragment()
    {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        mWeather = intent.getStringExtra(ForecastFragment.WEATHER);
        //Log.i("Weather", weather);
        TextView textView = (TextView)view.findViewById(R.id.detail_fragment_text_view);
        textView.setText(mWeather);


        /*
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) view.findViewById(R.id.detail_fragment_text_view))
                    .setText(forecastStr);
        }*/

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate share menu
        inflater.inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        setShareIntent(createForecastShareIntent());

        //return true;
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

        if (id == R.id.menu_item_share) {

            createForecastShareIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createForecastShareIntent() {
        Intent intent = getActivity().getIntent();
        String weather = intent.getStringExtra(ForecastFragment.WEATHER);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, weather + FORECAST_SHARE_HASHTAG);
        //startActivity(sendIntent);
        return sendIntent;
    }


    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

}
