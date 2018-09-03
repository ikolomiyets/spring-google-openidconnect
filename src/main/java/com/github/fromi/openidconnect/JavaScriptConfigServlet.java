package com.github.fromi.openidconnect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class JavaScriptConfigServlet extends HttpServlet {
    private String clientId;

    private String authorizationUri;

    private String redirectUri;

    private String targetOrigin;

    @Override
    public void init() throws ServletException {
        super.init();
        Properties properties = new Properties();

        InputStream is = ClassLoader.getSystemResourceAsStream("application.properties");
        try {
            properties.load(is);
            this.clientId = properties.getProperty("oauth2.clientId");
            this.authorizationUri = properties.getProperty("oauth2.authorizationUri");
            this.targetOrigin = properties.getProperty("oauth2.checkSessionUri");
            this.redirectUri = properties.getProperty("oauth2.redirectUri");
        } catch (Exception e) {

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionState = null;

        HttpSession session = req.getSession(false);
        if (session != null) {
            sessionState = (String)session.getAttribute("session_state");
        }

        resp.setContentType("application/javascript");
        PrintWriter out = resp.getWriter();
        try {
            out.println("  var stat = 'unchanged';");
            out.println(String.format("  var client_id =  '%s'", clientId));
            if (sessionState != null) {
                out.println(String.format("  var session_state = '%s'", sessionState));
            }
            out.println("  var mes = client_id + ' ' + session_state;");
            out.println(String.format("  var authorizationEndpoint = '%s';", authorizationUri));
            out.println(String.format("  var targetOrigin = '%s'", targetOrigin));
            out.println(String.format("  var redirectUri = '%s'", redirectUri));
        } finally {
            out.flush();
            out.close();
        }
    }
}
