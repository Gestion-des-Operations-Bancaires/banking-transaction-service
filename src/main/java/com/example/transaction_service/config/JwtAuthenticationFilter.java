package com.example.transaction_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${token.secret}")
    private String jwtSigningKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                // Token is valid; process claims if needed
                Object userId = claims.get("userId");
                request.setAttribute("userId", userId);
                request.setAttribute("jwt", jwt);

                // Continuer la chaîne de filtres avec un token valide
                filterChain.doFilter(request, response);
                return;

            } catch (ExpiredJwtException e) {
                // Utiliser ResponseEntity pour token expiré
                System.out.println("Token expired: " + e);
                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token has expired",
                        "The JWT token has expired. Please obtain a new token.");
                return;
            }
//            catch (Exception e) {
//                System.out.println("Token validation error: " + e);
//                // Utiliser ResponseEntity pour token invalide
//                handleErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token",
//                        "The JWT token is invalid or malformed.");
//                return;
//            }
        }

        // Pas de token ou token non Bearer, continuer la chaîne
        filterChain.doFilter(request, response);
    }


    /**
     * Méthode pour envoyer une réponse d'erreur formatée avec ResponseEntity
     */
    private void handleErrorResponse(HttpServletResponse response, HttpStatus status,
                                     String error, String message) throws IOException {
        // Créer l'objet ApiError
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", new Date().getTime());

        // Créer un ResponseEntity avec notre objet d'erreur
        ResponseEntity<Map<String, Object>> responseEntity =
                new ResponseEntity<>(errorResponse, status);

        // Configurer manuellement la réponse HTTP selon le ResponseEntity
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Écrire le corps JSON dans la réponse
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseEntity.getBody());


        System.out.println("Response: " + responseEntity.getBody());
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSigningKey.getBytes());
    }
}
