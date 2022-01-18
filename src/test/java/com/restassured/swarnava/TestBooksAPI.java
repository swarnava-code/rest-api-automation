package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestBooksAPI {
    String accessToken = "bff6c3e279a31e6f73fdbee51737a77d8768a7ab2d3202ba8aa3610614d5dec6";
    String orderId;

    @Test(priority = 1)
    void testPostRegisterAPIClient() {
        String url = "https://simple-books-api.glitch.me/api-clients/";
        String randomMail = "swarnava"+Math.random()+"@gmail.com";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientName", "Swarnava Chakraborty");
        jsonObject.put("clientEmail", randomMail);
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
                .when()
                .post(url)
                .then()
                .statusCode(201)
                .body("$", hasKey("accessToken"))
                .log().all();
    }

    @Test(priority = 2)
    void testPostOrder() {
        String url = "https://simple-books-api.glitch.me/orders";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bookId", 1);
        jsonObject.put("customerName", "Swarnava Chakraborty");
        Response response =  given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
                .when()
                .post(url)
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
        String url = "https://simple-books-api.glitch.me/status/";
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("status", equalTo("OK"))
                .log().all();
    }

    @Test(priority = 4)
    void testGetBooks() {
        String url = "https://simple-books-api.glitch.me/books/";
        String type = "fiction"; //fiction or non-fiction
        int limit = 3; // 1 to 20
        given()
                .contentType(ContentType.JSON)
                .param("type", type)
                .param("limit", limit)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("size()", equalTo(limit))
                .log().all();
    }

    @Test(priority = 5)
    void testGetSingleBook() {
        int bookId = 5;
        String url = "https://simple-books-api.glitch.me/books/{bookId}";
        given()
                .contentType(ContentType.JSON)
                .pathParam("bookId", bookId)
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("id", equalTo(bookId))
                .log().all();
    }

    @Test(priority = 6)
    void testGetOrders() {
        String url = "https://simple-books-api.glitch.me/orders";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .log().all();
    }

    @Test(priority = 7)
    void testGetSingleOrder() {
        String url = "https://simple-books-api.glitch.me/orders/{orderId}";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer "+accessToken)
                .pathParam("orderId", orderId)
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .body("id", equalTo(orderId))
                .log().all();
    }

    @Test(priority = 8)
    void testPatchOrder() {
        String url = "https://simple-books-api.glitch.me/orders/"+orderId;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerName", "Pk2");
        given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
                .when()
                .patch(url)
                .then()
                .statusCode(204)
                .log().all();
    }

    @Test(priority = 9)
    void testDeleteOrder() {
        String url = "https://simple-books-api.glitch.me/orders/"+orderId;
        given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .statusCode(204)
                .log().all();
    }
}
