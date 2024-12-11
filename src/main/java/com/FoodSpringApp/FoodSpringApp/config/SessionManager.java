package com.FoodSpringApp.FoodSpringApp.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Map<String, String> activeSessions = Collections.synchronizedMap(new HashMap<>());

    public static void addSession(String sessionId) {
        activeSessions.put(sessionId, sessionId);
    }

    public static void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public static Map<String, String> getAllSessions() {
        return activeSessions;
    }

    public static void logActiveSessions() {
        System.out.println("Sesiones activas en el servidor: " + activeSessions.keySet());
    }
}
