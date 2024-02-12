package org.example.dao;

import org.example.config.Env;
import org.example.exception.ApiClientException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class WeatherClient {

    private final HttpClient httpClient;

    public WeatherClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(2000))
                .build();
    }

    public Optional<String> findByCityQuery(String query) throws ApiClientException {
        String url = "https://api.openweathermap.org/data/2.5/find?q=" + query + "&type=like&units=metric&sort=population&cnt=30"  + "&appid=" + Env.apikey;
        Optional<String> responseBody = Optional.empty();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiClientException("HTTP error: " + response.statusCode());
            }
            responseBody = Optional.of(response.body());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ApiClientException("api down");
        }
        return responseBody;
    }

    public Optional<String> findByCoordQuery(String lat, String lon) throws ApiClientException {
        String url = "https://api.openweathermap.org/data/2.5/weather?units=metric&lat=" + lat + "&lon=" + lon + "&appid=" + Env.apikey;
        Optional<String> responseBody = Optional.empty();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiClientException("HTTP error: " + response.statusCode());
            }
            responseBody = Optional.of(response.body());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ApiClientException("API down");
        }
        return responseBody;
    }

}
