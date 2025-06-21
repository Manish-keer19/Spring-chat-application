package com.ms.chat.application.Controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class OAuthRedirectController {

    @GetMapping("/auth/github")
    public void githubRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String platform = request.getParameter("platform"); // Get platform query param
        request.getSession().setAttribute("platform", platform); // Store it in session

        // Redirect to the actual Spring Security OAuth endpoint
        response.sendRedirect("/oauth2/authorization/github");
    }

    @GetMapping("/auth/google")
    public void googleRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String platform = request.getParameter("platform");
        request.getSession().setAttribute("platform", platform);

        response.sendRedirect("/oauth2/authorization/google");
    }
}
