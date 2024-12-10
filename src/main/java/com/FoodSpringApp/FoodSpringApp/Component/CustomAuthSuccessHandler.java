package com.FoodSpringApp.FoodSpringApp.Component;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    private static final String ENCRYPTION_KEY = "3sG%#?3B.~+4RAy3ZsPXmR]I7io5j=)2*nE#4kZwG1,D9_0^E87NkR4y[Hhm6Yl,";
    private static ConcurrentHashMap<String, HttpSession> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        activeSessions.forEach((sessionId_, session) -> System.out.println("SessionId: " + sessionId_));
        String sessionId = request.getSession().getId();
        String encryptedSessionId = encrypt(sessionId);
        Cookie cookie = new Cookie("token_custom_foodspringapp", encryptedSessionId);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/"); 
    }

    public static boolean isSessionIdValid(String token) {
        try {
            String sessionId = decrypt(token);
            return true;
    
        } catch (Exception e) {
            System.err.println("Error al descifrar el token: " + e.getMessage());
            return false;
        }
    }

    private String encrypt(String data) {
        try {
            byte[] keyBytes = ENCRYPTION_KEY.substring(0, 32).getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar el sessionId", e);
        }
    }

    private static String decrypt(String data) {
        try {
            byte[] keyBytes = ENCRYPTION_KEY.substring(0, 32).getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(data);
            byte[] originalBytes = cipher.doFinal(decodedBytes);

            return new String(originalBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar el token", e);
        }
    }
}