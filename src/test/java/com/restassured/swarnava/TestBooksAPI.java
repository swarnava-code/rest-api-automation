package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

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
        Response response =  given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
                .when()
                .post(url)
                .then()
                .statusCode(201)
                .body("$", hasKey("accessToken"))
                .log().all()
                .extract().response();
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

    /*
    all get methods will implemented here
     */

    @Test(priority = 3)
    void testPatchOrder() {
        String url = "https://simple-books-api.glitch.me/orders/"+orderId;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerName", "Pk2");
        Response response =  given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonObject.toJSONString())
                .when()
                .patch(url)
                .then()
                .statusCode(204)
                .log().all()
                .extract().response();
    }

    @Test(priority = 4)
    void testDeleteOrder() {
        String url = "https://simple-books-api.glitch.me/orders/"+orderId;
        Response response =  given()
                .header("Authorization", "Bearer "+accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .statusCode(204)
                .log().all()
                .extract().response();
    }
}
