package com.github.fromi.openidconnect;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIT {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void welcomePageNotRedirected() {
        given().redirects().follow(false).when().get("/").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void securedPageRedirectsToLoginPage() {
        given().redirects().follow(false).when().get("/test").then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .header("Location", endsWith("/login"));
    }

    @Test
    public void loginPageRedirectsToGoogle() {
        given().redirects().follow(false).when().get("/login").then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .header("Location", startsWith("http://localhost:9763/oauth2/authorize"));
    }
}
