package org.example.servlets.auth;

import java.io.*;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.exception.DaoException;
import org.example.model.User;
import org.example.service.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JakartaServletWebApplication app;
    private UserService userService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.app = (JakartaServletWebApplication) getServletContext().getAttribute("app");
        this.templateEngine = (TemplateEngine) getServletContext().getAttribute("engine");
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IWebExchange webExchange = this.app.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, request.getLocale());
        templateEngine.process("LoginPage", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        IWebExchange webExchange = this.app.buildExchange(req, resp);
        WebContext context = new WebContext(webExchange, req.getLocale());

        User user = null;
        try {
            user = userService.login(login, password);

            if (!userService.checkLoginExists(login)) {
                String alert = "Username not exist!";
                context.setVariable("alert", alert);
                templateEngine.process("LoginPage", context, resp.getWriter());
            } else if (user == null) {
                String alert = "Incorrect password!";
                context.setVariable("alert", alert);
                templateEngine.process("LoginPage", context, resp.getWriter());
            }

            if (user != null) {
                HttpSession session = req.getSession();
                userService.saveSession(session.getId(), user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getLogin());
                session.setAttribute("auth", "true");
//            String referer = req.getHeader("Referer");
                resp.sendRedirect(req.getContextPath() + "/");
            }

        } catch (DaoException e) {
            context.setVariable("alert", e.getMessage());
            templateEngine.process("LoginPage", context, resp.getWriter());
        }

    }
}