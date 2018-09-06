package com.github.fromi.openidconnect;

import com.github.fromi.openidconnect.security.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class SampleSecuredController {
    @Value("${oauth2.clientId}")
    private String clientId;

    @Value("${oauth2.checkSessionUri}")
    private String checkSessionUri;

    @Value("${oauth2.authorizationUri}")
    private String authorizationUri;

    @Value("${oauth2.redirectUri}")
    private String redirectUri;

    @Value("${oauth2.logoutUri}")
    private String logoutUri;

    @Value("${oauth2.baseUri}")
    private String baseUri;

    @RequestMapping("/test")
    public String test(Model model) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userId", userInfo.getName());
        model.addAttribute("checkSessionUri", checkSessionUri + "?client_id=" + clientId);
        return "test";
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format("%s?post_logout_redirect_uri=%s", logoutUri, baseUri));
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userId", userInfo.getName());
        model.addAttribute("checkSessionUri", checkSessionUri + "?client_id=" + clientId);
        return "admin";
    }
}
