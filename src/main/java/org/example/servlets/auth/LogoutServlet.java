package org.example.servlets.auth;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.service.UserService;

import java.io.IOException;


@WebServlet(value = "/logout")
public class LogoutServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        userService.deleteSession(request.getSession().getId());

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JSESSIONID")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        HttpSession session = request.getSession();
        session.invalidate();

        response.sendRedirect("/theWeather/");
    }
}