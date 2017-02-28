package com.golubroman.golub.weatherv201;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.golubroman.golub.weatherv201.myretrofit.RetrofitModule;
import com.golubroman.golub.weatherv201.myretrofit.WeatherPOJO;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationFragment.LocationFragmentListener{
    ImageView status_icon, search_button;
    EditText search_field;
    TextView settings, status_day, status_humidity, status_pressure,
            status_wind, status_humidity_text, status_pressure_text,
            status_wind_text, status_hightemp, status_lowtemp, status_status;
    FrameLayout settings_layout;
    ArrayList<ElementListView> elementListViewList;
    String current_city;
    ProgressBar progressBar;
    ListView listView;
    Toolbar toolbar;
    android.app.FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    LocationFragment locationFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        fragmentTransaction.add(R.id.activity_main, locationFragment);
        fragmentTransaction.commit();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        changeFont();
    }

    @Override
    public void getCity(String city) {
        this.current_city = city;
        getResponse();
    }


    void setStatusImage(ElementListView elementListView) {
//        ElementListView elementListView = elementListViewList.get(position);
        status_day.setText(elementListView.getDate());
        status_humidity.setText(elementListView.getHumidity());
        status_pressure.setText(elementListView.getPressure());
        status_wind.setText(elementListView.getWind());
        status_hightemp.setText(elementListView.getMaxtemp());
        status_lowtemp.setText(elementListView.getMintemp());
        status_status.setText(elementListView.getStatus());
        int id = Integer.parseInt(elementListView.getId());
        if (id >= 200 && id <= 232) status_icon.setImageResource(R.drawable.thunder_1);
        else if ((id >= 300 && id <= 321) || (id >= 500 && id <= 532))
            status_icon.setImageResource(R.drawable.rain_1);
        else if (id >= 600 && id <= 622) status_icon.setImageResource(R.drawable.snow_1);
        else if (id >= 701 && id <= 781) status_icon.setImageResource(R.drawable.wind_1);
        else if (id >= 801 && id <= 804) status_icon.setImageResource(R.drawable.cloud_1);
        else if (id == 800) status_icon.setImageResource(R.drawable.clearsun_1);
    }
    void initialization(){
        listView = (ListView) findViewById(R.id.listview);
        settings = (TextView) findViewById(R.id.settings);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        search_field = (EditText) findViewById(R.id.search_field);
        search_button = (ImageView) findViewById(R.id.search_button);
        status_icon = (ImageView) findViewById(R.id.status_icon);
        status_day = (TextView) findViewById(R.id.status_day);
        status_humidity = (TextView) findViewById(R.id.status_humidity);
        status_pressure = (TextView) findViewById(R.id.status_pressure);
        status_wind = (TextView) findViewById(R.id.status_wind);
        status_humidity_text = (TextView) findViewById(R.id.status_humidity_text);
        status_pressure_text = (TextView) findViewById(R.id.status_pressure_text);
        status_wind_text = (TextView) findViewById(R.id.status_wind_text);
        status_hightemp = (TextView) findViewById(R.id.status_hightemp);
        status_lowtemp = (TextView) findViewById(R.id.status_lowtemp);
        status_status = (TextView) findViewById(R.id.status_status);
        settings_layout = (FrameLayout) findViewById(R.id.settings_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        locationFragment = new LocationFragment();
        current_city = "odessa";
        settings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(search_field.getText().toString()) || search_field.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Incorrect city`s name! Try again.", Toast.LENGTH_SHORT).show();
                } else {
                    current_city = search_field.getText().toString();
                    getResponse();
                }
            }
        });
    }
    void getResponse(){
        RetrofitModule.buildApi();
        new PutTask().execute();

    }

    private void init(final ArrayList<ElementListView> elementListViewList) {

        listView.setAdapter(new ListAdapter(MainActivity.this,
                elementListViewList));
        setStatusImage(elementListViewList.get(0));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setStatusImage(elementListViewList.get(i));
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    class PutTask extends AsyncTask<Void, Void, WeatherPOJO>{
        @Override
        protected WeatherPOJO doInBackground(Void... voids) {
            Response<WeatherPOJO> response;
            try {
                response =  RetrofitModule.getApi().getData(current_city, SettingsActivity.unitsNameText, "16", "28e909fe816297eafabdac9118bfa097").execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return response.body();
        }

        @Override
        protected void onPostExecute(WeatherPOJO weatherPOJO) {
            super.onPostExecute(weatherPOJO);
            init(RetrofitModule.putParsedData(SettingsActivity.unitsNameText, weatherPOJO));
        }
    }
    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/manteka.ttf");
        settings.setTypeface(manteka);
        status_day.setTypeface(manteka);
        status_humidity.setTypeface(manteka);
        status_pressure.setTypeface(manteka);
        status_wind.setTypeface(manteka);
        status_humidity_text.setTypeface(manteka);
        status_pressure_text.setTypeface(manteka);
        status_wind_text.setTypeface(manteka);
        status_hightemp.setTypeface(manteka);
        status_lowtemp.setTypeface(manteka);
        status_status.setTypeface(manteka);
    }
}