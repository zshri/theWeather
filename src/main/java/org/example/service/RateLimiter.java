package org.example.service;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RateLimiter {
    private static final int LIMIT = 60; // лимит запросов в минуту
    private static final long ONE_MINUTE = 60 * 1000; // 1 минута в миллисекундах

    private static Map<String, Integer> requestCountMap = new HashMap<>();
    private static Map<String, Long> lastRequestTimeMap = new HashMap<>();


    public RateLimiter() {
    }

    public static synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();

        // Проверяем, сколько запросов уже было сделано за последнюю минуту
        int requestCount = requestCountMap.getOrDefault(userId, 0);
        if (requestCount >= LIMIT) {
            long lastRequestTime = lastRequestTimeMap.get(userId);
            if (currentTime - lastRequestTime <= ONE_MINUTE) {
                return false; // Превышен лимит запросов
            } else {
                // Сбросить счетчик и обновить время последнего запроса после истечения минуты
                requestCountMap.put(userId, 0);
                lastRequestTimeMap.put(userId, currentTime);
            }
        }

        // Увеличиваем счетчик запросов и обновляем время последнего запроса
        requestCountMap.put(userId, requestCount + 1);
        lastRequestTimeMap.put(userId, currentTime);

        return true; // Запрос разрешен
    }



    private static final int REQUESTS_PER_MINUTE_LIMIT = 60;
    private static final long MINUTE_IN_MILLISECONDS = 60 * 1000;
    private static final Queue<Long> requestTimes = new LinkedList<>();;


    public void waitForRateLimit() {
        long currentTime = System.currentTimeMillis();
        long minuteAgo = currentTime - MINUTE_IN_MILLISECONDS;

        synchronized (requestTimes) {
            while (!requestTimes.isEmpty() && requestTimes.peek() < minuteAgo) {
                requestTimes.poll();
            }

            if (requestTimes.size() >= REQUESTS_PER_MINUTE_LIMIT) {
                long delay = minuteAgo - requestTimes.peek();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            requestTimes.add(currentTime);
        }
    }


    public boolean isBlocked() {

//       проверить константу если норм переслать и прибавить

        return false;
    }
}