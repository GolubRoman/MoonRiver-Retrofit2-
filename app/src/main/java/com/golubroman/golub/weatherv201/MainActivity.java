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

import com.golubroman.golub.weatherv201.Settings.SettingsActivity;
import com.golubroman.golub.weatherv201.WeatherList.ElementListView;
import com.golubroman.golub.weatherv201.WeatherList.ListAdapter;
import com.golubroman.golub.weatherv201.myretrofit.RetrofitModule;
import com.golubroman.golub.weatherv201.myretrofit.WeatherPOJO;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationFragment.LocationFragmentListener{
    @BindView(R.id.status_icon) ImageView status_icon;
    @BindView(R.id.search_button) ImageView search_button;

    @BindView(R.id.search_field) EditText search_field;

    @BindView(R.id.settings) TextView settings;
    @BindView(R.id.status_day) TextView status_day;
    @BindView(R.id.status_humidity) TextView status_humidity;
    @BindView(R.id.status_pressure) TextView status_pressure;
    @BindView(R.id.status_wind) TextView status_wind;
    @BindView(R.id.status_humidity_text) TextView status_humidity_text;
    @BindView(R.id.status_pressure_text) TextView status_pressure_text;
    @BindView(R.id.status_wind_text) TextView status_wind_text;
    @BindView(R.id.status_lowtemp) TextView status_lowtemp;
    @BindView(R.id.status_hightemp) TextView status_hightemp;
    @BindView(R.id.status_status) TextView status_status;

    @BindView(R.id.settings_layout) FrameLayout settings_layout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressbar) ProgressBar progressBar;
    @BindView(R.id.listview) ListView listView;

    private String current_city  = "odessa";
    final String incorrectCity = "Incorrect city`s name! Try again.";
    final String typeface = "fonts/manteka.ttf";
    final String cnt = "16";
    final String appId = "28e909fe816297eafabdac9118bfa097";

    private android.app.FragmentManager fragmentManager;
    private android.app.FragmentTransaction fragmentTransaction;
    private LocationFragment locationFragment;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        locationFragment = new LocationFragment();
        fragmentTransaction.add(R.id.activity_main, locationFragment);
        fragmentTransaction.commit();



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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
                    Toast.makeText(MainActivity.this, incorrectCity, Toast.LENGTH_SHORT).show();
                } else {
                    current_city = search_field.getText().toString();
                    getResponse();
                }
            }
        });

        changeFont();
    }

    @Override
    public void getCity(String city) {
        this.current_city = city;
        getResponse();
    }


    void setStatusImage(ElementListView elementListView) {
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
                response =  RetrofitModule.getApi().getData(current_city, SettingsActivity.unitsSubtitle, cnt, appId).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return response.body();
        }

        @Override
        protected void onPostExecute(WeatherPOJO weatherPOJO) {
            super.onPostExecute(weatherPOJO);
            init(RetrofitModule.putParsedData(SettingsActivity.unitsSubtitle, weatherPOJO));
        }
    }
    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(this.getResources().getAssets(), typeface);
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