package com.mail.resilientEmail.util;

import java.util.ArrayList;
import java.util.Map;

public class Utility {
    private final static int rateLimit = 5;

    public static String makeKey(String to, String subject, String body) {
        return to + "|" + subject + "|" + body;
    }

    public static boolean canSend(String to, Map<String,ArrayList<Long>> rateLimitMap) {
        long now = System.currentTimeMillis();
        if (!rateLimitMap.containsKey(to)) {
            rateLimitMap.put(to, new ArrayList<>());
        }
        ArrayList<Long> times = rateLimitMap.get(to);
        // Remove timestamps older than 1 min
        ArrayList<Long> newTimes = new ArrayList<>();
        for (Long t : times) {
            if (now - t < 60000) {
                newTimes.add(t);
            }
        }
        rateLimitMap.put(to, newTimes);
        return newTimes.size() < rateLimit;
    }

    public static void addToRateLimit(String to, Map<String,ArrayList<Long>> rateLimitMap) {
        long now = System.currentTimeMillis();
        if (!rateLimitMap.containsKey(to)) {
            rateLimitMap.put(to, new ArrayList<>());
        }
        rateLimitMap.get(to).add(now);
    }
}
