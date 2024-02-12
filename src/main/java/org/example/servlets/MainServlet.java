package org.example.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.config.SessionCounter;
import org.example.exception.ApiClientException;
import org.example.exception.ApiLimitExceededException;
import org.example.model.WeatherData;
import org.example.service.WeatherService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "MainServlet", value = "")
public class MainServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JakartaServletWebApplication app;
    private WeatherService weatherService;


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.app = (JakartaServletWebApplication) getServletContext().getAttribute("app");
        this.templateEngine = (TemplateEngine) getServletContext().getAttribute("engine");
        this.weatherService = (WeatherService) getServletContext().getAttribute("weatherService");

        System.out.println("main servlet up");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        IWebExchange webExchange = this.app.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, request.getLocale());

        HttpSession session = request.getSession();
        Long userId =(Long) session.getAttribute("userId");
        String userName =(String) session.getAttribute("userName");

        if (userId != null) {
            context.setVariable("userId", userId);
            context.setVariable("userName", userName);
            WeatherData personalWeatherDate = null;
            try {
                personalWeatherDate = weatherService.getPersonalWeatherDate(userId);
            } catch (ApiClientException | ApiLimitExceededException e) {
                context.setVariable("exception", e.getMessage());
            } catch (JsonProcessingException e) {
                context.setVariable("exception", "JsonProcessingException");
            } catch (Exception e) {
                context.setVariable("exception", e.getMessage());
            }
            context.setVariable("weatherData", personalWeatherDate );
        } else {
            WeatherData mockData = weatherService.getPublicCacheWeatherData();
            context.setVariable("weatherData", mockData );
        }

        context.setVariable("searchPlaceholder", "enter the city name or coordinates format 59.9342, 30.3351");

        int activeSessions = SessionCounter.getActiveSessions();
        System.out.println("count session " + activeSessions);

        templateEngine.process("Main", context, response.getWriter());
    }

}
