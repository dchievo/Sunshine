package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private String[] weather;
    ListView listView;
    ArrayAdapter<String> adapter;
    public static final String WEATHER = "";
    private SharedPreferences mShareActionProvider;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize ArrayList named fakeData and add the values from ArrayList with forecast data into it
        List<String> fakeData = new ArrayList<>();
        //fakeData.addAll(createArray());
        //created a View named rootView by inflating the layout fragment_main.xml.
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //created a listView by casting a view from the inflated layout fragment_main that was assigned
        //to rootView variable
        listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        //activity_main now shows the declared essentials for the toolbars/options etc.
        //content_main reflects the customized views/layout that you designate
        //fragment_main is a piece of the layout that you want included that is separate from the content_main
        //list_item_forecast is a custom layout that reflects only one textview that is the default view for the whole layout

        //create arrayadapter
        listView.setAdapter(createArrayAdapter(fakeData));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //I did it this way initially.  Declared a view and grabbed the textview and assigned
                //it the getText() from the textview.
                TextView v = (TextView) view.findViewById(R.id.listview_forecast_textview);

                //This is the way they did it in the video.  Basically within the adapter value itself
                //grab the String value located in position (ArrayAdapter) and assign it to weather.
                String weather = adapter.getItem(position);

                //Toast is based off of Context of Activity, String value that you want to show on screen
                //Toast.LENGTH_SHORT to show
                //Toast.makeText(getActivity().getApplicationContext(), weather , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(WEATHER, weather);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private ArrayAdapter<String> createArrayAdapter(List<String> fakeData)
    {
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.listview_forecast_textview, new ArrayList<String>());

        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
/*
        MenuItem item = menu.findItem(R.id.action_launch_map);*/
/*        mShareActionProvider = (SharedPreferences) item.getActionProvider();*/
    }

/*    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh)
        {
            FetchWeatherTask fetchWeatherTask = updateWeather();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private FetchWeatherTask updateWeather() {
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        // Assigning zipcode/weather string variable the value from sharedPreferences via (value if there is one saved in sharedpref and if not the default
        // value is given (which you defined within the pref_general menu layout
        String zipcode = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String temperatureType = sharedPreferences.getString(getString(R.string.pref_temp_key),getString(R.string.default_temperature));
        //Used to test String variable
        //Toast.makeText(getContext(), zipcode, Toast.LENGTH_SHORT).show();

        fetchWeatherTask.execute(zipcode, temperatureType);
        try {
            //assigns JSON values from fetchWeatherTask.get() to weather variable
            weather = fetchWeatherTask.get();
            //ArrayList<String> refreshedWeather = new ArrayList<String>(Arrays.asList(weather));
            //only way this worked was passing in a String[] and not as a ArrayList that way it
            //was not casted into a Non-modifiable List

            // clears adapter of all set values
            adapter.clear();

            // adds weather data grabbed by FetchWeatherTask AsyncTask onPostExecute().get() string data
            adapter.addAll(weather);

            // notifies adapter that data set was changed/updated.  observers automatically call
            //this method so it's unnecessary to call manually
            //adapter.notifyDataSetChanged();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fetchWeatherTask;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        updateWeather();
    }

}
