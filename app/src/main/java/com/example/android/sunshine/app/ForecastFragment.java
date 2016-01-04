package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.android.sunshine.app.FetchWeatherTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private String[] weather;
    ListView listView;
    ArrayAdapter<String> adapter;
    public static final String WEATHER = "";

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize ArrayList named fakeData and add the values from ArrayList with forecast data into it
        List<String> fakeData = new ArrayList<>();
        fakeData.addAll(createArray());
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

    //create ArrayList<String> and add the String[] list into it
    private List<String> createArray()
    {
        List<String> forecastList = new ArrayList<String>(Arrays.asList(createStringArray()));
        return forecastList;
    }

    //create String[] list and initializing with values for forecast
    private String[] createStringArray()
    {
        String[] forecastStringArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68",
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68",
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68",
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68"
        };
        return forecastStringArray;
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
    }

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
        String zipcode = sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        Toast.makeText(getContext(), zipcode, Toast.LENGTH_SHORT).show();
        fetchWeatherTask.execute(zipcode);
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

/*    private String grabWeatherData(String zipcode) {
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            Uri.Builder builder = new Uri.Builder();
            final String DOMAIN = "api.openweathermap.org";
            final String PATH = "data/2.5/forecast/daily";
            final String PARAM_LOCATION = "q";
            final String PARAM_MODE = "mode";
            final String PARAM_UNITS = "units";
            final String PARAM_DAYS = "cnt";
            final String PARAM_API = "appid";
            final String API = "d83b81600fa571f1067f59e396e38ada";

            builder.scheme("http")
                    .authority(DOMAIN)
                    .appendPath(PATH)
                    .appendQueryParameter(PARAM_LOCATION, zipcode)
                    .appendQueryParameter(PARAM_MODE,"json")
                    .appendQueryParameter(PARAM_UNITS, "metric")
                    .appendQueryParameter(PARAM_DAYS, "7")
                    .appendQueryParameter(PARAM_API, API);
            URL url = new URL(builder.build().toString());
            Log.i("URL", url.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }*/
}
