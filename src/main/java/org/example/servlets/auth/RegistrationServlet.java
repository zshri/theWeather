package org.example.servlets.auth;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.exception.DaoException;
import org.example.model.User;
import org.example.service.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(value = "/registration")
public class RegistrationServlet extends HttpServlet {

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
        templateEngine.process("RegistrationPage", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String rpassword = req.getParameter("rpassword");

        IWebExchange webExchange = this.app.buildExchange(req, resp);
        WebContext context = new WebContext(webExchange, req.getLocale());

        try {
            if (!password.equals(rpassword) && !userService.isValidPassword(password)) {
                String alert = "Incorrect password!";
                context.setVariable("alert", alert);
                templateEngine.process("RegistrationPage", context, resp.getWriter());
            } else if (userService.checkLoginExists(login)) {
                String alert = "Username already exist!";
                context.setVariable("alert", alert);
                templateEngine.process("RegistrationPage", context, resp.getWriter());
            } else {
                // todo return User?
                userService.createUser(login, password);
                User user = userService.login(login, password);
                HttpSession session = req.getSession();
                userService.saveSession(session.getId(), user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getLogin());
                session.setAttribute("auth", "true");
                resp.sendRedirect(req.getContextPath() + "/");
            }
        } catch (DaoException e) {
            context.setVariable("alert", e.getMessage());
            templateEngine.process("RegistrationPage", context, resp.getWriter());
        }


    }
}