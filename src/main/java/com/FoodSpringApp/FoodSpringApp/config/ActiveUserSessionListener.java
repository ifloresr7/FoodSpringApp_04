package com.FoodSpringApp.FoodSpringApp.config;

import jakarta.servlet.http.HttpSessionEvent;

public class ActiveUserSessionListener implements jakarta.servlet.http.HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        SessionManager.addSession(sessionId);
        System.out.println("Sesión creada: " + sessionId);
        SessionManager.logActiveSessions();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        SessionManager.removeSession(sessionId);
        System.out.println("Sesión destruida: " + sessionId);
        SessionManager.logActiveSessions();
    }
}
