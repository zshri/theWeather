package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.dao.SessionDAO;
import org.example.exception.ApiClientException;
import org.example.exception.ApiLimitExceededException;
import org.example.exception.DaoException;
import org.example.model.WeatherData;

import java.util.Timer;
import java.util.TimerTask;

public class SchedulerService {

    private final Timer scheduler;
    private final SessionDAO sessionDAO;
    private final WeatherService weatherService;

    public SchedulerService() {
        scheduler = new Timer();
        sessionDAO = new SessionDAO();
        weatherService = new WeatherService();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(new DatabaseTask(), 0,  24 * 60 * 60 * 1000);
        scheduler.scheduleAtFixedRate(new MainWeatherTask(), 0,   2 * 60 * 1000);
    }

    public void stop() {
        scheduler.cancel();
    }


    private class DatabaseTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("run DatabaseTask");
            sessionDAO.deleteExpiredRecords();
        }
    }

    private class MainWeatherTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("run MainWeatherTask");
            // todo будет ли опнуляться переменая если ошибка апи?
            try {
                WeatherService.weatherDataCache = weatherService.getPersonalWeatherDate(1L);
            } catch (ApiClientException | JsonProcessingException | ApiLimitExceededException | DaoException e) {

            }
        }
    }



}
