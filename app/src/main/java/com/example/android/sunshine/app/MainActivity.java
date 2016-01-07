package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Basically enabling appbar... actionbar is deprecated
        Toolbar myToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);

        // Create actionBar then set it to show up in MainActivity
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.i("Null Pointer: ", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Inflate share menu
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);


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
            //create new intent as an address to go to settingsactivity.class
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_launch_map) {
            // call method that creates URI then pass it via intent to open up google maps to view location
            getPreferredLocation();
            return true;
        }

        // ShareIntent that sends the weather info to an app of your preference
        if (id == R.id.menu_item_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPreferredLocation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String zipcode = sharedPreferences.getString("location","");
        //Toast.makeText(MainActivity.this, zipcode, Toast.LENGTH_SHORT).show();
        String gmaps = "http://maps.google.com/maps?q=";
        //Uri gmmIntentUri = Uri.parse(gmaps + zipcode); //"geo:37.7749,-122.4194"); -- my way of doing it
        //They used the buildUpon() method to construct query then passed it into the intent as an ACTION_VIEW
        Uri gmmIntentUri = Uri.parse(gmaps).buildUpon()
                            .appendQueryParameter("q",zipcode)
                            .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(mapIntent);
        }
    }
}
