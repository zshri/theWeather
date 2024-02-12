package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {


    @JsonProperty("id")
    private int id;

    @JsonProperty("locationId")
    private Long locationId;


    @JsonProperty("name")
    private String name;
    @JsonProperty("coord")
    private Coord coord;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("dt")
    private int dt;
    @JsonProperty("wind")
    private Wind wind;
    @JsonProperty("sys")
    private Sys sys;
    @JsonProperty("rain")
    private Object rain;
    @JsonProperty("snow")
    private Object snow;
    @JsonProperty("clouds")
    private Clouds clouds;
    @JsonProperty("weather")
    private List<Weather> weather;

    // getters and setters
}
