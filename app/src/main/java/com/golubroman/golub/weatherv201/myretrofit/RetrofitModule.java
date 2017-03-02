package com.golubroman.golub.weatherv201.myretrofit;

import com.golubroman.golub.weatherv201.WeatherList.ElementListView;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitModule{
    private static OpenWeatherApi openWeatherApi;
    private static Retrofit retrofit;
    final static String cnt = "16";
    public static void buildApi(){
        retrofit = new Retrofit.Builder().
                baseUrl("http://api.openweathermap.org/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        openWeatherApi = retrofit.create(OpenWeatherApi.class);
    }
    public static ArrayList<ElementListView> putParsedData(String units, WeatherPOJO weatherPOJO){
        ArrayList<ElementListView> elementListViewList = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(cnt); i++){
            elementListViewList.add(new ElementListView());
            ListPOJO listElement = weatherPOJO.getList().get(i);
            String date = listElement.getDt().toString();
            long unixDate = Long.valueOf(date) * 1000L;
            date = new Date(Long.parseLong(String.valueOf(unixDate))).toString();
            date = date.substring(0, 10);
            elementListViewList.get(i).setDate(date);
            elementListViewList.get(i).setHumidity(listElement.getHumidity().toString() + "%");
            elementListViewList.get(i).setPressure(listElement.getPressure().toString() + "hPa");
            String wind = roundString(listElement.getSpeed().toString());
            String mintemp = roundString(listElement.getTemp().getMin().toString());
            String maxtemp = roundString(listElement.getTemp().getMax().toString());
            if(units.equals("Metric")){
                mintemp += " \u00B0"+"C";
                maxtemp += " \u00B0"+"C";
                wind += " km/h SW";
            }
            else if(units.equals("Imperial")){
                mintemp += " \u00B0"+"F";
                maxtemp += " \u00B0"+"F";
                wind += " mph SW";
            }
            elementListViewList.get(i).setWind(wind);
            elementListViewList.get(i).setMintemp(mintemp);
            elementListViewList.get(i).setMaxtemp(maxtemp);
            elementListViewList.get(i).setStatus(listElement.getWeather().get(0).getMain());
            elementListViewList.get(i).setId(listElement.getWeather().get(0).getId().toString());
        }
        return elementListViewList;
    }
    private static String roundString(String s) {
        double d = Double.parseDouble(s);
        long l = Math.round(d);
        return Integer.toString((int)l);
    }
    public static OpenWeatherApi getApi(){
        return openWeatherApi;
    }
}

