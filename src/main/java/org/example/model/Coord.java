package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coord {
    @JsonProperty("lon")
    private double lon;

    @JsonProperty("lat")
    private double lat;

    // getters and setters
}
