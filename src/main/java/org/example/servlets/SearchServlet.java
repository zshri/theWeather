package org.example.servlets;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.exception.ApiClientException;
import org.example.exception.ApiLimitExceededException;
import org.example.model.WeatherData;
import org.example.service.WeatherService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JakartaServletWebApplication app;
    private WeatherService weatherService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.app = (JakartaServletWebApplication) getServletContext().getAttribute("app");
        this.templateEngine = (TemplateEngine) getServletContext().getAttribute("engine");
        this.weatherService = (WeatherService) getServletContext().getAttribute("weatherService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        IWebExchange webExchange = this.app.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, request.getLocale());

        HttpSession session = request.getSession();
        Long userId = (Long)session.getAttribute("userId");
        String userName = (String)session.getAttribute("userName");

        String query = request.getParameter("q");
        System.out.println(query);

        if (query != null) {

            WeatherData searchWeatherData = null;
            try {
                searchWeatherData = weatherService.getWeatherDataByQuery(query);
            } catch (ApiClientException | ApiLimitExceededException e) {
                context.setVariable("exception", e.getMessage());
            } catch (JsonProcessingException e) {
                context.setVariable("exception", "JsonProcessingException");
            } catch (Exception e) {
                context.setVariable("exception", e.getMessage());
            }

            context.setVariable("userId", userId);
            context.setVariable("userName", userName);
            context.setVariable("weatherData", searchWeatherData);
            context.setVariable("userQuery", query);
        }

        templateEngine.process("SearchPage", context, response.getWriter());
    }

}