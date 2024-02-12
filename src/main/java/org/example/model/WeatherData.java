package org.example.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherData {
    private String message;
    private String cod;
    private int count;
    private List<City> list;

    // getters and setters
}

