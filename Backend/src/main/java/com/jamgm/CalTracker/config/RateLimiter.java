package com.jamgm.CalTracker.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {
    private final ConcurrentHashMap<String, UserRequestData> requestCounts = new ConcurrentHashMap<>();
    private final static int REQUEST_LIMIT = 1000;
    private final static long TIME_WINDOW = 1000; // in milliseconds

    public boolean isAllowed(String userId) {
        long currentTime = System.currentTimeMillis();
        requestCounts.compute(userId, (key, requestData) -> {
            if (requestData == null || currentTime - requestData.startTime > TIME_WINDOW) {
                return new UserRequestData(1, currentTime); // reset count
            } else {
                requestData.requestCount++;
                return requestData;
            }
        });

        return requestCounts.get(userId).requestCount <= REQUEST_LIMIT;
    }

    private static class UserRequestData {
        int requestCount;
        long startTime;

        public UserRequestData(int requestCount, long startTime) {
            this.requestCount = requestCount;
            this.startTime = startTime;
        }
    }
}
