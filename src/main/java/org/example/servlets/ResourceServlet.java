package org.example.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
* todo
*  1) Почему при переходе по /resource скачиввает файл с библиотеками?
* */

@WebServlet(name = "ResourceServlet", urlPatterns = {"/resources/*"})
public class ResourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resourcePath = request.getPathInfo();

        // Определение типа ресурса (css, js и т.д.) на основе его расширения
        String contentType = getServletContext().getMimeType(resourcePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Установка типа содержимого для ответа
        response.setContentType(contentType);

        // Чтение ресурса из файловой системы или из пакета классов (в зависимости от вашего предпочтения)
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);

        // Проверка, что ресурс был найден
        if (inputStream == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Запись ресурса в выходной поток ответа
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        // Закрытие потоков
        inputStream.close();
        outputStream.close();
}
}