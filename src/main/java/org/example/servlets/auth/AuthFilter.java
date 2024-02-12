package org.example.servlets.auth;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.exception.DaoException;
import org.example.model.User;
import org.example.service.UserService;

import java.io.IOException;


@WebFilter(urlPatterns = {"/*"})
//@WebFilter(servletNames = {"MainServlet"})
public class AuthFilter implements Filter {

    private FilterConfig filterConfig;
    private UserService userService;
    private User user;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.userService = (UserService) filterConfig.getServletContext().getAttribute("userService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = httpRequest.getServletPath();
        HttpSession session = httpRequest.getSession(false);

        // todo почему два раза проигрывается?
        /*
        URL-паттерны: Если у вас есть несколько URL-паттернов в вашем фильтре и URL запроса соответствует нескольким паттернам, фильтр может быть
        вызван несколько раз для каждого паттерна.Обновление и работа сессий: Если при первом запуске приложения создаются и обновляются сессии
        пользователей, фильтр может вызываться два раза для каждой из таких операций.

        например
        Пошла проверка к базе ид: BB656FA96FAEBB417050C83D132EFC6E
        Пошла проверка к базе ид: B321E7B171B74FDEB7F4F4DBABD47C98
        */

        if (!url.contains("resources")) {

            if (session == null) {
                session = httpRequest.getSession();
            }

            String auth = (String)session.getAttribute("auth");

            if ( auth == null) {
                System.out.println("Проверка к базе ид: " + session.getId());
                if (checkAuthentication(session.getId())) {
                    System.out.println("checkAuthentication true");
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userName", user.getLogin());
                    session.setAttribute("auth", "true");
                } else {
                    System.out.println("checkAuthentication false");
                }
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkAuthentication(String sessionId) {
        try {
            this.user = userService.getUserBySession(sessionId);
        } catch (DaoException e) {
            e.printStackTrace(); // todo
        }
        return this.user != null;
    }

    @Override
    public void destroy() {
    }

}