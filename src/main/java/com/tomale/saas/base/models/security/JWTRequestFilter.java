package com.tomale.saas.base.models.security;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LogManager.getLogger(JWTRequestFilter.class);

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${app.cookie.name}")
    private String cookieName;

    @Value("${app.cipher.key}")
    private String cipherKey;

    @Autowired
    private AuthenticationManager authMgr;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // log.debug(cookie.getName());
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        String value = cookie.getValue();
                        JWTUtil jwt = new JWTUtil(jwtIssuer, jwtSecret, cipherKey);
                        if (jwt.verify(value)) {
                            try {
                                JsonObject json = jwt.toJSON(value);
                                // log.debug(json);

                                JWTAuthenticationToken preauth = new JWTAuthenticationToken(json);
                                // auth.setAuthenticated(true);
                                try {
                                    Authentication postauth = authMgr.authenticate(preauth);
                                    SecurityContextHolder.getContext().setAuthentication(postauth);
                                } catch(AuthenticationException e) {
                                    log.error(e);
                                }
                            } catch(Exception e) {
                                log.error("JWTRequestFilter::doFilterInternal()");
                                log.error(e);
                            }
                        } else {
                            log.warn("Invalid value for cookie '%s'", cookieName);
                        }
                    }
                }
            }
        }
        

        filterChain.doFilter(request, response);
    }

}