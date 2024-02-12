package org.example.servlets;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServletUtils {

    public static void sendNotAuthorized(HttpServletResponse res) throws IOException {
        final int unauthorized = 401;
        res.setStatus(unauthorized);
        res.setContentType("application/json");
        res.getWriter().write("{\"Error\": \"Not authorized\"}");
        res.getWriter().close();
    }

}