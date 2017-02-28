package com.golubroman.golub.weatherv201;

/**
 * Created by User on 18.01.2017.
 */
public class ElementListView {
    private String id, date, humidity, pressure, wind, mintemp, maxtemp, status;

    public String getId() { return id; }

    public void setId(String id){ this.id = id; }

    public String getDate() {
        return date;
    }

    public void setDate(String date){ this.date = date; }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity){ this.humidity = humidity; }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure){ this.pressure = pressure; }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind){ this.wind = wind; }

    public String getMintemp() { return mintemp; }

    public void setMintemp(String mintemp){ this.mintemp = mintemp; }

    public String getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(String maxtemp){ this.maxtemp = maxtemp; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){ this.status = status; }
}
