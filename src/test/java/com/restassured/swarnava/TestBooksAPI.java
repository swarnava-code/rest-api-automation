package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestBooksAPI {
    String accessToken = "bff6c3e279a31e6f73fdbee51737a77d8768a7ab2d3202ba8aa3610614d5dec6";
    String orderId;
    String baseUrl = "https://simple-books-api.glitch.me";

    @Test(priority = 1)
    void testPostRegisterAPIClient() {
        String randomMail = "swarnava"+Math.random()+"@gmail.com";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientName", "Swarnava Chakraborty");
        jsonObject.put("clientEmail", randomMail);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
            .when()
                .post(baseUrl+"/api-clients")
            .then()
                .statusCode(201)
                .body("$", hasKey("accessToken"))
                .log().all();
    }

    @Test(priority = 2)
    void testPostOrder() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bookId", 1);
        jsonObject.put("customerName", "Swarnava Chakraborty");
        Response response =  given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
            .when()
                .post(baseUrl+"/orders")
            .then()
                .statusCode(201)
                .body("created", equalTo(true))
                .body("$", hasKey("orderId"))
                .log().all()
                .extract().response();
        orderId = response.jsonPath().get("orderId");
    }

    @Test(priority = 3)
    void testGetStatus() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .get(baseUrl+"/status")
            .then()
                .statusCode(200)
                .body("status", equalTo("OK"))
                .log().all();
    }

    @Test(priority = 4)
    void testGetBooks() {
        String type = "fiction"; //fiction or non-fiction
        int limit = 3; // 1 to 20
        given()
                .contentType(ContentType.JSON)
                .param("type", type)
                .param("limit", limit)
            .when()
                .get(baseUrl+"/books")
            .then()
                .statusCode(200)
                .body("size()", equalTo(limit))
                .log().all();
    }

    @Test(priority = 5)
    void testGetSingleBook() {
        int bookId = 5;
        given()
                .contentType(ContentType.JSON)
                .pathParam("bookId", bookId)
                .accept(ContentType.JSON)
            .when()
                .get(baseUrl+"/books/{bookId}")
            .then()
                .statusCode(200)
                .body("id", equalTo(bookId))
                .log().all();
    }

    @Test(priority = 6)
    void testGetOrders() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
            .when()
                .get(baseUrl+"/orders")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .log().all();
    }

    @Test(priority = 7)
    void testGetSingleOrder() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .pathParam("orderId", orderId)
            .when()
                .get(baseUrl+"/orders/{orderId}")
            .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .log().all();
    }

    @Test(priority = 8)
    void testPatchOrder() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerName", "Pk2");
        given()
                .pathParam("orderId", orderId)
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
            .when()
                .patch(baseUrl+"/orders/{orderId}")
            .then()
                .statusCode(204)
                .log().all();
    }

    @Test(priority = 9)
    void testDeleteOrder() {
        given()
                .pathParam("orderId", orderId)
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .delete(baseUrl+"/orders/{orderId}")
            .then()
                .statusCode(204)
                .log().all();
    }
}