package com.soasec;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

@SpringBootApplication
@Configuration
@RestController
public class WebController {

    public static void main(String[] args) {
        SpringApplication.run(WebController.class, args);
    }

    @GetMapping("/callbackPage")
    @ResponseBody
    public String callbackPage(@RequestParam(required = false) String code) throws ParseException {
        // clientapp:strongpassword
        if (code != null) { // Auth code
            Connection.Response res;
            try {
                res = Jsoup
                        .connect("https://localhost:8443/" + URLEncoder.encode("oauth", "UTF-8") + "/"
                                + URLEncoder.encode("token", "UTF-8"))
                        .data("code", code)
                        .data("grant_type", "authorization_code")
                        .data("redirect_uri", "https://localhost:8444/callbackPage")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("authorization", "Basic Y2xpZW50YXBwOnN0cm9uZ3Bhc3N3b3Jk")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException ex) {
                System.out.println("Cannot connect to the Authorization Server");
                return "Error: " + ex;
            }
            return printResponse(res);
        } else {
            return "Implicit grant type: Access token is in the URL";
        }
    }

    @GetMapping("/refreshToken")
    @ResponseBody
    public String refreshToken(@RequestParam String token) throws ParseException {
        Connection.Response res;
        try {
            res = Jsoup
                    .connect("https://localhost:8443/" + URLEncoder.encode("oauth", "UTF-8") + "/"
                            + URLEncoder.encode("token", "UTF-8"))
                    .data("refresh_token", token)
                    .data("grant_type", "refresh_token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("authorization", "Basic Y2xpZW50YXBwOnN0cm9uZ3Bhc3N3b3Jk")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException ex) {
            System.out.println("Cannot connect to the Authorization Server");
            return "Error: " + ex;
        }
        return printResponse(res);
    }

    @GetMapping("/password")
    @ResponseBody
    public String password(@RequestParam String username, @RequestParam String password) throws ParseException {
        Connection.Response res;
        try {
            res = Jsoup
                    .connect("https://localhost:8443/" + URLEncoder.encode("oauth", "UTF-8") + "/"
                            + URLEncoder.encode("token", "UTF-8"))
                    .data("username", username)
                    .data("password", password)
                    .data("grant_type", "password")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("authorization", "Basic Y2xpZW50YXBwOnN0cm9uZ3Bhc3N3b3Jk")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException ex) {
            System.out.println("Cannot connect to the Authorization Server");
            return "Error: " + ex;
        }
        return printResponse(res);
    }

    @GetMapping("/clientCredentials")
    @ResponseBody
    public String clientCredentials() {
        Connection.Response res;
        try {
            res = Jsoup
                    .connect("https://localhost:8443/" + URLEncoder.encode("oauth", "UTF-8") + "/"
                            + URLEncoder.encode("token", "UTF-8"))
                    .data("grant_type", "client_credentials")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("authorization", "Basic Y2xpZW50YXBwOnN0cm9uZ3Bhc3N3b3Jk")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException ex) {
            System.out.println("Cannot connect to the Authorization Server");
            return "Error: " + ex;
        }
        return res.body();
    }

    private String printResponse(Connection.Response res) throws ParseException {
        String result = res.body();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);
        String accessToken = (String) json.get("access_token");
        try {
            res = Jsoup
                    .connect("https://localhost:8443/" + URLEncoder.encode("api", "UTF-8") + "/"
                            + URLEncoder.encode("users", "UTF-8") + "/" + URLEncoder.encode("me", "UTF-8"))
                    .header("authorization", "Bearer " + accessToken)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.GET)
                    .execute();
            return "<p>" + result + "</p><p>Risposta Resource Server: " + res.body() + "</p>";
        } catch (IOException ex) {
            System.out.println("Cannot connect to the Authorization Server");
            return "Error: " + ex;
        }
    }
}
