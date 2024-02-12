package org.example.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.dao.UserDAO;
import org.example.exception.ApiClientException;
import org.example.exception.ApiLimitExceededException;
import org.example.exception.DaoException;
import org.example.service.SchedulerService;
import org.example.service.UserService;
import org.example.service.WeatherService;
import org.hibernate.SessionFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


@WebListener
public class AppServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        JakartaServletWebApplication app = JakartaServletWebApplication.buildApplication(servletContextEvent.getServletContext());
        context.setAttribute("app", app);

        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(app);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        context.setAttribute("resolver", resolver);

        TemplateEngine engine = new TemplateEngine();
        engine.addTemplateResolver(resolver);
        context.setAttribute("engine", engine);

        SessionFactory sessionFactory = HibernateConfig.getSessionFactory();
        context.setAttribute("sessionFactory", sessionFactory);

        UserDAO userDAO = new UserDAO();
        context.setAttribute("userDAO", userDAO);

        UserService userService = new UserService();
        context.setAttribute("userService", userService);

        SchedulerService schedulerService = new SchedulerService();
        schedulerService.start();
        context.setAttribute("schedulerService", schedulerService);

        WeatherService weatherService = new WeatherService();
        context.setAttribute("weatherService", weatherService);

        InitData initData = new InitData(userService, weatherService);
        initData.init();

        try {
            WeatherService.weatherDataCache = weatherService.getPersonalWeatherDate(1L);
        } catch (ApiClientException | DaoException | ApiLimitExceededException | JsonProcessingException e) {

        }

        System.out.println("Контекст приложения инициализирован");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Контекст приложения уничтожен");
    }
}

