package org.example.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.DaoException;
import org.example.service.WeatherService;

import java.io.IOException;


@WebServlet(urlPatterns = {"/location/add", "/location/remove"})
public class LocationServlet extends HttpServlet {

    private WeatherService weatherService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.weatherService = (WeatherService) getServletContext().getAttribute("weatherService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        String servletPath = request.getServletPath();

        Long userId =(Long) request.getSession().getAttribute("userId");
        String name = request.getParameter("name");
        String locationId = request.getParameter("id");
        String latParam = request.getParameter("lat");
        String lonParam = request.getParameter("lon");
        Double lat = null;
        Double lon = null;
        if (latParam != null){
            try {
                lat = Double.parseDouble(latParam);
                lon = Double.parseDouble(lonParam);
            } catch (NumberFormatException e) {
                // Обработка ошибки
            }
        }

        if ("/location/add".equals(servletPath)) {
            try {
                weatherService.addLocation(name, userId, lat, lon);
                response.sendRedirect(request.getContextPath() + "/");
            } catch (DaoException e) {
//                response.sendRedirect(request .getContextPath() + "/?error=dao");
            }
        }

        if ("/location/remove".equals(servletPath)) {
            if (locationId != null) {
                try {
                    weatherService.removeLocation(locationId, userId);
                } catch (DaoException e) {
//                    ,n,n
                }
//                response.sendRedirect(request.getContextPath() + "/");
            } else {
                System.out.println("location id null");
                response.sendRedirect(request.getContextPath() + "/");
            }

        }
    }
}

