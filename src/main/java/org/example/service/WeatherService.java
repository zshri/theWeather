package org.example.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.ApiClientException;
import org.example.dao.WeatherClient;
import org.example.dao.LocationDAO;
import org.example.exception.ApiLimitExceededException;
import org.example.exception.DaoException;
import org.example.model.City;
import org.example.model.Coord;
import org.example.model.Location;
import org.example.model.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WeatherService {

    //    todo Refactor to static?
    private final WeatherClient weatherClient;
    private final RateLimiter rateLimiter;
    private final ObjectMapper objectMapper;
    private final LocationDAO locationDAO;

    public static WeatherData weatherDataCache;

    public WeatherService() {
        this.weatherClient = new WeatherClient();
        this.rateLimiter = new RateLimiter();
        this.objectMapper = new ObjectMapper();
        this.locationDAO = new LocationDAO();
    }

    public void addLocation(String name, Long userId, Double lat, Double lon) throws DaoException {
        Location location = new Location();
        location.setName(name);
        location.setUserId(userId);
        location.setLatitude(lat);
        location.setLongitude(lon);
        locationDAO.save(location);
    }

    public void removeLocation(String locationId, Long userId) throws DaoException {
        Long locId = Long.parseLong(locationId);
        Location location = locationDAO.getLocation(locId);
        if (Objects.equals(location.getUserId(), userId)) {
            locationDAO.delete(locId);
        }
    }

    public WeatherData getPublicCacheWeatherData() {
        return weatherDataCache;
    }

    //     todo -add cache or personal ratelimeter , 2) exception handling on stream
    public WeatherData getPersonalWeatherDate(Long userId) throws ApiClientException, JsonProcessingException, ApiLimitExceededException, DaoException {
        List<Location> listLocationByUserId = locationDAO.findListLocationByUserId(userId);
        WeatherData weatherData = new WeatherData();
        ArrayList<City> cityArrayList = new ArrayList<>();

        for (Location location : listLocationByUserId) {
            City city = mapLocationToCity(location);
            cityArrayList.add(city);
        }

        weatherData.setList(cityArrayList);

        return weatherData;
    }

    public City mapLocationToCity(Location location) throws ApiClientException, ApiLimitExceededException, JsonProcessingException {
        if (rateLimiter.isBlocked()) {
            throw new ApiLimitExceededException("api limit");
        }
        City city = null;
        Optional<String> respByCoordQuery = weatherClient.findByCoordQuery(location.getLatitude().toString(), location.getLongitude().toString());
        if (respByCoordQuery.isPresent()) {
            city = objectMapper.readValue(respByCoordQuery.get(), City.class);
            city.setLocationId(location.getId());
        }
        return city;
    }

    public WeatherData getWeatherDataByQuery(String query) throws JsonProcessingException, ApiClientException, ApiLimitExceededException {
        if (rateLimiter.isBlocked()) {
            throw new ApiLimitExceededException("api limit");
        }
        if (isValidCoordinates(query)) {
            Coord coord = parseCoord(query);
            Optional<String> json = weatherClient.findByCoordQuery(Double.toString(coord.getLat()), Double.toString(coord.getLon()));
            WeatherData weatherData = null;
            if (json.isPresent()) {
                City city = objectMapper.readValue(json.get(), City.class);
                weatherData = new WeatherData();
                ArrayList<City> cityArrayList = new ArrayList<City>();
                cityArrayList.add(city);
                weatherData.setList(cityArrayList);
            }
            return weatherData;
        } else {
            Optional<String> json = weatherClient.findByCityQuery(query);
            WeatherData weatherData = null;
            if (json.isPresent()) {
                weatherData = objectMapper.readValue(json.get(), WeatherData.class);
            }
            return weatherData;
        }
    }

    private Coord parseCoord(String query) {
        String[] parts = query.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid coordinates format");
        }
        double lat = Double.parseDouble(parts[0].trim());
        double lon = Double.parseDouble(parts[1].trim());
        Coord coord = new Coord();
        coord.setLat(lat);
        coord.setLon(lon);
        return coord;
    }

    private boolean isValidCoordinates(String query) {
        String regex = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";
        return Pattern.matches(regex, query);
    }

}
