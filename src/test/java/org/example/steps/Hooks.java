package org.example.steps;

import io.cucumber.java.After;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Hooks {
    @After()
    public void clearCookies(){
        given().cookies(new Cookies());
    }
}
