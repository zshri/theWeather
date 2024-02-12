package org.example.config;

import org.example.exception.DaoException;
import org.example.service.UserService;
import org.example.service.WeatherService;

public class InitData {

    private final UserService userService;
    private final WeatherService weatherService;

    public InitData(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    public void init(){
        userService.createUser("admin", "123456");

        try {
            weatherService.addLocation("London,GB", 1L, 51.5085, -0.1257);
            weatherService.addLocation("Moscow,RU", 1L, 55.7522, 37.6156);
            weatherService.addLocation("Tokyo,JP", 1L, 35.6895, 139.6917);
            weatherService.addLocation("New York City,US", 1L, 40.7143, -74.006);
        } catch (DaoException e) {

        }

    }
}
