package com.restassured.swarnava;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestJokesAPI {
    final String baseUrl = "https://v2.jokeapi.dev";

    @Test
    void testGetMiscJokes() {
        given()
            .when()
                .get(baseUrl + "/joke/Misc")
            .then()
                .statusCode(200)
                .body("category", equalTo("Misc"))
                .log().all();
    }

    @Test
    void testGetAnyJokes() {
        given()
            .when()
                .get(baseUrl + "/joke/Any")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    void testGetSafeJokes() {
        given()
            .when()
                .get(baseUrl + "/joke/Any?blacklistFlags=religious,political")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    void testGetSearchJokes() {
        String keyword = "school";
        Response response = given()
            .when()
                .get(baseUrl + "/joke/Any?contains=" + keyword)
            .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        String setup = response.jsonPath().get("setup");
        String delivery = response.jsonPath().get("delivery");
        boolean check1 = (setup.contains(keyword));
        boolean check2 = (delivery.contains(keyword));
        Assert.assertTrue((check1 || check2));
    }

    @Test
    void testGetCheckAmountOfJokes() {
        int amount = 3;
        given()
            .when()
                .get(baseUrl + "/joke/Any?amount=" + amount)
            .then()
                .statusCode(200)
                .body("size()", is(amount))
                .log().all();
    }
}
