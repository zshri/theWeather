package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("coord")
    private Coord coord;

    @JsonProperty("weather")
    private Weather[] weather;

    @JsonProperty("base")
    private String base;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("visibility")
    private int visibility;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("rain")
    private Rain rain;

    @JsonProperty("clouds")
    private Clouds clouds;

    @JsonProperty("dt")
    private long dt;

    @JsonProperty("sys")
    private Sys sys;

    @JsonProperty("timezone")
    private int timezone;

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("cod")
    private int cod;

    // getters and setters

}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Weather {
    @JsonProperty("id")
    private int id;

    @JsonProperty("main")
    private String main;

    @JsonProperty("description")
    private String description;

    @JsonProperty("icon")
    private String icon;

    // getters and setters
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Main {
    @JsonProperty("temp")
    private double temp;

    @JsonProperty("feels_like")
    private double feelsLike;

    @JsonProperty("temp_min")
    private double tempMin;

    @JsonProperty("temp_max")
    private double tempMax;

    @JsonProperty("pressure")
    private int pressure;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("sea_level")
    private int seaLevel;

    @JsonProperty("grnd_level")
    private int grndLevel;

    // getters and setters
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Wind {
    @JsonProperty("speed")
    private double speed;

    @JsonProperty("deg")
    private int deg;

    @JsonProperty("gust")
    private double gust;

    // getters and setters
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Rain {
    @JsonProperty("1h")
    private double oneHour;

    // getters and setters
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Clouds {
    @JsonProperty("all")
    private int all;

    // getters and setters
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class Sys {
    @JsonProperty("type")
    private int type;

    @JsonProperty("id")
    private long id;

    @JsonProperty("country")
    private String country;

    @JsonProperty("sunrise")
    private long sunrise;

    @JsonProperty("sunset")
    private long sunset;

    // getters and setters
}